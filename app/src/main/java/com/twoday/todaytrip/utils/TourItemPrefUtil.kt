package com.twoday.todaytrip.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.tourData.TourItem

object TourItemPrefUtil {
    private val TAG = "TourItemPrefUtil"

    fun loadTouristAttractionList() = loadTourItemList(PrefConstants.TOURIST_ATTRACTION_LIST_KEY)
    fun saveTouristAttractionList(touristAttractionList:List<TourItem>) = saveTourItemList(
            touristAttractionList,
            PrefConstants.TOURIST_ATTRACTION_LIST_KEY
        )

    fun loadRestaurantList() = loadTourItemList(PrefConstants.RESTAURANT_LIST_KEY)
    fun saveRestaurantList(restaurantList:List<TourItem>) = saveTourItemList(
            restaurantList,
            PrefConstants.RESTAURANT_LIST_KEY
        )

    fun loadCafeList() = loadTourItemList(PrefConstants.CAFE_LIST_KEY)
    fun saveCafeList(cafeList: List<TourItem>) = saveTourItemList(
            cafeList,
            PrefConstants.CAFE_LIST_KEY
        )

    fun loadEventList() = loadTourItemList(PrefConstants.EVENT_LIST_KEY)
    fun saveEventList(eventList:List<TourItem>) = saveTourItemList(
            eventList,
            PrefConstants.EVENT_LIST_KEY
        )

    fun resetTourItemListPref(){
        Log.d(TAG, "resetTourItemListPref) called")
        saveTouristAttractionList(emptyList())
        saveRestaurantList(emptyList())
        saveCafeList(emptyList())
        saveEventList(emptyList())
    }

    private fun getTourItemListPreferences(): SharedPreferences =
        MyApplication.appContext!!.getSharedPreferences(
            PrefConstants.PREFERENCE_TOUR_ITEM_LIST_KEY,
            Context.MODE_PRIVATE
        )
    private fun saveTourItemList(tourItemList:List<TourItem>, destinationKey: String){
        Log.d(TAG, "saveTourItemList) destination key: ${destinationKey}, list size: ${tourItemList.size}")
        val prefs = getTourItemListPreferences()
        val json = Gson().toJson(tourItemList)
        prefs.edit().putString(destinationKey, json).apply()
    }
    fun loadTourItemList(destinationKey: String):List<TourItem>{
        Log.d(TAG, "loadTourItemList) destination key: ${destinationKey}")
        val prefs = getTourItemListPreferences()
        val json = prefs.getString(destinationKey, null)
        return if ((json != null) && (json.toString() != "[]")) {
            val type = object : TypeToken<List<TourItem>>() {}.type
            Gson().fromJson(json, type)
        } else {
            emptyList()
        }
    }
}