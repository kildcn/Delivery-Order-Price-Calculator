package org.example.error

object ErrorMessages {
    const val MISSING_VENUE_SLUG = "Required parameter 'venue_slug' is missing"
    const val INVALID_CART_VALUE = "Invalid cart value. Must be a positive integer"
    const val INVALID_USER_LAT = "Invalid latitude. Must be between -90 and 90 degrees"
    const val INVALID_USER_LON = "Invalid longitude. Must be between -180 and 180 degrees"
    const val DELIVERY_NOT_POSSIBLE = "Delivery is not possible for this distance."
    const val VENUE_NOT_FOUND = "Venue not found or invalid"
    const val INTERNAL_SERVER_ERROR = "An unexpected error occurred"
}
