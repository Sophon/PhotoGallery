package com.bignerdranch.android.photogallery.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

private const val PREF_SEARCH_QUERY = "query"

object QueryPreferences {

    fun getStoredQuery(context: Context): String {
        val preference: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

        return preference.getString(PREF_SEARCH_QUERY, "")!!
    }

    fun setStoredQuery(context: Context, query: String) {
        PreferenceManager
            .getDefaultSharedPreferences(context)
            .edit()
            .putString(PREF_SEARCH_QUERY, query)
            .apply()
    }
}