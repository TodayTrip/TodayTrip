package com.twoday.todaytrip.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.tourData.TourContentTypeId
import com.twoday.todaytrip.tourData.TourItem
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement

object TourItemPrefUtil {
    private val TAG = "TourItemPrefUtil"
    fun loadAllTourItemList():List<TourItem> = mutableListOf<TourItem>().apply{
            addAll(loadTouristAttractionList())
            addAll(loadRestaurantList())
            addAll(loadCafeList())
            addAll(loadEventList())
        }

    fun loadTouristAttractionList() = loadTourItemList(PrefConstants.TOURIST_ATTRACTION_LIST_KEY)
    fun saveTouristAttractionList(touristAttractionList:List<TourItem>) = saveTourItemList(
            touristAttractionList,
            PrefConstants.TOURIST_ATTRACTION_LIST_KEY
        )
    fun saveMoreTouristAttractionList(moreTouristAttractionList:List<TourItem>){
        val newTouristAttractionList = mutableListOf<TourItem>()
        newTouristAttractionList.addAll(loadTouristAttractionList())
        newTouristAttractionList.addAll(moreTouristAttractionList)

        saveTourItemList(
            newTouristAttractionList,
            PrefConstants.TOURIST_ATTRACTION_LIST_KEY
        )
    }

    fun loadRestaurantList() = loadTourItemList(PrefConstants.RESTAURANT_LIST_KEY)
    fun saveRestaurantList(restaurantList:List<TourItem>) = saveTourItemList(
            restaurantList,
            PrefConstants.RESTAURANT_LIST_KEY
        )
    fun saveMoreRestaurantList(moreRestaurantList:List<TourItem>){
        val newRestaurantList = mutableListOf<TourItem>()
        newRestaurantList.addAll(loadRestaurantList())
        newRestaurantList.addAll(moreRestaurantList)

        saveTourItemList(
            newRestaurantList,
            PrefConstants.RESTAURANT_LIST_KEY
        )
    }

    fun loadCafeList() = loadTourItemList(PrefConstants.CAFE_LIST_KEY)
    fun saveCafeList(cafeList: List<TourItem>) = saveTourItemList(
            cafeList,
            PrefConstants.CAFE_LIST_KEY
        )
    fun saveMoreCafeList(moreCafeList:List<TourItem>){
        val newCafeList = mutableListOf<TourItem>()
        newCafeList.addAll(loadCafeList())
        newCafeList.addAll(moreCafeList)

        saveTourItemList(
            newCafeList,
            PrefConstants.CAFE_LIST_KEY
        )
    }

    fun loadEventList() = loadTourItemList(PrefConstants.EVENT_LIST_KEY)
    fun saveEventList(eventList:List<TourItem>) = saveTourItemList(
            eventList,
            PrefConstants.EVENT_LIST_KEY
        )
    fun saveMoreEventList(moreEventList:List<TourItem>){
        val newEventList = mutableListOf<TourItem>()
        newEventList.addAll(loadEventList())
        newEventList.addAll(moreEventList)

        saveTourItemList(
            newEventList,
            PrefConstants.EVENT_LIST_KEY
        )
    }

    fun resetTourItemListPref(){
        Log.d(TAG, "resetTourItemListPref called")
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

        // (1) TourItem JSON 직렬화 -> Pair에 담기
        val serializedTourItemList = tourItemList.map {
            it.getContentTypeId() to Gson().toJson(it)
        }
        // (2) List<Pair> 직렬화
        val json = Gson().toJson(serializedTourItemList)
        prefs.edit().putString(destinationKey, json).apply()
    }
    private fun loadTourItemList(destinationKey: String):List<TourItem>{
        Log.d(TAG, "loadTourItemList) destination key: ${destinationKey}")

        val prefs = getTourItemListPreferences()

        val json = prefs.getString(destinationKey, null)
        if((json == null) || (json.toString() == "[]"))
            return emptyList()

        // (1) List<Pair> 역직렬화
        val type = object : TypeToken<List<Pair<String, String>>>() {}.type
        val serializedTourItemList:List<Pair<String, String>> = Gson().fromJson(json, type)

        // (2) Pair에서 꺼내기 -> TourItem JSON 역직렬화
        val tourItemList = mutableListOf<TourItem>()
        serializedTourItemList.forEach{
            val tourItemType = when(it.first){
                TourContentTypeId.TOURIST_DESTINATION.contentTypeId -> {
                    object : TypeToken<TourItem.TouristDestination>() {}.type
                }
                TourContentTypeId.CULTURAL_FACILITIES.contentTypeId ->{
                    object : TypeToken<TourItem.CulturalFacilities>() {}.type
                }
                TourContentTypeId.RESTAURANT.contentTypeId ->{
                    object : TypeToken<TourItem.Restaurant>() {}.type
                }
                TourContentTypeId.LEISURE_SPORTS.contentTypeId ->{
                    object : TypeToken<TourItem.LeisureSports>() {}.type
                }
                TourContentTypeId.EVENT_PERFORMANCE_FESTIVAL.contentTypeId ->{
                    object : TypeToken<TourItem.EventPerformanceFestival>() {}.type
                }
                else -> {
                    object : TypeToken<TourItem.TouristDestination>() {}.type
                }
            }
            tourItemList.add(Gson().fromJson(it.second, tourItemType))
        }
        return tourItemList
    }
}