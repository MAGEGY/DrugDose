package com.mag5112005.drugdose.domain

import com.mag5112005.drugdose.data.models.DoseRecommendation
import com.mag5112005.drugdose.data.models.DrugData
import com.mag5112005.drugdose.data.models.PatientData
import kotlin.math.round

class DoseCalculator {

    fun calculateDose(
        patient: PatientData,
        drug: DrugData,
        route: String
    ): DoseRecommendation {
        var baseDose = drug.standardDose

        baseDose = applyAgeAdjustment(baseDose, patient.age)
        baseDose = applyWeightAdjustment(baseDose, patient.weight)

        if (drug.renalAdjustment) {
            baseDose = applyRenalAdjustment(baseDose, patient.renalFunction)
        }

        if (drug.hepaticAdjustment) {
            baseDose = applyHepaticAdjustment(baseDose, patient.hepaticFunction)
        }

        val precautions = mutableListOf<String>()
        val interactions = mutableListOf<String>()

        if (patient.isPregnant && drug.pregnancyCategory != "A") {
            precautions.add("Pregnancy Category: ${drug.pregnancyCategory}")
        }

        if (patient.isLactating) {
            precautions.add("Caution in lactating patients")
        }

        precautions.addAll(drug.warnings)
        interactions.addAll(drug.interactions)

        val maxDose = applyMaxDoseLimit(baseDose, drug.maxDailyDose)

        return DoseRecommendation(
            drugName = drug.name,
            dose = round(maxDose * 100) / 100,
            doseUnit = drug.doseUnit,
            frequency = drug.frequency,
            route = route,
            duration = "As per clinical indication",
            maxDailyDose = drug.maxDailyDose,
            precautions = precautions,
            interactions = interactions
        )
    }

    private fun applyAgeAdjustment(baseDose: Double, age: Int): Double {
        return when {
            age < 2 -> baseDose * 0.25
            age < 6 -> baseDose * 0.5
            age < 12 -> baseDose * 0.75
            age >= 65 -> baseDose * 0.85
            else -> baseDose
        }
    }

    private fun applyWeightAdjustment(baseDose: Double, weight: Double): Double {
        return if (weight < 50) baseDose * (weight / 70) else baseDose
    }

    private fun applyRenalAdjustment(baseDose: Double, renalFunction: String): Double {
        return when (renalFunction) {
            "Normal" -> baseDose
            "Mild Impairment" -> baseDose * 0.9
            "Moderate Impairment" -> baseDose * 0.75
            "Severe Impairment" -> baseDose * 0.5
            else -> baseDose
        }
    }

    private fun applyHepaticAdjustment(baseDose: Double, hepaticFunction: String): Double {
        return when (hepaticFunction) {
            "Normal" -> baseDose
            "Mild Impairment" -> baseDose * 0.9
            "Moderate Impairment" -> baseDose * 0.75
            "Severe Impairment" -> baseDose * 0.5
            else -> baseDose
        }
    }

    private fun applyMaxDoseLimit(calculatedDose: Double, maxDailyDose: Double): Double {
        return minOf(calculatedDose, maxDailyDose)
    }

    fun checkInteractions(
        selectedDrug: DrugData,
        concomitantMedications: List<String>
    ): List<String> {
        val interactions = mutableListOf<String>()

        concomitantMedications.forEach { med ->
            if (selectedDrug.interactions.any { it.contains(med, ignoreCase = true) }) {
                interactions.add("Potential interaction with $med")
            }
        }

        return interactions
    }
}
