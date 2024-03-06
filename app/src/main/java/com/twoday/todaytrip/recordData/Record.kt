package com.twoday.todaytrip.recordData

import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.ui.route.SavePhotoData
import com.twoday.todaytrip.utils.DateTimeUtil
import com.twoday.todaytrip.utils.DestinationPrefUtil
import com.twoday.todaytrip.utils.PrefConstants
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