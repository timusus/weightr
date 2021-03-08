package com.acompany.data.model

data class Exercise(
    val name: String,
    val sessionId: Int,
    val sets: Int,
    val reps: Int,
    val percentageOneRepMax: Double?,
)