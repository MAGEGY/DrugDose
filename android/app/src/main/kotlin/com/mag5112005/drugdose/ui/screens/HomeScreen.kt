package com.mag5112005.drugdose.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mag5112005.drugdose.domain.DoseCalculator
import com.mag5112005.drugdose.data.models.PatientData
import com.mag5112005.drugdose.ui.components.ProfessionalDoseDisplay
import com.mag5112005.drugdose.data.repository.DrugRepository
import com.mag5112005.drugdose.data.repository.SearchResult
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var drugName by remember { mutableStateOf("") }
    var drugSearchText by remember { mutableStateOf("") }
    var showDrugSuggestions by remember { mutableStateOf(false) }
    var showResult by remember { mutableStateOf(false) }
    var doseResult by remember { mutableStateOf<com.mag5112005.drugdose.data.models.DoseRecommendation?>(null) }
    var selectedGender by remember { mutableStateOf("Male") }
    var renalFunction by remember { mutableStateOf("Normal") }
    var hepaticFunction by remember { mutableStateOf("Normal") }
    var selectedRoute by remember { mutableStateOf("Oral") }
    var isSearching by remember { mutableStateOf(false) }
    var searchSource by remember { mutableStateOf("") }
    var searchError by remember { mutableStateOf("") }
    
    val repository = remember { DrugRepository() }
    val scope = rememberCoroutineScope()
    var searchJob by remember { mutableStateOf<Job?>(null) }
    
    var filteredDrugs by remember { mutableStateOf(repository.getAllLocalDrugs()) }
    
    val genders = listOf("Male", "Female")
    val renalFunctions = listOf("Normal", "Mild Impairment", "Moderate Impairment", "Severe Impairment")
    val hepaticFunctions = listOf("Normal", "Mild Impairment", "Moderate Impairment", "Severe Impairment")
    val routes = listOf("Oral", "IV (Intravenous)", "IM (Intramuscular)", "SC (Subcutaneous)", "Rectal", "Topical", "Sublingual", "Inhalation")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.MedicalServices,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("DrugDose Calculator")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Pharmaceutical Dose Calculator",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Calculate accurate medication dosages based on patient data",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            Text(
                "Patient Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age (years)") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight (kg)") },
                leadingIcon = { Icon(Icons.Default.FitnessCenter, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            
            var genderExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = genderExpanded,
                onExpandedChange = { genderExpanded = !genderExpanded }
            ) {
                OutlinedTextField(
                    value = selectedGender,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Gender") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = genderExpanded,
                    onDismissRequest = { genderExpanded = false }
                ) {
                    genders.forEach { gender ->
                        DropdownMenuItem(
                            text = { Text(gender) },
                            onClick = {
                                selectedGender = gender
                                genderExpanded = false
                            }
                        )
                    }
                }
            }
            
            var renalExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = renalExpanded,
                onExpandedChange = { renalExpanded = !renalExpanded }
            ) {
                OutlinedTextField(
                    value = renalFunction,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Renal Function") },
                    leadingIcon = { Icon(Icons.Default.Biotech, contentDescription = null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = renalExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = renalExpanded,
                    onDismissRequest = { renalExpanded = false }
                ) {
                    renalFunctions.forEach { function ->
                        DropdownMenuItem(
                            text = { Text(function) },
                            onClick = {
                                renalFunction = function
                                renalExpanded = false
                            }
                        )
                    }
                }
            }
            
            var hepaticExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = hepaticExpanded,
                onExpandedChange = { hepaticExpanded = !hepaticExpanded }
            ) {
                OutlinedTextField(
                    value = hepaticFunction,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Hepatic Function") },
                    leadingIcon = { Icon(Icons.Default.LocalHospital, contentDescription = null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = hepaticExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = hepaticExpanded,
                    onDismissRequest = { hepaticExpanded = false }
                ) {
                    hepaticFunctions.forEach { function ->
                        DropdownMenuItem(
                            text = { Text(function) },
                            onClick = {
                                hepaticFunction = function
                                hepaticExpanded = false
                            }
                        )
                    }
                }
            }
            
            Text(
                "Medication & Route",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            OutlinedTextField(
                value = drugSearchText,
                onValueChange = { 
                    drugSearchText = it
                    drugName = it
                    showDrugSuggestions = it.isNotEmpty()
                    searchError = ""
                    
                    // Cancel previous search
                    searchJob?.cancel()
                    
                    if (it.isEmpty()) {
                        // Reset to all local drugs
                        filteredDrugs = repository.getAllLocalDrugs()
                        searchSource = ""
                        isSearching = false
                    } else {
                        // ALWAYS search both local + FDA with fuzzy matching and autocorrect
                        // Start with 2 characters for better UX
                        if (it.length >= 2) {
                            searchJob = scope.launch {
                                // Short debounce for responsive typing
                                delay(300)
                                isSearching = true
                                searchSource = "Searching..."
                                
                                try {
                                    when (val result = repository.searchDrugs(it)) {
                                        is SearchResult.Success -> {
                                            filteredDrugs = result.drugs
                                            searchSource = result.source
                                            searchError = ""
                                        }
                                        is SearchResult.Error -> {
                                            if (result.partialResults.isNotEmpty()) {
                                                filteredDrugs = result.partialResults
                                                searchSource = "Local Database"
                                            } else {
                                                filteredDrugs = emptyList()
                                            }
                                            searchError = "Search error. Showing available results."
                                        }
                                        is SearchResult.NotFound -> {
                                            filteredDrugs = emptyList()
                                            searchError = "No medication found. Try different spelling or shorter term."
                                        }
                                    }
                                } catch (e: Exception) {
                                    filteredDrugs = emptyList()
                                    searchError = "Search unavailable. Check connection."
                                }
                                
                                isSearching = false
                            }
                        } else {
                            // For 1 character, do quick local-only search
                            val quickLocal = repository.getAllLocalDrugs().filter { drug ->
                                drug.name.startsWith(it, ignoreCase = true)
                            }
                            filteredDrugs = quickLocal
                            searchSource = if (quickLocal.isNotEmpty()) "Local Database" else ""
                        }
                    }
                },
                label = { Text("Search Medication") },
                leadingIcon = {
                    if (isSearching) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                },
                placeholder = { Text("Start typing... autocorrect enabled") },
                supportingText = {
                    if (drugSearchText.isEmpty()) {
                        Text(
                            "Search 36 local drugs + millions from FDA • Typo-tolerant",
                            style = MaterialTheme.typography.labelSmall
                        )
                    } else if (isSearching) {
                        Text(
                            "Searching local + online databases...",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                trailingIcon = {
                    if (drugSearchText.isNotEmpty()) {
                        IconButton(onClick = { 
                            drugSearchText = ""
                            drugName = ""
                            showDrugSuggestions = false
                            filteredDrugs = repository.getAllLocalDrugs()
                            searchSource = ""
                            searchError = ""
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                }
            )
            
            // Show search source indicator
            if (searchSource.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            searchSource.contains("FDA") -> MaterialTheme.colorScheme.primaryContainer
                            searchSource == "Local Database" -> MaterialTheme.colorScheme.secondaryContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            when {
                                searchSource.contains("Local + FDA") -> Icons.Default.CloudDone
                                searchSource == "FDA Database" -> Icons.Default.CloudDone
                                else -> Icons.Default.Storage
                            },
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            if (searchSource == "Searching...") "Searching local + FDA..." else "Source: $searchSource",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (searchSource.contains("FDA")) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
            
            // Show search error
            if (searchError.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            searchError,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
            
            if (showDrugSuggestions && drugSearchText.isNotEmpty()) {
                if (filteredDrugs.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(4.dp)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "${filteredDrugs.size} medication(s) found",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                if (isSearching) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(12.dp),
                                            strokeWidth = 1.dp
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            "Searching online...",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                }
                            }
                            filteredDrugs.take(5).forEach { drug ->
                                TextButton(
                                    onClick = {
                                        drugName = drug.name
                                        drugSearchText = drug.name
                                        showDrugSuggestions = false
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.Medication,
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column(horizontalAlignment = Alignment.Start) {
                                            Text(
                                                drug.name,
                                                style = MaterialTheme.typography.bodyLarge,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                drug.category,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                                if (drug != filteredDrugs.take(5).last()) {
                                    Divider()
                                }
                            }
                            if (filteredDrugs.size > 5) {
                                Text(
                                    "...and ${filteredDrugs.size - 5} more",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                } else if (!isSearching && drugSearchText.length >= 3) {
                    // Only show "not found" after user has typed 3+ characters and search is complete
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "No exact matches found",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Try:\n• Different spelling\n• Generic name instead of brand\n• Shorter search term",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
            
            // Show selected drug indicator
            if (drugName.isNotBlank() && !showDrugSuggestions) {
                val selectedDrug = filteredDrugs.find { it.name.equals(drugName, ignoreCase = true) }
                if (selectedDrug != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Selected: ${selectedDrug.name}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    selectedDrug.category,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "\"$drugName\" not found in database. Select from suggestions above.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }
            
            var routeExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = routeExpanded,
                onExpandedChange = { routeExpanded = !routeExpanded }
            ) {
                OutlinedTextField(
                    value = selectedRoute,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Route of Administration") },
                    leadingIcon = { Icon(Icons.Default.LocalPharmacy, contentDescription = null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = routeExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = routeExpanded,
                    onDismissRequest = { routeExpanded = false }
                ) {
                    routes.forEach { route ->
                        DropdownMenuItem(
                            text = { Text(route) },
                            onClick = {
                                selectedRoute = route
                                routeExpanded = false
                            }
                        )
                    }
                }
            }
            
            val isValidDrug = drugName.isNotBlank() && 
                filteredDrugs.any { it.name.equals(drugName, ignoreCase = true) }
            val canCalculate = age.isNotBlank() && weight.isNotBlank() && isValidDrug
            
            Button(
                onClick = {
                    if (canCalculate) {
                        scope.launch {
                            val patientData = PatientData(
                                age = age.toIntOrNull() ?: 0,
                                weight = weight.toDoubleOrNull() ?: 0.0,
                                gender = selectedGender,
                                renalFunction = renalFunction,
                                hepaticFunction = hepaticFunction,
                                isPregnant = false,
                                isLactating = false
                            )
                            
                            val calculator = DoseCalculator()
                            val drugData = repository.getDrugByName(drugName)
                            
                            if (drugData != null) {
                                val routeShort = selectedRoute.split(" ").first()
                                doseResult = calculator.calculateDose(patientData, drugData, routeShort)
                                showResult = true
                                showDrugSuggestions = false
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp),
                enabled = canCalculate,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (canCalculate) MaterialTheme.colorScheme.primary 
                                    else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (canCalculate) MaterialTheme.colorScheme.onPrimary 
                                  else MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Icon(Icons.Default.Calculate, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Calculate Dose", style = MaterialTheme.typography.titleMedium)
                    if (!canCalculate) {
                        Text(
                            when {
                                age.isBlank() || weight.isBlank() -> "Enter patient data"
                                !isValidDrug -> "Select valid medication"
                                else -> "Fill all fields"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            }
            
            if (showResult && doseResult != null) {
                ProfessionalDoseDisplay(result = doseResult!!)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
