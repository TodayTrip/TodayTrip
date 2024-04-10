package com.twoday.todaytrip.tourData

import android.util.Log
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object TourItemJsonConverter {
    private val TAG = "TourItemJsonConverter"

    private val moshi = Moshi.Builder()
        .add(
            PolymorphicJsonAdapterFactory.of(TourItem::class.java, "tour_item_type")
                .withSubtype(TourItem.TouristDestination::class.java, "tourist_attraction")
                .withSubtype(TourItem.CulturalFacilities::class.java, "cultural_facilities")
                .withSubtype(TourItem.Restaurant::class.java, "restaurant")
                .withSubtype(TourItem.LeisureSports::class.java, "leisure_sports")
                .withSubtype(TourItem.EventPerformanceFestival::class.java, "event_performance_festival")
        )
        .add(KotlinJsonAdapterFactory())
        .build()

    fun toJson(tourItem: TourItem): String {
        Log.d(TAG, "toJson, TourItem:${tourItem.getTitle()}")
        return try{
            moshi.adapter(TourItem::class.java).toJson(tourItem)
        }catch (e:JsonDataException){
            e.printStackTrace()
            ""
        }
    }

    fun fromJson(json: String): TourItem? {
        Log.d(TAG, "fromJson, json: ${json}")
        return try {
            moshi.adapter(TourItem::class.java).fromJson(json)
        }catch (e:JsonDataException){
            e.printStackTrace()
            null
        }
    }
}