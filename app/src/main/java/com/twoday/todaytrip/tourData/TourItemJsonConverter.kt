package com.twoday.todaytrip.tourData

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object TourItemJsonConverter {
    fun toJson(tourItem: TourItem): String {
        val serializedPair = tourItem.getContentTypeId() to Gson().toJson(tourItem)
        return Gson().toJson(serializedPair)
    }

    fun fromJson(json: String): TourItem {
        val type = object : TypeToken<Pair<String, String>>() {}.type
        val serializedPair: Pair<String, String> = Gson().fromJson(json, type)

        val tourItemType = when (serializedPair.first) {
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
        return Gson().fromJson(serializedPair.second, tourItemType)
    }
}