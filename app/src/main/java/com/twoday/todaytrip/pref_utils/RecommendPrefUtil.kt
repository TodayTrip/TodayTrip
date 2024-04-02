package com.twoday.todaytrip.pref_utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.tourData.TourContentTypeId
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.tourData.TourItemJsonConverter

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
        prefs.edit()
            .putString(
                destinationKey,
                TourItemJsonConverter.toJson(recommendTourItem)
            )
            .apply()
    }

    private fun loadRecommendTourItem(destinationKey: String): TourItem? {
        val prefs = getRecommendTourItemPreferences()

        val json = prefs.getString(destinationKey, null)
        if ((json == null) || json.toString().isNullOrEmpty())
            return null

        return TourItemJsonConverter.fromJson(json)
    }
}
