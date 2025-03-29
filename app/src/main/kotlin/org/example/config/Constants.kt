package org.example.config

object Constants {
    object Api {
        const val BASE_URL = "https://consumer-api.development.dev.woltapi.com/home-assignment-api/v1"
        const val STATIC_VENUE_PATH = "$BASE_URL/venues/%s/static"
        const val DYNAMIC_VENUE_PATH = "$BASE_URL/venues/%s/dynamic"
    }

    object Delivery {
        const val EARTH_RADIUS_METERS = 6371e3
        const val MIN_LATITUDE = -90.0
        const val MAX_LATITUDE = 90.0
        const val MIN_LONGITUDE = -180.0
        const val MAX_LONGITUDE = 180.0
    }
}
