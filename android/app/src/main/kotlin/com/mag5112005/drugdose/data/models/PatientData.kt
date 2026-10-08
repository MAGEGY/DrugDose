package com.mag5112005.drugdose.data.models

data class PatientData(
    val id: String = "",
    val age: Int = 0,
    val weight: Double = 0.0,
    val height: Double = 0.0,
    val gender: String = "",
    val renalFunction: String = "Normal",
    val hepaticFunction: String = "Normal",
    val isPregnant: Boolean = false,
    val isLactating: Boolean = false,
    val concomitantMedications: List<String> = emptyList()
) {
    fun calculateBSA(): Double {
        return kotlin.math.sqrt((height * weight) / 3600.0)
    }

    fun isChild(): Boolean = age < 18
    fun isElderly(): Boolean = age >= 65
    fun isAdult(): Boolean = age in 18..64
}
