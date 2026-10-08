package com.mag5112005.drugdose.data.repository

import com.mag5112005.drugdose.data.DrugDatabase
import com.mag5112005.drugdose.data.api.FDAApiService
import com.mag5112005.drugdose.data.api.FDADrug
import com.mag5112005.drugdose.data.models.DrugData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DrugRepository(
    private val fdaApiService: FDAApiService = FDAApiService.create()
) {
    // Cache for FDA drugs to avoid repeated API calls
    private val fdaCache = mutableMapOf<String, List<DrugData>>()

    /**
     * Search drugs: Search BOTH local DB AND FDA API simultaneously with fuzzy matching
     */
    suspend fun searchDrugs(query: String): SearchResult {
        return withContext(Dispatchers.IO) {
            try {
                val normalizedQuery = query.lowercase().trim()
                
                // Step 1: Search local database with fuzzy matching
                val localResults = searchLocalWithFuzzy(query)
                
                // Step 2: Check cache for FDA results
                val cachedKey = normalizedQuery
                val cachedFDAResults = fdaCache[cachedKey]
                
                // Step 3: Search FDA API (ALWAYS, not just when local is empty)
                val fdaResults = try {
                    if (cachedFDAResults != null) {
                        cachedFDAResults
                    } else {
                        val results = searchFDA(query)
                        if (results.isNotEmpty()) {
                            fdaCache[cachedKey] = results
                        }
                        results
                    }
                } catch (e: Exception) {
                    emptyList()
                }
                
                // Combine and deduplicate results
                val allResults = (localResults + fdaResults)
                    .distinctBy { it.name.lowercase() }
                    .sortedByDescending { drug ->
                        // Sort by relevance: exact match > starts with > contains
                        when {
                            drug.name.equals(query, ignoreCase = true) -> 3
                            drug.name.startsWith(query, ignoreCase = true) -> 2
                            else -> 1
                        }
                    }
                
                when {
                    allResults.isNotEmpty() -> {
                        val source = when {
                            localResults.isNotEmpty() && fdaResults.isNotEmpty() -> "Local + FDA Database"
                            fdaResults.isNotEmpty() -> "FDA Database"
                            else -> "Local Database"
                        }
                        SearchResult.Success(allResults, source = source)
                    }
                    else -> SearchResult.NotFound("No medication found matching \"$query\"")
                }
            } catch (e: Exception) {
                SearchResult.Error("Search error: ${e.message}", emptyList())
            }
        }
    }

    /**
     * Search local database with fuzzy matching and autocorrect
     */
    private fun searchLocalWithFuzzy(query: String): List<DrugData> {
        val allDrugs = DrugDatabase.getDrugList()
        val normalizedQuery = query.lowercase().trim()
        
        // Exact and partial matches
        val matches = allDrugs.filter { drug ->
            val drugName = drug.name.lowercase()
            val category = drug.category.lowercase()
            
            // Exact match
            drugName == normalizedQuery ||
            // Starts with
            drugName.startsWith(normalizedQuery) ||
            // Contains
            drugName.contains(normalizedQuery) ||
            category.contains(normalizedQuery) ||
            // Word boundary match (e.g., "metro" matches "Metformin")
            drugName.split(" ", "-").any { it.startsWith(normalizedQuery) } ||
            // Fuzzy match for typos (Levenshtein distance)
            levenshteinDistance(drugName, normalizedQuery) <= 2
        }
        
        return matches
    }
    
    /**
     * Calculate Levenshtein distance for fuzzy matching (autocorrect)
     */
    private fun levenshteinDistance(s1: String, s2: String): Int {
        val len1 = s1.length
        val len2 = s2.length
        
        // Short circuit for performance
        if (len1 == 0) return len2
        if (len2 == 0) return len1
        if (Math.abs(len1 - len2) > 3) return 999 // Too different
        
        val dp = Array(len1 + 1) { IntArray(len2 + 1) }
        
        for (i in 0..len1) dp[i][0] = i
        for (j in 0..len2) dp[0][j] = j
        
        for (i in 1..len1) {
            for (j in 1..len2) {
                val cost = if (s1[i - 1] == s2[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,      // deletion
                    dp[i][j - 1] + 1,      // insertion
                    dp[i - 1][j - 1] + cost // substitution
                )
            }
        }
        
        return dp[len1][len2]
    }

    /**
     * Search FDA API with fuzzy matching and autocorrect
     */
    private suspend fun searchFDA(query: String): List<DrugData> {
        return try {
            val normalizedQuery = query.lowercase().trim()
            
            // Enhanced search strategies with wildcard and fuzzy matching
            val searchQueries = listOf(
                // Exact matches
                "openfda.generic_name:\"$query\"",
                "openfda.brand_name:\"$query\"",
                // Prefix matches (autocomplete)
                "openfda.generic_name:$query*",
                "openfda.brand_name:$query*",
                "openfda.substance_name:$query*",
                // Partial matches for autocorrect
                "openfda.generic_name:*$query*",
                "openfda.brand_name:*$query*",
                // Try without wildcards for broader match
                "openfda.generic_name:$query",
                "openfda.brand_name:$query"
            )
            
            val allResults = mutableListOf<DrugData>()
            
            for (searchQuery in searchQueries) {
                try {
                    val response = fdaApiService.searchDrug(searchQuery, limit = 20)
                    val results = response.results
                    if (!results.isNullOrEmpty()) {
                        val converted = results.mapNotNull { fdaDrug ->
                            convertFDADrugToDrugData(fdaDrug)
                        }
                        allResults.addAll(converted)
                        
                        // If we found good matches, no need to try more queries
                        if (converted.any { it.name.contains(query, ignoreCase = true) }) {
                            break
                        }
                    }
                } catch (e: Exception) {
                    // Try next query
                    continue
                }
            }
            
            // Deduplicate and filter by relevance
            allResults
                .distinctBy { it.name.lowercase() }
                .filter { drug ->
                    val drugName = drug.name.lowercase()
                    // More lenient filtering - fuzzy match
                    drugName.contains(normalizedQuery) ||
                    normalizedQuery.contains(drugName) ||
                    levenshteinDistance(drugName, normalizedQuery) <= 3
                }
                .take(15) // Limit results
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Convert FDA drug data to our DrugData model
     */
    private fun convertFDADrugToDrugData(fdaDrug: FDADrug): DrugData? {
        val openFda = fdaDrug.openFda ?: return null
        val genericName = openFda.genericName?.firstOrNull() ?: 
                         openFda.substanceName?.firstOrNull() ?: return null
        
        // Extract dosage info from dosage_and_administration if available
        val dosageText = fdaDrug.dosageAndAdministration?.firstOrNull() ?: ""
        val standardDose = extractDoseFromText(dosageText)
        
        // Determine category from pharm class
        val category = openFda.pharmClass?.firstOrNull()?.let { 
            it.split("[")[0].trim() 
        } ?: "Medication"
        
        // Get routes
        val routes = openFda.route?.map { it.uppercase() } ?: listOf("PO")
        
        // Extract warnings
        val warnings = fdaDrug.warnings?.take(3)?.map { 
            it.replace(Regex("<[^>]*>"), "").take(200) 
        } ?: emptyList()
        
        // Extract contraindications
        val contraindications = fdaDrug.contraindications?.take(3)?.map { 
            it.replace(Regex("<[^>]*>"), "").take(200) 
        } ?: emptyList()
        
        // Extract interactions
        val interactions = fdaDrug.drugInteractions?.take(3)?.map {
            it.replace(Regex("<[^>]*>"), "").take(200)
        } ?: emptyList()

        return DrugData(
            id = "fda_${genericName.replace(" ", "_").lowercase()}",
            name = genericName,
            category = category,
            routes = routes,
            standardDose = standardDose,
            doseUnit = "mg",
            frequency = "As directed",
            maxDailyDose = standardDose * 4, // Conservative estimate
            renalAdjustment = warnings.any { it.contains("renal", ignoreCase = true) },
            hepaticAdjustment = warnings.any { it.contains("hepatic", ignoreCase = true) || 
                                                it.contains("liver", ignoreCase = true) },
            pregnancyCategory = "C", // Default, would need specific lookup
            contraindications = contraindications.ifEmpty { listOf("See full prescribing information") },
            warnings = warnings.ifEmpty { listOf("Consult healthcare provider") },
            interactions = interactions.ifEmpty { listOf("Consult pharmacist for interactions") }
        )
    }

    /**
     * Extract dose from FDA text
     */
    private fun extractDoseFromText(text: String): Double {
        // Try to find dose in mg
        val mgPattern = Regex("""(\d+)\s*mg""", RegexOption.IGNORE_CASE)
        val mgMatch = mgPattern.find(text)
        if (mgMatch != null) {
            return mgMatch.groupValues[1].toDoubleOrNull() ?: 100.0
        }
        
        // Default dose
        return 100.0
    }

    /**
     * Get all local drugs
     */
    fun getAllLocalDrugs(): List<DrugData> {
        return DrugDatabase.getDrugList()
    }

    /**
     * Get drug by exact name (check local first, then FDA)
     */
    suspend fun getDrugByName(name: String): DrugData? {
        return withContext(Dispatchers.IO) {
            // Check local first
            val localDrug = DrugDatabase.getDrugList().find { 
                it.name.equals(name, ignoreCase = true) 
            }
            if (localDrug != null) return@withContext localDrug

            // Search FDA
            try {
                val fdaResults = searchFDA(name)
                fdaResults.find { it.name.equals(name, ignoreCase = true) }
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Clear FDA cache
     */
    fun clearCache() {
        fdaCache.clear()
    }
}

sealed class SearchResult {
    data class Success(val drugs: List<DrugData>, val source: String) : SearchResult()
    data class Error(val message: String, val partialResults: List<DrugData>) : SearchResult()
    data class NotFound(val message: String) : SearchResult()
}
