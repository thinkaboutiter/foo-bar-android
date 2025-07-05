package com.cool.element.foobar.utils

object Constants {
    
    // Network Configuration
    object Network {
        const val BASE_URL = "https://gist.githubusercontent.com/"
        const val CONTENT_TYPE_JSON = "application/json"
        const val API_ENDPOINT = "thinkaboutiter/b10cd61de50e7451e0f17f1e73780d1e/raw/2fdfdf76ae8c849ca8a7cf04faaa11c832d10a01/gistfile1.txt/"
    }
    
    // Database Configuration
    object Database {
        const val NAME = "foobar_database"
    }
    
    // Mock Data Generation
    object MockData {
        const val CAR_TITLE_PREFIX = "DB Car"
        const val CAR_URL_PREFIX = "http"
        const val CAR_YEAR_BASE = 2020
        const val CAR_PRICE_BASE = 4000
        const val MOCK_CARS_COUNT = 10
    }
    
    // UI Strings
    object UI {
        const val TAB_LOCAL_CARS = "Local Cars"
        const val TAB_NETWORK_CARS = "Network Cars"
        const val ERROR_PREFIX = "Error: "
        
        // Log Tags
        const val LOG_TAG_UI = "UI"
    }
}
