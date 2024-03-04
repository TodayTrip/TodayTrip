package com.twoday.todaytrip.recordData

import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.utils.DateTimeUtil
import com.twoday.todaytrip.utils.PrefConstants
import com.twoday.todaytrip.utils.SharedPreferencesUtil

data class Record(
    val route: List<Pair<TourItem, String>> // Pair<장소, 장소에 추가한 사진 URL>
){
    val destination:String
    val travelDate:String
    init{
        destination = loadDestination()
        travelDate = DateTimeUtil.getCurrentDate()
    }
    private fun loadDestination() =
        SharedPreferencesUtil.loadDestination(
            MyApplication.appContext!!,
            PrefConstants.DESTINATION_KEY
        )?:""
}