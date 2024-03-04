package com.twoday.todaytrip.utils

import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.tourData.TourItem

object TourItemSharedPreferenceUtil {
    fun saveTouristAttractionList(touristAttractionList:List<TourItem>) =
        SharedPreferencesUtil.saveTourItemList(
            MyApplication.appContext!!,
            touristAttractionList,
            PrefConstants.TOURIST_ATTRACTION_LIST_KEY
        )
    fun saveRestaurantList(restaurantList:List<TourItem>) =
        SharedPreferencesUtil.saveTourItemList(
            MyApplication.appContext!!,
            restaurantList,
            PrefConstants.RESTAURANT_LIST_KEY
        )
    fun saveCafeList(cafeList: List<TourItem>) =
        SharedPreferencesUtil.saveTourItemList(
            MyApplication.appContext!!,
            cafeList,
            PrefConstants.CAFE_LIST_KEY
        )
    fun saveEventList(eventList:List<TourItem>) =
        SharedPreferencesUtil.saveTourItemList(
            MyApplication.appContext!!,
            eventList,
            PrefConstants.EVENT_LIST_KEY
        )
}