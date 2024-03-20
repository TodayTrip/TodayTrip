package com.twoday.todaytrip.viewModel

import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.utils.ContentIdPrefUtil
import com.twoday.todaytrip.utils.DestinationData
import com.twoday.todaytrip.utils.DestinationPrefUtil
import com.twoday.todaytrip.utils.PageNoPrefUtil
import com.twoday.todaytrip.utils.RecommendPrefUtil
import com.twoday.todaytrip.utils.TourItemPrefUtil

class RandomViewModel : ViewModel() {

    // 전체 랜덤 시 여행지 랜덤 선택하는 함수, 테마 Sharf에는 null로 저장
    // 완전랜덤을 안 쓰고 전체 랜덤도 지역 선택을 해서 함수 미사용?
    fun selectRandomDestination() {
        val randomDestination = DestinationData.allRandomDestination.random()
        DestinationPrefUtil.saveDestination(randomDestination)
        DestinationPrefUtil.saveTheme("")
    }

    fun resetSharedPref() {
        TourItemPrefUtil.resetTourItemListPref() // 관광지 정보 목록 초기화
        ContentIdPrefUtil.resetContentIdListPref() // 경로에 담은 관광지 정보 초기화
        PageNoPrefUtil.resetPageNoPref() // 관광지 목록 별 pageNo 초기화
        RecommendPrefUtil.resetRecommendTourItemPref() // 여행지 추천 코스 초기화
    }

    fun loadDestinationSharedPref(): String {
        return DestinationPrefUtil.loadDestination()
    }
}