package com.twoday.todaytrip.viewModel

import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.pref_utils.ContentIdPrefUtil
import com.twoday.todaytrip.pref_utils.DestinationPrefUtil
import com.twoday.todaytrip.pref_utils.PageNoPrefUtil
import com.twoday.todaytrip.pref_utils.RecommendPrefUtil
import com.twoday.todaytrip.pref_utils.TourItemPrefUtil

class RandomViewModel : ViewModel() {
    fun resetSharedPref() {
        DestinationPrefUtil.resetDestinationPref()
        TourItemPrefUtil.resetTourItemListPref() // 관광지 정보 목록 초기화
        ContentIdPrefUtil.resetContentIdListPref() // 경로에 담은 관광지 정보 초기화
        PageNoPrefUtil.resetPageNoPref() // 관광지 목록 별 pageNo 초기화
        RecommendPrefUtil.resetRecommendTourItemPref() // 여행지 추천 코스 초기화
    }

    fun loadDestinationSharedPref(): String {
        return DestinationPrefUtil.loadDestination()
    }
}