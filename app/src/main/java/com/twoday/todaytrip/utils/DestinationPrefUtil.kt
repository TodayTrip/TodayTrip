package com.twoday.todaytrip.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.twoday.todaytrip.MyApplication

object DestinationPrefUtil {
    private val TAG = "DestinationPrefUtil"

    fun loadTheme(): String = loadDestination(PrefConstants.THEME_KEY) ?: ""
    fun saveTheme(theme:String) = saveDestination(theme, PrefConstants.THEME_KEY)

    fun loadDestination(): String = loadDestination(PrefConstants.DESTINATION_KEY) ?: ""
    fun saveDestination(destination:String) = saveDestination(destination, PrefConstants.DESTINATION_KEY)

    private fun getDestPreferences(): SharedPreferences =
        MyApplication.appContext!!.getSharedPreferences(
            PrefConstants.PREFERENCE_DESTINATION_KEY,
            Context.MODE_PRIVATE
        )
    private fun saveDestination(destination: String?, destinationKey: String) {
        val editor = getDestPreferences().edit()
        editor.putString(destinationKey, destination)
        Log.d(TAG, "SaveDestination) destination key: ${destinationKey}, saved value: $destination")
        editor.apply()
    }
    private fun loadDestination(destinationKey: String): String? {
        Log.d(TAG, "LoadDestination) destination key: ${destinationKey}, loaded value: ${getDestPreferences().getString(destinationKey, null)}")
        return getDestPreferences().getString(destinationKey, null)
    }

    fun resetDestinationPref() {
        getDestPreferences().edit().clear().commit()
    }
}