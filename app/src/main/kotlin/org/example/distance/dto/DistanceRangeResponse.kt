package org.example.distance.dto

import kotlinx.serialization.Serializable

@Serializable
data class DistanceRangeResponse(
    val min: Double,
    val max: Double,
    val a: Double,
    val b: Double
)
