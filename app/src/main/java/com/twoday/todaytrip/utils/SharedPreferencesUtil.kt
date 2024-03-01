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
        Log.d("SaveDestination", "destination key: ${destinationKey}, saved value: $destination")
        editor.apply()
    }
    fun loadDestination(context: Context, destinationKey: String): String? {
        Log.d("LoadDestination", "destination key: ${destinationKey}, loaded value: ${getDestPreferences(context).getString(destinationKey, null)}")
        return getDestPreferences(context).getString(destinationKey, null)
    }

    fun saveTourItemList(context:Context, tourItemList:List<TourItem>, destinationKey: String){
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

    fun eraseTourItemList(context: Context){
        Log.d("eraseTourItemList", "erase all TourItem lists in Shared Preference")
        saveTourItemList(context, emptyList(),  PrefConstants.TOUR_INFO_TAB_LIST_KEY)
        saveTourItemList(context, emptyList(),  PrefConstants.RESTAURANT_TAB_LIST_KEY)
        saveTourItemList(context, emptyList(),  PrefConstants.CAFE_TAB_LIST_KEY)
        saveTourItemList(context, emptyList(),  PrefConstants.EVENT_TAB_LIST_KEY)
    }
}