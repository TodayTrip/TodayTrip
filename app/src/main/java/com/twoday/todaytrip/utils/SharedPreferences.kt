package com.twoday.todaytrip.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object SharedPreferencesUtil {
    private fun getDestPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.PREFERENCE_DESTINATION_KEY, Context.MODE_PRIVATE)
    }

    fun saveDestination(context: Context, destination: String, destinationKey: String) {
        val editor = getDestPreferences(context).edit()
        editor.putString(destinationKey, destination)
        Log.d("SaveDestination", "Destination saved: $destination")
        editor.apply()
    }

    fun loadDestination(context: Context, destinationKey: String): String? {
        Log.d("LoadDestination", "Loaded destination: ${getDestPreferences(context).getString(destinationKey, null)}")
        return getDestPreferences(context).getString(destinationKey, null)
    }
}