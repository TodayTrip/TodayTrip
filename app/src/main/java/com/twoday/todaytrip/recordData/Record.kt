package com.twoday.todaytrip.recordData

import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.utils.DateTimeUtil
import com.twoday.todaytrip.utils.DestinationPrefUtil
import com.twoday.todaytrip.utils.PrefConstants

data class Record(
    val route: List<Pair<TourItem, String>> // Pair<장소, 장소에 추가한 사진 URL>
){
    val destination:String
    val travelDate:String
    init{
        destination = DestinationPrefUtil.loadDestination()
        travelDate = DateTimeUtil.getCurrentDate()
    }
}