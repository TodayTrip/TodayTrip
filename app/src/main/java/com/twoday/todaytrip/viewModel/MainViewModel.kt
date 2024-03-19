package com.twoday.todaytrip.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.R
import com.twoday.todaytrip.tourApi.TourNetworkInterfaceUtils
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.ui.place_list.RecommendCover
import com.twoday.todaytrip.ui.place_list.RecommendData
import com.twoday.todaytrip.ui.place_list.RecommendTourItem
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
    private val destination by lazy {
        DestinationPrefUtil.loadDestination()
    }
    // TODO 여행지 시군구 선택
    private val destinationSigungu = "전체"

    private val areaCode by lazy {
        getDestinationAreaCode(destination)
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

    // 오늘의 랜덤 코스에 뜰 관광지 정보
    private val _recommendDataList = MutableLiveData<List<RecommendData>>()
    val recommendDataList: LiveData<List<RecommendData>> get() = _recommendDataList

    // 무한 스크롤을 위한 API 호출 성공 여부 저장
    // 초기값 = 0 (무한 스크롤 호출 전)
    // 페이지 번호가 1로 바뀌었다면 무한 스크롤 성공적, -1로 바뀌었다면 무한 스크롤 실패
    private val _touristAttractionMoreLoaded = MutableLiveData<Int>(0)
    val touristAttractionMoreLoaded
        get() = _touristAttractionMoreLoaded
    private val _restaurantMoreLoaded = MutableLiveData<Int>(0)
    val restaurantMoreLoaded get() = _restaurantMoreLoaded
    private val _cafeMoreLoaded = MutableLiveData<Int>(0)
    val cafeMoreLoaded get() = _cafeMoreLoaded
    private val _eventMoreLoaded = MutableLiveData<Int>(0)
    val eventMoreLoaded get() = _eventMoreLoaded

    init {
        initRecommendDataList()
        getTourItemAndRecommendDataList()
    }

    private fun initRecommendDataList() {
        _recommendDataList.value = listOf(
            RecommendCover(
                imageId = getTitleImageId(destination)!!,
                subTitleId = R.string.place_list_recommend_sub_title_cover,
                title = "$destination $destinationSigungu"
            )
        )
    }

    private fun getTitleImageId(destination: String): Int? {
        return when (destination) {
            "서울" -> listOf(
                R.drawable.img_seoul1,
                R.drawable.img_seoul2,
                R.drawable.img_seoul3,
                R.drawable.img_seoul4
            ).random()

            "인천" -> listOf(
                R.drawable.img_incheon1,
                R.drawable.img_incheon2,
                R.drawable.img_incheon3
            ).random()

            "전북" -> listOf(
                R.drawable.img_jeonbuk1,
                R.drawable.img_jeonbuk2
            ).random()

            "전남" -> listOf(
                R.drawable.img_jeonnam1,
                R.drawable.img_jeonnam2,
                R.drawable.img_jeonnam3,
                R.drawable.img_jeonnam4,
                R.drawable.img_jeonnam5
            ).random()

            "경북" -> listOf(
                R.drawable.img_gyeongbuk1,
                R.drawable.img_gyeongbuk2,
                R.drawable.img_gyeongbuk3
            ).random()

            "경남" -> listOf(
                R.drawable.img_gyeongnam1,
                R.drawable.img_gyeongnam2,
                R.drawable.img_gyeongnam3
            ).random()

            "충북" -> listOf(
                R.drawable.img_chungbuk2,
                R.drawable.img_chungbuk3,
                R.drawable.img_chungbuk4,
                R.drawable.img_chungbuk5
            ).random()

            "충남" -> listOf(
                R.drawable.img_chungnam1,
                R.drawable.img_chungnam2,
                R.drawable.img_chungnam3
            ).random()

            "강원" -> listOf(
                R.drawable.img_gangwon1,
                R.drawable.img_gangwon2,
                R.drawable.img_gangwon3
            ).random()

            "대구" -> listOf(
                R.drawable.img_daegu1,
                R.drawable.img_daegu2,
                R.drawable.img_daegu3
            ).random()


            "부산" -> listOf(
                R.drawable.img_busan1,
                R.drawable.img_busan2,
                R.drawable.img_busan3,
                R.drawable.img_busan4
            ).random()

            "대전" -> listOf(
                R.drawable.img_seoul1
            ).random()

            "제주" -> listOf(
                R.drawable.img_jeju1,
                R.drawable.img_jeju2,
                R.drawable.img_jeju3
            ).random()

            "경기" -> listOf(
                R.drawable.img_gyeonggi1,
                R.drawable.img_gyeonggi2,
                R.drawable.img_gyeonggi3
            ).random()

            "광주" -> listOf(
                R.drawable.img_gwangju1,
                R.drawable.img_gwangju2,
                R.drawable.img_gwangju3
            ).random()

            "울산" -> listOf(
                R.drawable.img_ulsan1
            ).random()

            else -> null
        }
    }


    private fun getDestinationAreaCode(destination: String): String =
        if (destination.isNullOrBlank()) ""
        else DestinationData.destinationAreaCodes[destination] ?: ""

    // 관광지,음식점,카페,행사/축제 정보 목록 & 추천 관광지 정보 목록 초기화 함수
    // MainViewModel이 생성될 때(init) 최초로 한 번만 호출됨
    private fun getTourItemAndRecommendDataList() = CoroutineScope(Dispatchers.IO).launch {
        val touristAttractionJob = loadOrFetchTouristAttractionList()
        val restaurantJob = loadOrFetchRestaurantList()
        val cafeJob = loadOrFetchCafeList()
        val eventJob = loadOrFetchEventList()

        touristAttractionJob.join()
        CoroutineScope(Dispatchers.Main).launch {
            addRecommendTouristAttraction()
        }

        restaurantJob.join()
        CoroutineScope(Dispatchers.Main).launch {
            addRecommendRestaurant()
        }

        cafeJob.join()
        CoroutineScope(Dispatchers.Main).launch {
            addRecommendCafe()
        }

        eventJob.join()
        CoroutineScope(Dispatchers.Main).launch {
            addRecommendEvent()
        }
    }

    private fun addRecommendTouristAttraction() {
        if (!_touristAttractionList.value.isNullOrEmpty()) {
            val recommendTouristAttraction = _touristAttractionList.value!!.random()
//            _touristAttractionList.value = _touristAttractionList.value!!.filter {
//                it.getContentId() != recommendTouristAttraction.getContentId()
//            }

            Log.d(TAG, "added recommend: ${recommendTouristAttraction.getTitle()}")
            _recommendDataList.value = _recommendDataList.value!!.toMutableList().apply {
                add(
                    RecommendTourItem(
                        imageUrl = recommendTouristAttraction.getImage(),
                        subTitleId = R.string.place_list_recommend_sub_title_tourist_attraction,
                        title = recommendTouristAttraction.getTitle(),
                        tourItem = recommendTouristAttraction
                    )
                )
            }
        }
    }

    private fun addRecommendRestaurant() {
        if (!_restaurantList.value.isNullOrEmpty()) {
            val recommendRestaurant = _restaurantList.value!!.random()
//            _restaurantList.value = _restaurantList.value!!.filter {
//                it.getContentId() != recommendRestaurant.getContentId()
//            }

            Log.d(TAG, "added recommend: ${recommendRestaurant.getTitle()}")
            _recommendDataList.value = _recommendDataList.value!!.toMutableList().apply {
                add(
                    RecommendTourItem(
                        imageUrl = recommendRestaurant.getImage(),
                        subTitleId = R.string.place_list_recommend_sub_title_restaurant,
                        title = recommendRestaurant.getTitle(),
                        tourItem = recommendRestaurant
                    )
                )
            }
        }
    }
    private fun addRecommendCafe() {
        if (!_cafeList.value.isNullOrEmpty()) {
            val recommendCafe = _cafeList.value!!.random()
//            _cafeList.value = _cafeList.value!!.filter {
//                it.getContentId() != recommendCafe.getContentId()
//            }

            Log.d(TAG, "added recommend: ${recommendCafe.getTitle()}")
            _recommendDataList.value = _recommendDataList.value!!.toMutableList().apply {
                add(
                    RecommendTourItem(
                        imageUrl = recommendCafe.getImage(),
                        subTitleId = R.string.place_list_recommend_sub_title_cafe,
                        title = recommendCafe.getTitle(),
                        tourItem = recommendCafe
                    )
                )
            }
        }
    }
    private fun addRecommendEvent() {
        if (!_eventList.value.isNullOrEmpty()) {
            val recommendEvent = _eventList.value!!.random()
//            _eventList.value = _eventList.value!!.filter {
//                it.getContentId() != recommendEvent.getContentId()
//            }

            Log.d(TAG, "added recommend: ${recommendEvent.getTitle()}")
            _recommendDataList.value = _recommendDataList.value!!.toMutableList().apply {
                add(
                    RecommendTourItem(
                        imageUrl = recommendEvent.getImage(),
                        subTitleId = R.string.place_list_recommend_sub_title_event,
                        title = recommendEvent.getTitle(),
                        tourItem = recommendEvent
                    )
                )
            }
        }
    }

    // 관광지 정보 Shared Preference에 있는 경우 가져오고, 없는 경우 API 호출(최대 3번)을 통해 가져오는 함수
    // 관광지 탭에서 RecyclerView를 초기화할 때/ 불러오기 재시도할 때 호출됨
    fun loadOrFetchTouristAttractionList() = CoroutineScope(Dispatchers.IO).launch {
        CoroutineScope(Dispatchers.Main).launch {
            _isTouristAttractionLoadReady = false
        }

        var touristAttractionList: List<TourItem>? = null

        var apiCallCount = 0
        while (true) {
            touristAttractionList = TourItemPrefUtil.loadTouristAttractionList()
            if (touristAttractionList.isNullOrEmpty() && (apiCallCount < MAX_API_CALL_COUNT)) {
                apiCallCount++
                delay(1000)
                Log.d(TAG, "fetch tourist attraction, apiCallCount: $apiCallCount")
                fetchAndSaveTouristAttractionList()
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
    fun loadOrFetchRestaurantList() = CoroutineScope(Dispatchers.IO).launch {
        var restaurantList: List<TourItem>? = null

        var apiCallCount = 0
        while (true) {
            restaurantList = TourItemPrefUtil.loadRestaurantList()
            if (restaurantList.isNullOrEmpty() && (apiCallCount < MAX_API_CALL_COUNT)) {
                apiCallCount++
                delay(1000)
                Log.d(TAG, "fetch restaurant attraction, apiCall: $apiCallCount")
                fetchAndSaveRestaurantList()
            } else break
        }

        CoroutineScope(Dispatchers.Main).launch {
            _restaurantList.value = restaurantList!!.toList()
        }
        return@launch
    }

    // 카페 정보 Shared Preference에 있는 경우 가져오고, 없는 경우 API 호출(최대 3번)을 통해 가져오는 함수
    // 카페 탭에서 RecyclerView를 초기화할 때/ 불러오기 재시도할 때 호출됨
    fun loadOrFetchCafeList() = CoroutineScope(Dispatchers.IO).launch {
        var cafeList: List<TourItem>? = null

        var apiCallCount = 0
        while (true) {
            cafeList = TourItemPrefUtil.loadCafeList()
            if (cafeList.isNullOrEmpty() && (apiCallCount < MAX_API_CALL_COUNT)) {
                apiCallCount++
                delay(1000)
                Log.d(TAG, "fetch cafe attraction, apiCall: $apiCallCount")
                fetchAndSaveCafeList()
            } else break
        }

        CoroutineScope(Dispatchers.Main).launch {
            _cafeList.value = cafeList!!.toList()
        }
        return@launch
    }

    // 행사/축제 정보 Shared Preference에 있는 경우 가져오고, 없는 경우 API 호출(최대 3번)을 통해 가져오는 함수
    // 행사/축제 탭에서 RecyclerView를 초기화할 때/ 불러오기 재시도할 때 호출됨
    fun loadOrFetchEventList() = CoroutineScope(Dispatchers.IO).launch {
        var eventList: List<TourItem>? = null

        var apiCallCount = 0
        while (true) {
            eventList = TourItemPrefUtil.loadEventList()
            if (eventList.isNullOrEmpty() && (apiCallCount < MAX_API_CALL_COUNT)) {
                apiCallCount++
                delay(1000)
                Log.d(TAG, "fetch event attraction, apiCall: $apiCallCount")
                fetchAndSaveEventList()
            } else break
        }

        CoroutineScope(Dispatchers.Main).launch {
            _eventList.value = eventList!!.toList()
        }
        return@launch
    }

    // 관광지 정보를 API 호출을 통해 가져오고, Shared Preference에 저장하는 함수
    // loadOrFetchTouristAttractionList() 함수에서 호출됨
    private suspend fun fetchAndSaveTouristAttractionList() {
        val pageNo = PageNoPrefUtil.loadTouristAttractionPageNo()
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
        if (touristAttractionList.await().isNotEmpty())
            PageNoPrefUtil.saveTouristAttractionPageNo(pageNo + 1)
    }

    // 음식점 정보를 API 호출을 통해 가져오고, Shared Preference에 저장하는 함수
    // loadOrFetchRestaurantList() 함수에서 호출됨
    private suspend fun fetchAndSaveRestaurantList() {
        val pageNo = PageNoPrefUtil.loadRestaurantPageNo()
        Log.d(TAG, "fetchAndSaveRestaurantList) pageNo: $pageNo")

        val restaurantList = CoroutineScope(Dispatchers.IO).async {
            TourNetworkInterfaceUtils.fetchRestaurantTabList(areaCode, pageNo)
        }
        TourItemPrefUtil.saveRestaurantList(restaurantList.await())
        if (restaurantList.await().isNotEmpty())
            PageNoPrefUtil.saveRestaurantPageNo(pageNo + 1)
    }

    // 카페 정보를 API 호출을 통해 가져오고, Shared Preference에 저장하는 함수
    // loadOrFetchCafeList() 함수에서 호출됨
    private suspend fun fetchAndSaveCafeList() {
        val pageNo = PageNoPrefUtil.loadCafePageNo()
        Log.d(TAG, "fetchAndSaveCafeList) pageNo: $pageNo")

        val cafeList = CoroutineScope(Dispatchers.IO).async {
            TourNetworkInterfaceUtils.fetchCafeTabList(areaCode, pageNo)
        }
        TourItemPrefUtil.saveCafeList(cafeList.await())
        if (cafeList.await().isNotEmpty())
            PageNoPrefUtil.saveCafePageNo(pageNo + 1)
    }

    // 행사/축제 정보를 API 호출을 통해 가져오고, Shared Preference에 저장하는 함수
    // loadOrFetchEventList() 함수에서 호출됨
    private suspend fun fetchAndSaveEventList() {
        val pageNo = PageNoPrefUtil.loadEventPageNo()
        Log.d(TAG, "fetchAndSaveEventList) pageNo: $pageNo")

        val eventList = CoroutineScope(Dispatchers.IO).async {
            TourNetworkInterfaceUtils.fetchEventTabList(areaCode, pageNo)
        }
        TourItemPrefUtil.saveEventList(eventList.await())
        if (eventList.await().isNotEmpty())
            PageNoPrefUtil.saveEventPageNo(pageNo + 1)
    }

    // 다음 페이지의 관광지 정보를 API 호출을 총해 가져온 뒤,
    // 기존의 목록 뒤에 붙여 Shared Preference에 저장하는 함수
    // Infinite Scroll을 위해 사용됨
    fun fetchAndSaveMoreTouristAttractionList() = CoroutineScope(Dispatchers.IO).launch {
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

        Log.d(
            TAG,
            "fetchAndSaveMoreTouristAttractionList) moreTouristAttractionList size: ${moreTouristAttractionList.size}"
        )
        if (moreTouristAttractionList.isEmpty()) {
            delay(700)
            CoroutineScope(Dispatchers.Main).launch {
                _touristAttractionMoreLoaded.value = -1
                _isTouristAttractionLoadReady = true
            }
            return@launch
        }

        TourItemPrefUtil.saveMoreTouristAttractionList(moreTouristAttractionList)
        PageNoPrefUtil.saveTouristAttractionPageNo(pageNo + 1)

        CoroutineScope(Dispatchers.Main).launch {
            _touristAttractionList.value = TourItemPrefUtil.loadTouristAttractionList()
            _touristAttractionMoreLoaded.value = 1
            _isTouristAttractionLoadReady = true
        }
    }

    // 다음 페이지의 음식점 정보를 API 호출을 총해 가져온 뒤,
    // 기존의 목록 뒤에 붙여 Shared Preference에 저장하는 함수
    // Infinite Scroll을 위해 사용됨
    fun fetchAndSaveMoreRestaurantList() = CoroutineScope(Dispatchers.IO).launch {
        CoroutineScope(Dispatchers.Main).launch {
            _isRestaurantLoadReady = false
        }

        val pageNo = PageNoPrefUtil.loadRestaurantPageNo()
        Log.d(TAG, "fetchAndSaveMoreRestaurantList) pageNo: $pageNo")

        val moreRestaurantList = async {
            TourNetworkInterfaceUtils.fetchRestaurantTabList(areaCode, pageNo)
        }.await()

        Log.d(
            TAG,
            "fetchAndSaveMoreRestaurantList) moreRestaurantList size: ${moreRestaurantList.size}"
        )
        if (moreRestaurantList.isEmpty()) {
            delay(700)
            CoroutineScope(Dispatchers.Main).launch {
                _restaurantMoreLoaded.value = -1
                _isRestaurantLoadReady = true
            }
            return@launch
        }
        TourItemPrefUtil.saveMoreRestaurantList(moreRestaurantList)
        PageNoPrefUtil.saveRestaurantPageNo(pageNo + 1)

        CoroutineScope(Dispatchers.Main).launch {
            _restaurantList.value = TourItemPrefUtil.loadRestaurantList()
            _restaurantMoreLoaded.value = 1
            _isRestaurantLoadReady = true
        }
    }

    // 다음 페이지의 카페 정보를 API 호출을 총해 가져온 뒤,
    // 기존의 목록 뒤에 붙여 Shared Preference에 저장하는 함수
    // Infinite Scroll을 위해 사용됨
    fun fetchAndSaveMoreCafeList() = CoroutineScope(Dispatchers.IO).launch {
        CoroutineScope(Dispatchers.Main).launch {
            _isCafeLoadReady = false
        }

        val pageNo = PageNoPrefUtil.loadCafePageNo()
        Log.d(TAG, "fetchAndSaveMoreCafeList) pageNo: $pageNo")

        val moreCafeList = async {
            TourNetworkInterfaceUtils.fetchCafeTabList(areaCode, pageNo)
        }.await()

        Log.d(TAG, "fetchAndSaveMoreCafeList) moreCafeList size: ${moreCafeList.size}")
        if (moreCafeList.isEmpty()) {
            delay(700)
            CoroutineScope(Dispatchers.Main).launch {
                _cafeMoreLoaded.value = -1
                _isCafeLoadReady = true
            }
            return@launch
        }

        TourItemPrefUtil.saveMoreCafeList(moreCafeList)
        PageNoPrefUtil.saveCafePageNo(pageNo + 1)

        CoroutineScope(Dispatchers.Main).launch {
            _cafeList.value = TourItemPrefUtil.loadCafeList()
            _cafeMoreLoaded.value = 1
            _isCafeLoadReady = true
        }
    }

    // 다음 페이지의 행사/축제 정보를 API 호출을 총해 가져온 뒤,
    // 기존의 목록 뒤에 붙여 Shared Preference에 저장하는 함수
    // Infinite Scroll을 위해 사용됨
    fun fetchAndSaveMoreEventList() = CoroutineScope(Dispatchers.IO).launch {
        CoroutineScope(Dispatchers.Main).launch {
            _isEventLoadReady = false
        }

        val pageNo = PageNoPrefUtil.loadEventPageNo()
        Log.d(TAG, "fetchAndSaveMoreEventList) pageNo: $pageNo")
        val moreEventList = async {
            TourNetworkInterfaceUtils.fetchEventTabList(areaCode, pageNo)
        }.await()

        Log.d(TAG, "fetchAndSaveMoreEventList) moreEventList size: ${moreEventList.size}")
        if (moreEventList.isEmpty()) {
            delay(700)
            CoroutineScope(Dispatchers.Main).launch {
                _eventMoreLoaded.value = -1
                _isEventLoadReady = true
            }
            return@launch
        }

        TourItemPrefUtil.saveMoreEventList(moreEventList)
        PageNoPrefUtil.saveEventPageNo(pageNo + 1)

        CoroutineScope(Dispatchers.Main).launch {
            _eventList.value = TourItemPrefUtil.loadEventList()
            _eventMoreLoaded.value = 1
            _isEventLoadReady = true
        }
    }

    // API 추가 로딩 후, 추가 로딩 실패/성공 저장 변수 디폴트 값으로 돌려놓는 함수
    fun setTouristAttractionMoreLoadedDefault() {
        _touristAttractionMoreLoaded.value = 0
    }

    fun setRestaurantMoreLoadedDefault() {
        _restaurantMoreLoaded.value = 0
    }

    fun setCafeMoreLoadedDefault() {
        _cafeMoreLoaded.value = 0
    }

    fun setEventMoreLoadedDefault() {
        _eventMoreLoaded.value = 0
    }
}