package com.twoday.todaytrip.ui.record

import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.ui.save_photo.SavePhotoData
import com.twoday.todaytrip.utils.DateTimeUtil
import com.twoday.todaytrip.utils.DestinationPrefUtil
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Record(
    val recordId: String = UUID.randomUUID().toString(),
    val destination:String = DestinationPrefUtil.loadDestination(),
    val travelDate:String = DateTimeUtil.getCurrentDate(),
    val savePhotoDataList: List<SavePhotoData>
)