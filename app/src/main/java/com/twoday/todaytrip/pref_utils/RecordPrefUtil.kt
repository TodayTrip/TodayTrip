package com.twoday.todaytrip.pref_utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.tourData.TourContentTypeId
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.ui.record.Record
import com.twoday.todaytrip.ui.save_photo.SavePhotoData
import kotlinx.serialization.Serializable

object RecordPrefUtil {
    private val TAG = "RecordPrefUtil"

    fun loadRecordList() = loadRecordList(PrefConstants.RECORD_LIST_KEY)
    fun addRecord(record: Record) = saveRecordList(
        loadRecordList().toMutableList().apply {
            add(record)
        },
        PrefConstants.RECORD_LIST_KEY
    )

    fun deleteRecord(record: Record) {
        Log.d(TAG, "deleteRecord) recordId: ${record.recordId}")
        saveRecordList(
            loadRecordList().filter { it.recordId != record.recordId },
            PrefConstants.RECORD_LIST_KEY
        )
    }

    private fun getRecordListPreferences(): SharedPreferences =
        MyApplication.appContext!!.getSharedPreferences(
            PrefConstants.PREFERENCE_RECORD_LIST_KEY,
            Context.MODE_PRIVATE
        )

    private fun saveRecordList(recordList: List<Record>, destinationKey: String) {
        Log.d(TAG, "saveTourItemList) recordList size = ${recordList.size}")
        val prefs = getRecordListPreferences()

        val serializedRecordList: MutableList<SerializedRecord> = mutableListOf()
        recordList.forEach { record ->
            serializedRecordList.add(
                SerializedRecord(
                    record.recordId,
                    record.destination,
                    record.travelDate,
                    record.savePhotoDataList.map { savePhotoData ->
                        // SavePhotoData JSON 직렬화 -> Triple에 저장하기
                        Triple(
                            savePhotoData.tourItem.getContentTypeId(),
                            savePhotoData. imageUriList,
                            Gson().toJson(savePhotoData.tourItem)
                        )
                    }
                )
            )
        }
        val json = Gson().toJson(serializedRecordList)
        prefs.edit().putString(destinationKey, json).apply()
    }

    private fun loadRecordList(destinationKey: String): List<Record> {
        Log.d(TAG, "loadRecordList) destination key: ${destinationKey}")

        val prefs = getRecordListPreferences()
        val json = prefs.getString(destinationKey, null)
        if ((json == null) || (json.toString() == "[]"))
            return emptyList()

        val type = object : TypeToken<List<SerializedRecord>>() {}.type
        val serializedRecordList: List<SerializedRecord> =
            Gson().fromJson(json, type)

        val recordList = mutableListOf<Record>()
        serializedRecordList.forEach { serializedRecord ->
            val savePhotoDataList = mutableListOf<SavePhotoData>()

            serializedRecord.serializedSavePhotoDataList.forEach {
                // SavePhotoData 역직렬화 -> Triple에서 꺼내기
                val tourItemType = when (it.first) {
                    TourContentTypeId.TOURIST_DESTINATION.contentTypeId ->
                        object : TypeToken<TourItem.TouristDestination>() {}.type

                    TourContentTypeId.CULTURAL_FACILITIES.contentTypeId ->
                        object : TypeToken<TourItem.CulturalFacilities>() {}.type

                    TourContentTypeId.RESTAURANT.contentTypeId ->
                        object : TypeToken<TourItem.Restaurant>() {}.type

                    TourContentTypeId.LEISURE_SPORTS.contentTypeId ->
                        object : TypeToken<TourItem.LeisureSports>() {}.type

                    TourContentTypeId.EVENT_PERFORMANCE_FESTIVAL.contentTypeId ->
                        object : TypeToken<TourItem.EventPerformanceFestival>() {}.type

                    else ->
                        object : TypeToken<TourItem.TouristDestination>() {}.type
                }
                savePhotoDataList.add(
                    SavePhotoData(
                        Gson().fromJson(it.third, tourItemType),
                        it.second
                    )
                )
            }

            recordList.add(
                Record(
                    recordId = serializedRecord.recordId,
                    destination = serializedRecord.destination,
                    travelDate = serializedRecord.travelDate,
                    savePhotoDataList = savePhotoDataList
                )
            )
        }
        return recordList
    }
}

@Serializable
data class SerializedRecord(
    @SerializedName("recordid")
    val recordId: String,
    val destination: String,
    @SerializedName("traveldate")
    val travelDate: String,
    @SerializedName("serializedsavephotodatalist")
    val serializedSavePhotoDataList: List<Triple<String, MutableList<String>, String>>
) : java.io.Serializable