package org.example.delivery.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import org.example.distance.dto.DistanceRangeResponse

@Serializable
data class DeliveryPricing(
    @SerialName("base_price")
    val basePrice: Double,
    @SerialName("distance_ranges")
    val distanceRanges: List<DistanceRangeResponse>
)
