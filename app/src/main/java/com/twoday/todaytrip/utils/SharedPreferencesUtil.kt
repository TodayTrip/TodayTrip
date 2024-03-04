package com.twoday.todaytrip.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.twoday.todaytrip.tourData.TourItem

object SharedPreferencesUtil {
    private val TAG = "SharedPreferencesUtil"
    private fun getDestPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PrefConstants.PREFERENCE_DESTINATION_KEY, Context.MODE_PRIVATE)
    }

    fun saveDestination(context: Context, destination: String?, destinationKey: String) {
        val editor = getDestPreferences(context).edit()
        editor.putString(destinationKey, destination)
        Log.d(TAG, "SaveDestination) destination key: ${destinationKey}, saved value: $destination")
        editor.apply()
    }
    fun loadDestination(context: Context, destinationKey: String): String? {
        Log.d(TAG, "LoadDestination) destination key: ${destinationKey}, loaded value: ${getDestPreferences(context).getString(destinationKey, null)}")
        return getDestPreferences(context).getString(destinationKey, null)
    }

    fun saveTourItemList(context:Context, tourItemList:List<TourItem>, destinationKey: String){
        Log.d(TAG, "saveTourItemList) destination key: ${destinationKey}, list size: ${tourItemList.size}")
        val prefs = getDestPreferences(context)
        val json = Gson().toJson(tourItemList)
        prefs.edit().putString(destinationKey, json).apply()
    }
    fun loadTourItemList(context:Context, destinationKey: String):List<TourItem>{
        Log.d(TAG, "loadTourItemList) destination key: ${destinationKey}")
        val prefs = getDestPreferences(context)
        val json = prefs.getString(destinationKey, null)
        return if ((json != null) && (json.toString() != "[]")) {
            val type = object : TypeToken<List<TourItem>>() {}.type
            Gson().fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun resetTourItemList(context: Context){
        saveTourItemList(
            context,
            emptyList(),
            PrefConstants.TOURIST_ATTRACTION_LIST_KEY
        )
        saveTourItemList(
            context,
            emptyList(),
            PrefConstants.RESTAURANT_LIST_KEY
        )
        saveTourItemList(
            context,
            emptyList(),
            PrefConstants.CAFE_LIST_KEY
        )
        saveTourItemList(
            context,
            emptyList(),
            PrefConstants.EVENT_LIST_KEY
        )
    }
}