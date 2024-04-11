package com.twoday.todaytrip.ui.record

import com.squareup.moshi.JsonClass
import com.twoday.todaytrip.ui.save_photo.SavePhotoData
import com.twoday.todaytrip.utils.DateTimeUtil
import com.twoday.todaytrip.pref_utils.DestinationPrefUtil
import java.util.UUID

@JsonClass(generateAdapter = true)
data class Record(
    val recordId: String = UUID.randomUUID().toString(),
    val destination:String = DestinationPrefUtil.loadDestination(),
    val travelDate:String = DateTimeUtil.getCurrentDate(),
    val savePhotoDataList: List<SavePhotoData>
)