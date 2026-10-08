package com.mag5112005.drugdose.data.api

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// FDA API Response Models
data class FDADrugResponse(
    @SerializedName("results") val results: List<FDADrug>?
)

data class FDADrug(
    @SerializedName("openfda") val openFda: OpenFDA?,
    @SerializedName("products") val products: List<FDAProduct>?,
    @SerializedName("purpose") val purpose: List<String>?,
    @SerializedName("indications_and_usage") val indicationsAndUsage: List<String>?,
    @SerializedName("warnings") val warnings: List<String>?,
    @SerializedName("dosage_and_administration") val dosageAndAdministration: List<String>?,
    @SerializedName("drug_interactions") val drugInteractions: List<String>?,
    @SerializedName("contraindications") val contraindications: List<String>?
)

data class OpenFDA(
    @SerializedName("generic_name") val genericName: List<String>?,
    @SerializedName("brand_name") val brandName: List<String>?,
    @SerializedName("route") val route: List<String>?,
    @SerializedName("substance_name") val substanceName: List<String>?,
    @SerializedName("product_type") val productType: List<String>?,
    @SerializedName("pharm_class_epc") val pharmClass: List<String>?
)

data class FDAProduct(
    @SerializedName("product_type") val productType: String?,
    @SerializedName("route") val route: String?,
    @SerializedName("dosage_form") val dosageForm: String?,
    @SerializedName("active_ingredients") val activeIngredients: List<FDAIngredient>?
)

data class FDAIngredient(
    @SerializedName("name") val name: String?,
    @SerializedName("strength") val strength: String?
)

// Retrofit API Interface
interface FDAApiService {
    @GET("drug/label.json")
    suspend fun searchDrug(
        @Query("search") query: String,
        @Query("limit") limit: Int = 10
    ): FDADrugResponse

    companion object {
        private const val BASE_URL = "https://api.fda.gov/"

        fun create(): FDAApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(FDAApiService::class.java)
        }
    }
}
