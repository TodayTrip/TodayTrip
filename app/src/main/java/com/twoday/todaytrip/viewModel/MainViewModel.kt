package com.twoday.todaytrip.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.tourApi.TourNetworkClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {


    fun callAreaBased() = CoroutineScope(Dispatchers.IO).launch {
        val areaBasedList = async{
            // 지역 코드 6: 부산
            TourNetworkClient.tourNetWork.getAreaBasedList(areaCode = "6")
        }.await()
        areaBasedList.response.body.items.item.forEach{
            Log.d("TourApiTest", "${it.title}, ${it.address?:"no address"}")
        }
    }

    fun callCommonDetail() = CoroutineScope(Dispatchers.IO).launch {
        val commonDetail = async {
            // 콘텐츠 ID 126508: 경복궁
            TourNetworkClient.tourNetWork.getCommonDetail(contentId = "126508")
        }.await()
        commonDetail.response.body.items.item.forEach{
            Log.d("TourApiTest", "${it.title}, ${it.overview?:"no overview"}")
        }
    }

    fun callIntroDetail() = CoroutineScope(Dispatchers.IO).launch {
        val introDetail = async {
            // 콘텐츠 ID 126508: 경복궁
            // 콘텐츠 타입 ID 12: 관광지
            TourNetworkClient.tourNetWork.getIntroDetail(contentId = "126508", contentTypeId = "12")
        }.await()
        introDetail.response.body.items.item.forEach{
            Log.d("TourApiTest", "${it.contentId}, ${it.contentTypeId}")
        }
    }
}