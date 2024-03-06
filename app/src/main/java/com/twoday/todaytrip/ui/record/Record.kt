package com.twoday.todaytrip.ui.record

import com.twoday.todaytrip.ui.save_photo.SavePhotoData
import com.twoday.todaytrip.utils.DateTimeUtil
import com.twoday.todaytrip.utils.DestinationPrefUtil
import kotlinx.serialization.Serializable

@Serializable
data class Record(
    val savePhotoDataList: List<SavePhotoData>
){
    val destination:String
    val travelDate:String
    init{
        destination = DestinationPrefUtil.loadDestination()
        travelDate = DateTimeUtil.getCurrentDate()
    }
}