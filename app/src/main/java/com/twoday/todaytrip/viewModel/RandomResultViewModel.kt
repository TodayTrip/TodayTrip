package com.twoday.todaytrip.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.R
import com.twoday.todaytrip.tourApi.TourApiUtils
import com.twoday.todaytrip.pref_utils.ContentIdPrefUtil
import com.twoday.todaytrip.utils.DestinationData
import com.twoday.todaytrip.pref_utils.DestinationPrefUtil
import com.twoday.todaytrip.pref_utils.PageNoPrefUtil
import com.twoday.todaytrip.pref_utils.RecommendPrefUtil
import com.twoday.todaytrip.pref_utils.TourItemPrefUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.measureTimedValue

class RandomResultViewModel : ViewModel() {
    private val TAG = "RandomResultViewModel"

    private val theme by lazy {
        DestinationPrefUtil.loadTheme()
    }
    private val areaCode by lazy {
        getDestinationAreaCode(
            DestinationPrefUtil.loadDestination()
        )
    }

    private val _isTouristAttractionListReady = MutableLiveData<Boolean>()
    val isTouristAttractionListReady: LiveData<Boolean>
        get() = _isTouristAttractionListReady

    val regionToMap
        get() = mapOf(
            "서울" to R.drawable.img_map_seoul,
            "인천" to R.drawable.img_map_incheon,
            "전북" to R.drawable.img_map_jeonbuk,
            "전남" to R.drawable.img_map_jeonnam,
            "경북" to R.drawable.img_map_gyeongbuk,
            "경남" to R.drawable.img_map_gyeongnam,
            "충북" to R.drawable.img_map_chungbuk,
            "충남" to R.drawable.img_map_chungnam,
            "강원" to R.drawable.img_map_gangwon,
            "대구" to R.drawable.img_map_daegu,
            "부산" to R.drawable.img_map_busan,
            "대전" to R.drawable.img_map_daejeon,
            "제주" to R.drawable.img_map_jeju,
            "경기" to R.drawable.img_map_gyeonggi,
            "광주" to R.drawable.img_map_gwangju,
            "울산" to R.drawable.img_map_ulsan
        )

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
        val timedValue = measureTimedValue {
            val touristAttractionJob = CoroutineScope(Dispatchers.IO).launch {
                fetchAndSaveTouristAttractionList()
            }
                touristAttractionJob.join()
        }
        Log.d(TAG, "chAndSaveTouristAttractionList duration: ${timedValue.duration}")

        withContext(Dispatchers.Main) {
            if (TourItemPrefUtil.loadTouristAttractionList().isEmpty())
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
                TourApiUtils.fetchTouristAttractionList(areaCode, pageNo)
            else
                TourApiUtils.fetchTouristAttractionListWithTheme(
                    theme,
                    areaCode,
                    pageNo
                )
        TourItemPrefUtil.saveTouristAttractionList(touristAttractionList)

        if (!touristAttractionList.isNullOrEmpty())
            PageNoPrefUtil.saveTouristAttractionPageNo(pageNo + 1)
    }
}