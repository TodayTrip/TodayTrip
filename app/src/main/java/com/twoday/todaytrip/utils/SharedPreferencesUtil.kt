package com.twoday.todaytrip.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.twoday.todaytrip.tourData.TourItem

object SharedPreferencesUtil {
    private fun getDestPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PrefConstants.PREFERENCE_DESTINATION_KEY, Context.MODE_PRIVATE)
    }

    fun saveDestination(context: Context, destination: String?, destinationKey: String) {
        val editor = getDestPreferences(context).edit()
        editor.putString(destinationKey, destination)
        Log.d("SaveDestination", "Destination saved: $destination")
        editor.apply()
    }
    fun loadDestination(context: Context, destinationKey: String): String? {
        Log.d("LoadDestination", "Loaded destination: ${getDestPreferences(context).getString(destinationKey, null)}")
        return getDestPreferences(context).getString(destinationKey, null)
    }

    fun saveTourItemList(context:Context, tourItemList:List<TourItem>, destinationKey: String){
        if(tourItemList == emptyList<TourItem>()){
            Log.d("saveTourItemList", "cannot save empty list!")
            return
        }

        val prefs = getDestPreferences(context)
        val json = Gson().toJson(tourItemList)
        prefs.edit().putString(destinationKey, json).apply()
    }
    fun loadTourItemList(context:Context, destinationKey: String):List<TourItem>{
        val prefs = getDestPreferences(context)
        val json = prefs.getString(destinationKey, null)

        return if (json != null) {
            val type = object : TypeToken<List<TourItem>>() {}.type
            Gson().fromJson(json, type)
        } else {
            emptyList()
        }
    }
}