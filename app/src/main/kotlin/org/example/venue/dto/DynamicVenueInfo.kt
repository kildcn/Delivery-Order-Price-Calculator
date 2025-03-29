package org.example.venue.dto

import kotlinx.serialization.Serializable
import org.example.distance.model.DistanceRange

@Serializable
data class DynamicVenueInfo(
    val maxDeliveryDistance: Double,
    val orderMinimumNoSurcharge: Double,
    val basePrice: Double,
    val distanceRanges: List<DistanceRange>
)
