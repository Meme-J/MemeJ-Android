package com.example.memej.Utils

import android.content.SearchRecentSuggestionsProvider


/**
 * Provider Class
 */
class MySuggestionProvider : SearchRecentSuggestionsProvider() {


    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "com.example.MySuggestionProvider"
        const val MODE: Int = SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES
    }
}