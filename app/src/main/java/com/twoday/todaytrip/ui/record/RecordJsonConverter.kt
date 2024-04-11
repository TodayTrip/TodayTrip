package com.twoday.todaytrip.ui.record

import android.util.Log
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.twoday.todaytrip.ui.save_photo.SavePhotoData
import com.twoday.todaytrip.ui.save_photo.SavePhotoDataJsonConverter

/*
@JsonClass(generateAdapter = true)
data class Record(
    val recordId: String = UUID.randomUUID().toString(),
    val destination:String = DestinationPrefUtil.loadDestination(),
    val travelDate:String = DateTimeUtil.getCurrentDate(),
    val savePhotoDataList: List<SavePhotoData>
)
*/

@JsonClass(generateAdapter = true)
data class RecordDTO(
    val recordId: String,
    val destination: String,
    val travelDate: String,
    val savePhotoDataJsonList: List<String>
)

object RecordJsonConverter {
    private val TAG = "RecordJsonConverter"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    fun toJson(record: Record): String {
        Log.d(TAG, "toJson, record:${record.destination}")
        return try {
            val savePhotoDataJsonList = mutableListOf<String>()
            record.savePhotoDataList.forEach { savePhotoData ->
                SavePhotoDataJsonConverter.toJson(savePhotoData)?.let {
                    savePhotoDataJsonList.add(it)
                }
            }
            val recordDTO = record.run {
                RecordDTO(recordId, destination, travelDate, savePhotoDataJsonList)
            }
            moshi.adapter(RecordDTO::class.java).toJson(recordDTO)
        } catch (e: JsonDataException) {
            e.printStackTrace()
            ""
        }
    }

    fun fromJson(json: String): Record? {
        Log.d(TAG, "fromJson, json: ${json}")
        return try {
            val recordDTO = moshi.adapter(RecordDTO::class.java).fromJson(json)
            recordDTO?.let {
                val savePhotoDataList = mutableListOf<SavePhotoData>()
                it.savePhotoDataJsonList.forEach { savePhotoDataJson ->
                    SavePhotoDataJsonConverter.fromJson(savePhotoDataJson)?.let { savePhotoData ->
                        savePhotoDataList.add(savePhotoData)
                    }
                }
                Record(it.recordId, it.destination, it.travelDate, savePhotoDataList)
            }
        } catch (e: JsonDataException) {
            e.printStackTrace()
            null
        }
    }
}
