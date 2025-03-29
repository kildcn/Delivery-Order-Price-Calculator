package org.example.distance.model

import kotlinx.serialization.Serializable

@Serializable
data class DistanceRange(val min: Double, val max: Double, val a: Double, val b: Double)
