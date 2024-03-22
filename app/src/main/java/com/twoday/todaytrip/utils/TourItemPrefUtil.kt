package com.twoday.todaytrip.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.tourData.TourCategoryId3
import com.twoday.todaytrip.tourData.TourContentTypeId
import com.twoday.todaytrip.tourData.TourItem

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
        eventList,
        PrefConstants.EVENT_LIST_KEY
    )

    fun saveMoreEventList(moreEventList: List<TourItem>) {
        val loadedEventList = loadEventList()
        val filteredEventList = moreEventList.filter { moreTourItem  ->
            loadedEventList.find{
                it.getContentId() == moreTourItem.getContentId()
            } == null
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

    private fun saveTourItemList(tourItemList: List<TourItem>, destinationKey: String) {
        Log.d(
            TAG,
            "saveTourItemList) destination key: ${destinationKey}, list size: ${tourItemList.size}"
        )

        val prefs = getTourItemListPreferences()

        // (1) TourItem JSON 직렬화 -> Pair에 담기
        val serializedTourItemList = tourItemList.map {
            it.getContentTypeId() to Gson().toJson(it)
        }
        // (2) List<Pair> 직렬화
        val json = Gson().toJson(serializedTourItemList)
        prefs.edit().putString(destinationKey, json).apply()
    }

    private fun loadTourItemList(destinationKey: String): List<TourItem> {
        Log.d(TAG, "loadTourItemList) destination key: ${destinationKey}")

        val prefs = getTourItemListPreferences()

        val json = prefs.getString(destinationKey, null)
        if ((json == null) || (json.toString() == "[]"))
            return emptyList()

        // (1) List<Pair> 역직렬화
        val type = object : TypeToken<List<Pair<String, String>>>() {}.type
        val serializedTourItemList: List<Pair<String, String>> = Gson().fromJson(json, type)

        // (2) Pair에서 꺼내기 -> TourItem JSON 역직렬화
        val tourItemList = mutableListOf<TourItem>()
        serializedTourItemList.forEach {
            val tourItemType = when (it.first) {
                TourContentTypeId.TOURIST_DESTINATION.contentTypeId -> {
                    object : TypeToken<TourItem.TouristDestination>() {}.type
                }

                TourContentTypeId.CULTURAL_FACILITIES.contentTypeId -> {
                    object : TypeToken<TourItem.CulturalFacilities>() {}.type
                }

                TourContentTypeId.RESTAURANT.contentTypeId -> {
                    object : TypeToken<TourItem.Restaurant>() {}.type
                }

                TourContentTypeId.LEISURE_SPORTS.contentTypeId -> {
                    object : TypeToken<TourItem.LeisureSports>() {}.type
                }

                TourContentTypeId.EVENT_PERFORMANCE_FESTIVAL.contentTypeId -> {
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