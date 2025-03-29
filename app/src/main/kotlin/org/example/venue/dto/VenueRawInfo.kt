package org.example.venue.dto

import kotlinx.serialization.Serializable
import org.example.venue.model.VenueLocation

@Serializable
data class VenueRawInfo(
    val id: String,
    val name: String,
    val location: VenueLocation
)
