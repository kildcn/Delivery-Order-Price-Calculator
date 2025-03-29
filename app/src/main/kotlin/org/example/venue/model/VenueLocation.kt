package org.example.venue.model

import kotlinx.serialization.Serializable

@Serializable
data class VenueLocation(val coordinates: List<Double>)
