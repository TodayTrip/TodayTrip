package com.twoday.todaytrip.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.tourData.TourContentTypeId
import com.twoday.todaytrip.tourData.TourItem

object RecommendPrefUtil {
    private val TAG = "RecommendUtil"

    fun loadRecommendTouristAttraction(): TourItem? =
        loadRecommendTourItem(PrefConstants.RECOMMEND_TOURIST_ATTRACTION_KEY)

    fun saveRecommendTouristAttraction(recommendTouristAttraction: TourItem) =
        saveRecommendTourItem(
            recommendTouristAttraction,
            PrefConstants.RECOMMEND_TOURIST_ATTRACTION_KEY
        )

    fun loadRecommendRestaurant(): TourItem? =
        loadRecommendTourItem(PrefConstants.RECOMMEND_RESTAURANT_KEY)

    fun saveRecommendRestaurant(recommendRestaurant: TourItem) =
        saveRecommendTourItem(
            recommendRestaurant,
            PrefConstants.RECOMMEND_RESTAURANT_KEY
        )

    fun loadRecommendCafe(): TourItem? =
        loadRecommendTourItem(PrefConstants.RECOMMEND_CAFE_KEY)

    fun saveRecommendCafe(recommendCafe: TourItem) =
        saveRecommendTourItem(
            recommendCafe,
            PrefConstants.RECOMMEND_CAFE_KEY
        )

    fun loadRecommendEvent(): TourItem? =
        loadRecommendTourItem(PrefConstants.RECOMMEND_EVENT_KEY)

    fun saveEventTouristAttraction(recommendEvent: TourItem) =
        saveRecommendTourItem(
            recommendEvent,
            PrefConstants.RECOMMEND_EVENT_KEY
        )

    fun resetRecommendTourItemPref() {
        Log.d(TAG, "resetRecommendTourItemPref) called")

        val pref = getRecommendTourItemPreferences()
        pref.edit().clear().commit()
    }

    private fun getRecommendTourItemPreferences(): SharedPreferences =
        MyApplication.appContext!!.getSharedPreferences(
            PrefConstants.PREFERENCE_RECOMMEND_TOUR_ITEM_KEY,
            Context.MODE_PRIVATE
        )

    private fun saveRecommendTourItem(recommendTourItem: TourItem, destinationKey: String) {
        val prefs = getRecommendTourItemPreferences()

        // (1) TourItem JSON 직렬화 -> Pair에 담기
        val serializedTourItem = (
                recommendTourItem.getContentTypeId() to Gson().toJson(recommendTourItem)
                ) as Pair<String, String>

        // (2) Pair 직렬화
        val json = Gson().toJson(serializedTourItem)
        prefs.edit().putString(destinationKey, json).apply()
    }

    private fun loadRecommendTourItem(destinationKey: String): TourItem? {
        val prefs = getRecommendTourItemPreferences()

        val json = prefs.getString(destinationKey, null)
        if ((json == null) || json.toString().isNullOrEmpty())
            return null

        // (1) Pair 역직렬화
        val type = object : TypeToken<Pair<String, String>>() {}.type
        val serializedTourItem: Pair<String, String> = Gson().fromJson(json, type)

        // (2) Pair에서 꺼내기 -> TourItem JSON 역직렬화
        val tourItemType = when (serializedTourItem.first) {
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
        return Gson().fromJson(serializedTourItem.second, tourItemType)
    }
}
