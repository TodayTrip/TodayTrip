package com.twoday.todaytrip.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.tourApi.TourNetworkInterfaceUtils
import com.twoday.todaytrip.utils.ContentIdPrefUtil
import com.twoday.todaytrip.utils.DestinationData
import com.twoday.todaytrip.utils.DestinationPrefUtil
import com.twoday.todaytrip.utils.PageNoPrefUtil
import com.twoday.todaytrip.utils.RecommendPrefUtil
import com.twoday.todaytrip.utils.TourItemPrefUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RandomResultViewModel : ViewModel() {
    private val TAG = "RandomResultViewModel"

    private val theme by lazy{
        DestinationPrefUtil.loadTheme()
    }
    private val areaCode by lazy{
        getDestinationAreaCode(
            DestinationPrefUtil.loadDestination()
        )
    }

    private val _isTouristAttractionListReady = MutableLiveData<Boolean>()
    val isTouristAttractionListReady: LiveData<Boolean>
        get() = _isTouristAttractionListReady

    init {
        resetSharedPref()

        _isTouristAttractionListReady.value = false
        CoroutineScope(Dispatchers.IO).launch {
            fetchAndSaveTourItemList()
        }
    }

    private fun resetSharedPref() {
        TourItemPrefUtil.resetTourItemListPref() // 관광지 정보 목록 초기화
        ContentIdPrefUtil.resetContentIdListPref() // 경로에 담은 관광지 정보 초기화
        PageNoPrefUtil.resetPageNoPref() // 관광지 목록 별 pageNo 초기화
        RecommendPrefUtil.resetRecommendTourItemPref() // 여행지 추천 코스 초기화
    }


    private suspend fun fetchAndSaveTourItemList() {
        val touristAttractionJob = CoroutineScope(Dispatchers.IO).launch {
            fetchAndSaveTouristAttractionList()
        }
        touristAttractionJob.join()

        withContext(Dispatchers.Main) {
            if(TourItemPrefUtil.loadTouristAttractionList().isEmpty())
                delay(3000)
            _isTouristAttractionListReady.value = true
        }
    }

    private fun getDestinationAreaCode(destination: String?): String =
        if (destination.isNullOrBlank()) ""
        else DestinationData.destinationAreaCodes[destination] ?: ""

    private suspend fun fetchAndSaveTouristAttractionList() {
        val pageNo = PageNoPrefUtil.FIRST_PAGE

        val touristAttractionList =
            if (theme.isNullOrBlank())
                TourNetworkInterfaceUtils.fetchTouristAttractionList(areaCode, pageNo)
            else
                TourNetworkInterfaceUtils.fetchTouristAttractionListWithTheme(theme, areaCode, pageNo)
        TourItemPrefUtil.saveTouristAttractionList(touristAttractionList)

        if(!touristAttractionList.isNullOrEmpty())
            PageNoPrefUtil.saveTouristAttractionPageNo(pageNo+1)
    }
}