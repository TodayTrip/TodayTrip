package com.twoday.todaytrip.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.tourApi.TourNetworkInterfaceUtils
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.utils.DestinationData
import com.twoday.todaytrip.utils.DestinationPrefUtil
import com.twoday.todaytrip.utils.PageNoPrefUtil
import com.twoday.todaytrip.utils.TourItemPrefUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val TAG = "MainViewModel"

    private val theme by lazy {
        DestinationPrefUtil.loadTheme()
    }
    private val areaCode by lazy {
        getDestinationAreaCode(
            DestinationPrefUtil.loadDestination()
        )
    }

    private val MAX_API_CALL_COUNT = 3


    // API 호출 가능한 상태인지 아닌지를 저장 변수
    private var _isTouristAttractionLoadReady = true
    val isTouristAttractionLoadReady get() = _isTouristAttractionLoadReady
    private var _isRestaurantLoadReady = true
    val isRestaurantLoadReady get() = _isRestaurantLoadReady
    private var _isCafeLoadReady = true
    val isCafeLoadReady get() = _isCafeLoadReady
    private var _isEventLoadReady = true
    val isEventLoadReady get() = _isEventLoadReady

    // Shared Preference에서 꺼내거나 API 호출을 통해 받아온 정보 목록 저장 변수
    private val _touristAttractionList = MutableLiveData<List<TourItem>>()
    val touristAttractionList: LiveData<List<TourItem>>
        get() = _touristAttractionList
    private val _restaurantList = MutableLiveData<List<TourItem>>()
    val restaurantList: LiveData<List<TourItem>>
        get() = _restaurantList
    private val _cafeList = MutableLiveData<List<TourItem>>()
    val cafeList: LiveData<List<TourItem>>
        get() = _cafeList
    private val _eventList = MutableLiveData<List<TourItem>>()
    val eventList: LiveData<List<TourItem>>
        get() = _eventList

    init {
        getTourItemList()
    }

    private fun getDestinationAreaCode(destination: String): String =
        if (destination.isNullOrBlank()) ""
        else DestinationData.destinationAreaCodes[destination] ?: ""

    // 관광지 & 음식점 & 카페 & 행사/축제 정보 목록 초기화 함수
    // MainViewModel이 생성될 때(init) 최초로 한 번만 호출됨
    private fun getTourItemList() {
        loadOrFetchTouristAttractionList()
        loadOrFetchRestaurantList()
        loadOrFetchCafeList()
        loadOrFetchEventList()
    }

    // 관광지 정보 Shared Preference에 있는 경우 가져오고, 없는 경우 API 호출(최대 3번)을 통해 가져오는 함수
    // 관광지 탭에서 RecyclerView를 초기화할 때/ 불러오기 재시도할 때 호출됨
    fun loadOrFetchTouristAttractionList() = CoroutineScope(Dispatchers.IO).launch {
        CoroutineScope(Dispatchers.Main).launch {
            _isTouristAttractionLoadReady = false
        }

        var touristAttractionList: List<TourItem>? = null

        var apiCallCount = 0
        val pageNo = PageNoPrefUtil.loadTouristAttractionPageNo()
        while (true) {
            touristAttractionList = TourItemPrefUtil.loadTouristAttractionList()
            if (touristAttractionList.isNullOrEmpty() && (apiCallCount < MAX_API_CALL_COUNT)) {
                apiCallCount++
                delay(1000)
                Log.d(TAG, "fetch tourist attraction, apiCallCount: $apiCallCount")
                fetchAndSaveTouristAttractionList(pageNo)
            } else break
        }

        CoroutineScope(Dispatchers.Main).launch {
            _touristAttractionList.value = touristAttractionList!!.toList()
            _isTouristAttractionLoadReady = true
        }
        return@launch
    }
    // 음식점 정보 Shared Preference에 있는 경우 가져오고, 없는 경우 API 호출(최대 3번)을 통해 가져오는 함수
    // 음식점 탭에서 RecyclerView를 초기화할 때/ 불러오기 재시도할 때 호출됨
    private fun loadOrFetchRestaurantList() = CoroutineScope(Dispatchers.IO).launch {
        var restaurantList: List<TourItem>? = null

        var apiCallCount = 0
        val pageNo = PageNoPrefUtil.loadRestaurantPageNo()
        while (true) {
            restaurantList = TourItemPrefUtil.loadRestaurantList()
            if (restaurantList.isNullOrEmpty() && (apiCallCount < MAX_API_CALL_COUNT)) {
                apiCallCount++
                delay(1000)
                Log.d(TAG, "fetch restaurant attraction, apiCall: $apiCallCount")
                fetchAndSaveRestaurantList(pageNo)
            } else break
        }

        CoroutineScope(Dispatchers.Main).launch {
            _restaurantList.value = restaurantList!!.toList()
        }
        return@launch
    }
    // 카페 정보 Shared Preference에 있는 경우 가져오고, 없는 경우 API 호출(최대 3번)을 통해 가져오는 함수
    // 카페 탭에서 RecyclerView를 초기화할 때/ 불러오기 재시도할 때 호출됨
    private fun loadOrFetchCafeList() = CoroutineScope(Dispatchers.IO).launch {
        var cafeList: List<TourItem>? = null

        var apiCallCount = 0
        val pageNo = PageNoPrefUtil.loadCafePageNo()
        while (true) {
            cafeList = TourItemPrefUtil.loadCafeList()
            if (cafeList.isNullOrEmpty() && (apiCallCount < MAX_API_CALL_COUNT)) {
                apiCallCount++
                delay(1000)
                Log.d(TAG, "fetch cafe attraction, apiCall: $apiCallCount")
                fetchAndSaveCafeList(pageNo)
            } else break
        }

        CoroutineScope(Dispatchers.Main).launch {
            _cafeList.value = cafeList!!.toList()
        }
        return@launch
    }
    // 행사/축제 정보 Shared Preference에 있는 경우 가져오고, 없는 경우 API 호출(최대 3번)을 통해 가져오는 함수
    // 행사/축제 탭에서 RecyclerView를 초기화할 때/ 불러오기 재시도할 때 호출됨
    private fun loadOrFetchEventList() = CoroutineScope(Dispatchers.IO).launch {
        var eventList: List<TourItem>? = null

        var apiCallCount = 0
        val pageNo = PageNoPrefUtil.loadEventPageNo()
        while (true) {
            eventList = TourItemPrefUtil.loadEventList()
            if (eventList.isNullOrEmpty() && (apiCallCount < MAX_API_CALL_COUNT)) {
                apiCallCount++
                delay(1000)
                Log.d(TAG, "fetch event attraction, apiCall: $apiCallCount")
                fetchAndSaveEventList(pageNo)
            } else break
        }

        CoroutineScope(Dispatchers.Main).launch {
            _eventList.value = eventList!!.toList()
        }
        return@launch
    }

    // 관광지 정보를 API 호출을 통해 가져오고, Shared Preference에 저장하는 함수
    // loadOrFetchTouristAttractionList() 함수에서 호출됨
    private suspend fun fetchAndSaveTouristAttractionList(pageNo: Int) {
        Log.d(TAG, "fetchAndSaveTouristAttractionList) pageNo: $pageNo")
        val touristAttractionList = CoroutineScope(Dispatchers.IO).async {
            if (theme.isNullOrBlank())
                TourNetworkInterfaceUtils.fetchTouristAttractionList(areaCode, pageNo)
            else
                TourNetworkInterfaceUtils.fetchTouristAttractionListWithTheme(
                    theme,
                    areaCode,
                    pageNo
                )
        }
        TourItemPrefUtil.saveTouristAttractionList(touristAttractionList.await())
        if(touristAttractionList.await().isNotEmpty())
            PageNoPrefUtil.saveTouristAttractionPageNo(pageNo+1)
    }
    // 음식점 정보를 API 호출을 통해 가져오고, Shared Preference에 저장하는 함수
    // loadOrFetchRestaurantList() 함수에서 호출됨
    private suspend fun fetchAndSaveRestaurantList(pageNo: Int) {
        Log.d(TAG, "fetchAndSaveRestaurantList) pageNo: $pageNo")
        val restaurantList = CoroutineScope(Dispatchers.IO).async {
            TourNetworkInterfaceUtils.fetchRestaurantTabList(areaCode, pageNo)
        }
        TourItemPrefUtil.saveRestaurantList(restaurantList.await())
        if(restaurantList.await().isNotEmpty())
            PageNoPrefUtil.saveRestaurantPageNo(pageNo + 1)
    }
    // 카페 정보를 API 호출을 통해 가져오고, Shared Preference에 저장하는 함수
    // loadOrFetchCafeList() 함수에서 호출됨
    private suspend fun fetchAndSaveCafeList(pageNo: Int) {
        Log.d(TAG,"fetchAndSaveCafeList) pageNo: $pageNo")
        val cafeList = CoroutineScope(Dispatchers.IO).async {
            TourNetworkInterfaceUtils.fetchCafeTabList(areaCode, pageNo)
        }
        TourItemPrefUtil.saveCafeList(cafeList.await())
        if(cafeList.await().isNotEmpty())
                PageNoPrefUtil.saveCafePageNo(pageNo + 1)
    }
    // 행사/축제 정보를 API 호출을 통해 가져오고, Shared Preference에 저장하는 함수
    // loadOrFetchEventList() 함수에서 호출됨
    private suspend fun fetchAndSaveEventList(pageNo: Int) {
        Log.d(TAG, "fetchAndSaveEventList) pageNo: $pageNo")
        val eventList = CoroutineScope(Dispatchers.IO).async {
            TourNetworkInterfaceUtils.fetchEventTabList(areaCode, pageNo)
        }
        TourItemPrefUtil.saveEventList(eventList.await())
        if(eventList.await().isNotEmpty())
            PageNoPrefUtil.saveEventPageNo(pageNo + 1)
    }

    // 다음 페이지의 관광지 정보를 API 호출을 총해 가져온 뒤,
    // 기존의 목록 뒤에 붙여 Shared Preference에 저장하는 함수
    // Infinite Scroll을 위해 사용됨
    fun fetchAndSaveMoreTouristAttractionList() = CoroutineScope(Dispatchers.IO).launch{
        CoroutineScope(Dispatchers.Main).launch {
            _isTouristAttractionLoadReady = false
        }

        val pageNo = PageNoPrefUtil.loadTouristAttractionPageNo()
        Log.d(TAG, "fetchAndSaveMoreTouristAttractionList) pageNo: $pageNo")
        val moreTouristAttractionList = async {
            if (theme.isNullOrBlank())
                TourNetworkInterfaceUtils.fetchTouristAttractionList(areaCode, pageNo)
            else
                TourNetworkInterfaceUtils.fetchTouristAttractionListWithTheme(
                    theme,
                    areaCode,
                    pageNo
                )
        }.await()
        Log.d(TAG, "fetchAndSaveMoreTouristAttractionList) moreTouristAttractionList size: ${moreTouristAttractionList.size}")
        if(moreTouristAttractionList.isEmpty()) return@launch
        TourItemPrefUtil.saveMoreTouristAttractionList(moreTouristAttractionList)
        PageNoPrefUtil.saveTouristAttractionPageNo(pageNo + 1)

        CoroutineScope(Dispatchers.Main).launch {
            _touristAttractionList.value = TourItemPrefUtil.loadTouristAttractionList()
            _isTouristAttractionLoadReady = true
        }
    }
    // 다음 페이지의 음식점 정보를 API 호출을 총해 가져온 뒤,
    // 기존의 목록 뒤에 붙여 Shared Preference에 저장하는 함수
    // Infinite Scroll을 위해 사용됨
    fun fetchAndSaveMoreRestaurantList() = CoroutineScope(Dispatchers.IO).launch{
        CoroutineScope(Dispatchers.Main).launch {
            _isRestaurantLoadReady = false
        }

        val pageNo = PageNoPrefUtil.loadRestaurantPageNo()
        Log.d(TAG, "fetchAndSaveMoreRestaurantList) pageNo: $pageNo")
        val moreRestaurantList = async {
            TourNetworkInterfaceUtils.fetchRestaurantTabList(areaCode, pageNo)
        }.await()
        Log.d(TAG, "fetchAndSaveMoreRestaurantList) moreRestaurantList size: ${moreRestaurantList.size}")
        if(moreRestaurantList.isEmpty()) return@launch
        TourItemPrefUtil.saveMoreRestaurantList(moreRestaurantList)
        PageNoPrefUtil.saveRestaurantPageNo(pageNo + 1)

        CoroutineScope(Dispatchers.Main).launch {
            _restaurantList.value = TourItemPrefUtil.loadRestaurantList()
            _isRestaurantLoadReady = true
        }
    }
    // 다음 페이지의 카페 정보를 API 호출을 총해 가져온 뒤,
    // 기존의 목록 뒤에 붙여 Shared Preference에 저장하는 함수
    // Infinite Scroll을 위해 사용됨
    fun fetchAndSaveMoreCafeList() = CoroutineScope(Dispatchers.IO).launch{
        CoroutineScope(Dispatchers.Main).launch {
            _isCafeLoadReady = false
        }

        val pageNo = PageNoPrefUtil.loadCafePageNo()
        Log.d(TAG, "fetchAndSaveMoreCafeList) pageNo: $pageNo")
        val moreCafeList = async {
            TourNetworkInterfaceUtils.fetchCafeTabList(areaCode, pageNo)
        }.await()
        Log.d(TAG, "fetchAndSaveMoreCafeList) moreCafeList size: ${moreCafeList.size}")
        if(moreCafeList.isEmpty()) return@launch
        TourItemPrefUtil.saveMoreCafeList(moreCafeList)
        PageNoPrefUtil.saveCafePageNo(pageNo + 1)

        CoroutineScope(Dispatchers.Main).launch {
            _cafeList.value = TourItemPrefUtil.loadCafeList()
            _isCafeLoadReady = true
        }
    }
    // 다음 페이지의 행사/축제 정보를 API 호출을 총해 가져온 뒤,
    // 기존의 목록 뒤에 붙여 Shared Preference에 저장하는 함수
    // Infinite Scroll을 위해 사용됨
    fun fetchAndSaveMoreEventList() = CoroutineScope(Dispatchers.IO).launch{
        CoroutineScope(Dispatchers.Main).launch {
            _isEventLoadReady = false
        }

        val pageNo = PageNoPrefUtil.loadEventPageNo()
        Log.d(TAG, "fetchAndSaveMoreEventList) pageNo: $pageNo")
        val moreEventList = async {
            TourNetworkInterfaceUtils.fetchEventTabList(areaCode, pageNo)
        }.await()
        Log.d(TAG, "fetchAndSaveMoreEventList) moreEventList size: ${moreEventList.size}")
        if(moreEventList.isEmpty()) return@launch
        TourItemPrefUtil.saveMoreEventList(moreEventList)
        PageNoPrefUtil.saveEventPageNo(pageNo + 1)

        CoroutineScope(Dispatchers.Main).launch {
            _eventList.value = TourItemPrefUtil.loadEventList()
            _isEventLoadReady = true
        }
    }
}