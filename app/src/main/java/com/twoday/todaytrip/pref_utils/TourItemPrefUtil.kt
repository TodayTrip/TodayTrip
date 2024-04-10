package com.twoday.todaytrip.pref_utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.tourData.TourCategoryId3
import com.twoday.todaytrip.tourData.TourContentTypeId
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.tourData.TourItemJsonConverter
import kotlinx.serialization.json.Json

object TourItemPrefUtil {
    private val TAG = "TourItemPrefUtil"
    fun loadAllTourItemList(): List<TourItem> = mutableListOf<TourItem>().apply {
        addAll(loadTouristAttractionList())
        addAll(loadRestaurantList())
        addAll(loadCafeList())
        addAll(loadEventList())
    }

    fun addTourItem(tourItem: TourItem){
        when(tourItem) {
            is TourItem.Restaurant -> {
                if ((!tourItem.tourItemInfo.category3.isNullOrEmpty()) &&
                    (tourItem.tourItemInfo.category3 == TourCategoryId3.CAFE_AND_TEA.id)
                )
                    addCafe(tourItem)
                else
                    addRestaurant(tourItem)
            }
            is TourItem.EventPerformanceFestival -> {
                addEvent(tourItem)
            }
            else -> { // TouristDestination, CulturalFacilities, LeisureSports
                addTouristAttraction(tourItem)
            }
        }
    }

    fun loadTouristAttractionList() = loadTourItemList(PrefConstants.TOURIST_ATTRACTION_LIST_KEY)
    fun saveTouristAttractionList(touristAttractionList: List<TourItem>) = saveTourItemList(
        touristAttractionList,
        PrefConstants.TOURIST_ATTRACTION_LIST_KEY
    )
    fun saveMoreTouristAttractionList(moreTouristAttractionList: List<TourItem>) {
        val loadedTouristAttractionList = loadTouristAttractionList()
        val filteredTouristAttractionList = moreTouristAttractionList.filter { moreTourItem  ->
            loadedTouristAttractionList.find{
                it.getContentId() == moreTourItem.getContentId()
            } == null
        }
        val newTouristAttractionList = mutableListOf<TourItem>().apply {
            addAll(loadedTouristAttractionList)
            addAll(filteredTouristAttractionList)
        }

        saveTourItemList(
            newTouristAttractionList,
            PrefConstants.TOURIST_ATTRACTION_LIST_KEY
        )
    }
    private fun addTouristAttraction(touristAttraction: TourItem) =
        saveMoreTouristAttractionList(listOf(touristAttraction))

    fun loadRestaurantList() = loadTourItemList(PrefConstants.RESTAURANT_LIST_KEY)
    fun saveRestaurantList(restaurantList: List<TourItem>) = saveTourItemList(
        restaurantList,
        PrefConstants.RESTAURANT_LIST_KEY
    )
    fun saveMoreRestaurantList(moreRestaurantList: List<TourItem>) {
        val loadedRestaurantList = loadRestaurantList()
        val filteredRestaurantList = moreRestaurantList.filter { moreTourItem  ->
            loadedRestaurantList.find{
                it.getContentId() == moreTourItem.getContentId()
            } == null
        }
        val newRestaurantList = mutableListOf<TourItem>().apply {
            addAll(loadedRestaurantList)
            addAll(filteredRestaurantList)
        }

        saveTourItemList(
            newRestaurantList,
            PrefConstants.RESTAURANT_LIST_KEY
        )
    }
    private fun addRestaurant(restaurant:TourItem) = saveMoreRestaurantList(listOf(restaurant))

    fun loadCafeList() = loadTourItemList(PrefConstants.CAFE_LIST_KEY)
    fun saveCafeList(cafeList: List<TourItem>) = saveTourItemList(
        cafeList,
        PrefConstants.CAFE_LIST_KEY
    )
    fun saveMoreCafeList(moreCafeList: List<TourItem>) {
        val loadedCafeList = loadCafeList()
        val filteredCafeList = moreCafeList.filter { moreTourItem  ->
            loadedCafeList.find{
                it.getContentId() == moreTourItem.getContentId()
            } == null
        }
        val newCafeList = mutableListOf<TourItem>().apply {
            addAll(loadedCafeList)
            addAll(filteredCafeList)
        }

        saveTourItemList(
            newCafeList.distinct(),
            PrefConstants.CAFE_LIST_KEY
        )
    }
    private fun addCafe(cafe:TourItem) = saveMoreCafeList(listOf(cafe))

    fun loadEventList() = loadTourItemList(PrefConstants.EVENT_LIST_KEY)
    fun saveEventList(eventList: List<TourItem>) = saveTourItemList(
        eventList.filter{
            !(it as TourItem.EventPerformanceFestival).isEventPerformanceFestivalOver()
        },
        PrefConstants.EVENT_LIST_KEY
    )

    fun saveMoreEventList(moreEventList: List<TourItem>) {
        val loadedEventList = loadEventList()
        val filteredEventList = moreEventList.filter { moreTourItem  ->
            loadedEventList.find{
                it.getContentId() == moreTourItem.getContentId()
            } == null
        }.filter {
            !(it as TourItem.EventPerformanceFestival).isEventPerformanceFestivalOver()
        }
        val newEventList = mutableListOf<TourItem>().apply {
            addAll(loadedEventList)
            addAll(filteredEventList)
        }

        saveTourItemList(
            newEventList.distinct(),
            PrefConstants.EVENT_LIST_KEY
        )
    }
    private fun addEvent(event:TourItem) = saveMoreEventList(listOf(event))

    fun resetTourItemListPref() {
        Log.d(TAG, "resetTourItemListPref called")
        getTourItemListPreferences().edit().clear().commit()
    }

    private fun getTourItemListPreferences(): SharedPreferences =
        MyApplication.appContext!!.getSharedPreferences(
            PrefConstants.PREFERENCE_TOUR_ITEM_LIST_KEY,
            Context.MODE_PRIVATE
        )

    private fun saveTourItemList(tourItemList: List<TourItem>, destinationKey: String) {
        val prefs = getTourItemListPreferences()

        val serializedTourItemList = tourItemList.map {
            TourItemJsonConverter.toJson(it)
        }
        val json = Gson().toJson(serializedTourItemList)
        prefs.edit().putString(destinationKey, json).apply()
    }

    private fun loadTourItemList(destinationKey: String): List<TourItem> {
        val prefs = getTourItemListPreferences()

        val json = prefs.getString(destinationKey, null)
        if ((json == null) || (json.toString() == "[]"))
            return emptyList()

        val type = object : TypeToken<List<String>>() {}.type
        val serializedTourItemList: List<String> = Gson().fromJson(json, type)

        val tourItemList = mutableListOf<TourItem>()
        serializedTourItemList.forEach {
            TourItemJsonConverter.fromJson(it)?.let{tourItem ->
                tourItemList.add(tourItem)
            }
        }
        return tourItemList
    }
}