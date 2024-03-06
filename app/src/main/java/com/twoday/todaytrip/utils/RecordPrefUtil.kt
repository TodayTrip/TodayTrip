package com.twoday.todaytrip.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.recordData.Record
import com.twoday.todaytrip.tourData.TourContentTypeId
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.ui.route.SavePhotoData
import kotlinx.serialization.json.Json

object RecordPrefUtil {
    private val TAG = "RecordPrefUtil"

    fun loadRecordList() = RecordPrefUtil.loadRecordList(PrefConstants.RECORD_LIST_KEY)
    fun saveRecordList(recordList: List<Record>) = RecordPrefUtil.saveRecordList(
        recordList,
        PrefConstants.RECORD_LIST_KEY
    )

    fun addRecord(record: Record) = RecordPrefUtil.saveRecordList(
        loadRecordList().toMutableList().apply {
            add(record)
        },
        PrefConstants.RECORD_LIST_KEY
    )

    private fun getRecordListPreferences(): SharedPreferences =
        MyApplication.appContext!!.getSharedPreferences(
            PrefConstants.PREFERENCE_RECORD_LIST_KEY,
            Context.MODE_PRIVATE
        )

    private fun saveRecordList(recordList: List<Record>, destinationKey: String) {
        Log.d(TAG, "saveTourItemList) recordList size = ${recordList.size}")
        val prefs = getRecordListPreferences()

        // (1) SavePhotoData JSON 직렬화 -> Triple에 담기
        val serializedRecordList = recordList.map { record ->
            record.savePhotoDataList.map {
                Triple(it.tourItem.getContentTypeId(), it.imageUrl, Gson().toJson(it.tourItem))
            }
        }
        // (2) List<List<Triple>> 직렬화
        val json = Gson().toJson(serializedRecordList)
        prefs.edit().putString(destinationKey, json).apply()
    }

    private fun loadRecordList(destinationKey: String): List<Record> {
        Log.d(TAG, "loadRecordList) destination key: ${destinationKey}")

        val prefs = getRecordListPreferences()

        val json = prefs.getString(destinationKey, null)
        if ((json == null) || (json.toString() == "[]"))
            return emptyList()

        // (1) List<List<Triple>> 역직렬화
        val type = object : TypeToken<List<List<Triple<String, String, Json>>>>() {}.type
        val serializedRecordList: List<List<Triple<String, String, Json>>> =
            Gson().fromJson(json, type)

        // (2) Triple에서 꺼내기 -> SavePhotoData JSON 역직렬화
        val recordList = mutableListOf<Record>()
        serializedRecordList.forEach { serializedRecord ->
            val record = mutableListOf<SavePhotoData>()

            serializedRecord.forEach {
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

                record.add(
                    SavePhotoData(
                        Gson().fromJson(it.third as String, tourItemType),
                        it.second
                    )
                )
            }
            recordList.add(Record(record))
        }
        return recordList
    }
}