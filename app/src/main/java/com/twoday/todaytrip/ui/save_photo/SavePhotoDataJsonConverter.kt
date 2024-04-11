package com.twoday.todaytrip.ui.save_photo

import android.util.Log
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.twoday.todaytrip.tourData.TourItemJsonConverter

/*
@JsonClass(generateAdapter = true)
data class SavePhotoData(
    val tourItem: TourItem,
    var imageUriList: MutableList<String> = mutableListOf(),
    val position: Int = 0
)
*/


@JsonClass(generateAdapter = true)
data class SavePhotoDataDTO(
    val tourItemJson: String,
    var imageUriList: MutableList<String> = mutableListOf(),
    val position: Int = 0
)

object SavePhotoDataJsonConverter {
    private val TAG = "SavePhotoDataJsonConverter"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    fun toJson(savePhotoData: SavePhotoData): String {
        Log.d(TAG, "toJson, savePhotoData:${savePhotoData.tourItem.getTitle()}")
        return try{
            val tourItemJson = TourItemJsonConverter.toJson(savePhotoData.tourItem)
            val savePhotoDataDTO =
                SavePhotoDataDTO(tourItemJson, savePhotoData.imageUriList, savePhotoData.position)
            moshi.adapter(SavePhotoDataDTO::class.java).toJson(savePhotoDataDTO)
        }catch (e: JsonDataException){
            e.printStackTrace()
            ""
        }
    }

    fun fromJson(json: String): SavePhotoData? {
        Log.d(TAG, "fromJson, json: ${json}")
        return try {
            val savePhotoDataDTO =
                moshi.adapter(SavePhotoDataDTO::class.java).fromJson(json)
            savePhotoDataDTO?.let {
                val tourItem = TourItemJsonConverter.fromJson(it.tourItemJson)
                tourItem?.let {tourItem ->
                    SavePhotoData(tourItem, it.imageUriList, it.position)
                }
            }
        }catch (e:JsonDataException){
            e.printStackTrace()
            null
        }
    }
}
