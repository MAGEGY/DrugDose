package com.mag5112005.drugdose.data.models

data class DrugData(
    val id: String,
    val name: String,
    val category: String,
    val routes: List<String>,
    val standardDose: Double,
    val doseUnit: String,
    val frequency: String,
    val maxDailyDose: Double,
    val minAge: Int = 0,
    val maxAge: Int = 120,
    val renalAdjustment: Boolean = false,
    val hepaticAdjustment: Boolean = false,
    val pregnancyCategory: String = "Not Specified",
    val contraindications: List<String> = emptyList(),
    val warnings: List<String> = emptyList(),
    val interactions: List<String> = emptyList()
)

data class DoseRecommendation(
    val drugName: String,
    val dose: Double,
    val doseUnit: String,
    val frequency: String,
    val route: String,
    val duration: String,
    val maxDailyDose: Double,
    val precautions: List<String>,
    val interactions: List<String>,
    val confidence: Float = 0.95f
)
