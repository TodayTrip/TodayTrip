package com.twoday.todaytrip.viewModel

import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.utils.ContentIdPrefUtil
import com.twoday.todaytrip.utils.DestinationData
import com.twoday.todaytrip.utils.DestinationPrefUtil
import com.twoday.todaytrip.utils.TourItemPrefUtil

class RandomViewModel : ViewModel() {

    // 전체 랜덤 시 여행지 랜덤 선택하는 함수, 테마 Sharf에는 null로 저장
    fun selectRandomDestination() {
        val randomDestination = DestinationData.allRandomDestination.random()
        DestinationPrefUtil.saveDestination(randomDestination)
        DestinationPrefUtil.saveTheme("")
    }

    fun resetSharedPref() {
        TourItemPrefUtil.resetTourItemListPref()
        ContentIdPrefUtil.resetContentIdListPref()
    }
}