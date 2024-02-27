package com.twoday.todaytrip.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.twoday.todaytrip.R
import com.twoday.todaytrip.tourApi.TourNetworkClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //지역 기반 관광지 정보 조회 api 테스트 코드
        //callAreaBasedList()

        //공통 정보 조회 api 테스트 코드
        //callCommonDetail()

        //소개 정보 조회 api 테스트 코드
        callIntroDetail()
    }

    private fun callAreaBased() = CoroutineScope(Dispatchers.IO).launch {
        val areaBasedList = async{
            // 지역 코드 6: 부산
            TourNetworkClient.tourNetWork.getAreaBasedList(areaCode = "6")
        }.await()
        areaBasedList.response.body.items.item.forEach{
            Log.d("TourApiTest", "${it.title}, ${it.address?:"no address"}")
        }
    }

    private fun callCommonDetail() = CoroutineScope(Dispatchers.IO).launch {
        val commonDetail = async {
            // 콘텐츠 ID 126508: 경복궁
            TourNetworkClient.tourNetWork.getCommonDetail(contentId = "126508")
        }.await()
        commonDetail.response.body.items.item.forEach{
            Log.d("TourApiTest", "${it.title}, ${it.overview?:"no overview"}")
        }
    }

    private fun callIntroDetail() = CoroutineScope(Dispatchers.IO).launch {
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