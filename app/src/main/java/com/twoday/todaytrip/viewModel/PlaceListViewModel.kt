package com.twoday.todaytrip.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.R
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.ui.place_list.RecommendCover
import com.twoday.todaytrip.ui.place_list.RecommendData
import com.twoday.todaytrip.ui.place_list.RecommendEmpty
import com.twoday.todaytrip.ui.place_list.RecommendTourItem
import com.twoday.todaytrip.utils.DestinationPrefUtil
import com.twoday.todaytrip.utils.RecommendPrefUtil
import com.twoday.todaytrip.utils.RecordPrefUtil
import com.twoday.todaytrip.weatherApi.Item
import com.twoday.todaytrip.weatherApi.WeatherClient
import com.twoday.todaytrip.weatherApi.weather
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class WeatherInfo(
    val sky: String,
    val temp: String,
    val result: String
)

class PlaceListViewModel : ViewModel() {

    private val _destination = MutableLiveData<String>()
    val destination: LiveData<String> get() = _destination
    private val _destinationSigungu = MutableLiveData<String>()
    val destinationSigungu: LiveData<String> get() = _destinationSigungu

    private val _weatherInfo = MutableLiveData<WeatherInfo>()
    val weatherInfo: LiveData<WeatherInfo> get() = _weatherInfo

    // 오늘의 랜덤 코스에 뜰 관광지 정보
    private val _recommendDataList = MutableLiveData<List<RecommendData>>()
    val recommendDataList: LiveData<List<RecommendData>> get() = _recommendDataList

    // 오늘 랜덤 코스에 뜰 관광지 정보 인덱스 상수
    private val RECOMMEND_INDEX_TOURIST_ATTRACTION = 1
    private val RECOMMEND_INDEX_RESTAURANT = 2
    private val RECOMMEND_INDEX_CAFE = 3
    private val RECOMMEND_INDEX_EVENT = 4

    init {
        initDestination()
        initWeatherInfo()
        initRecommendDataList()
    }

    private fun initDestination() {
        _destination.value = DestinationPrefUtil.loadDestination()!!
        _destinationSigungu.value = "전체" // TODO 스피너로 시군구 선택 구현
    }

    private fun initWeatherInfo() {
        val cal = Calendar.getInstance()
        val timeToDate = cal.time
        val timeH = SimpleDateFormat("HH", Locale.getDefault()).format(timeToDate)
        var baseTime = getBaseTime(timeH)
        var baseDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(timeToDate)

        val coordinates = getCoordinates(_destination.value!!)!!
        val latitude = coordinates.latitude
        val longitude = coordinates.longitude

        // 오전 12시나 1시인 경우 전날 데이터 사용
        if (baseTime.toInt() == 0) {
            cal.add(Calendar.DATE, -1)
            baseDate.format(timeToDate)
            baseTime = "2300"
        } else {
            baseDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(timeToDate)
            baseTime = getBaseTime(timeH)
        }

        WeatherClient.weatherNetWork.getWeather(
            dataType = "JSON",
            numOfRows = 12,
            pageNo = 1,
            baseDate = baseDate,
            baseTime = baseTime,
            nx = latitude,
            ny = longitude
        ).enqueue(object : retrofit2.Callback<weather> {
            override fun onResponse(call: Call<weather>, response: Response<weather>) {
                if (response.isSuccessful) {
                    val it: List<Item> = response.body()?.response?.body?.items?.item ?: return
                    var temp = ""
                    var sky = ""
                    var rainType = ""
                    it.forEach { item ->
                        when (item.category) {
                            "SKY" -> sky = item.fcstValue
                            "TMP" -> temp = item.fcstValue
                            "PTY" -> rainType = item.fcstValue
                        }
                    }
                    _weatherInfo.value = WeatherInfo(sky, temp, getWeatherResult(rainType))
                }
            }

            override fun onFailure(call: Call<weather>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })
    }

    private fun getWeatherResult(rainType: String) = when (rainType) {
        "0" -> ""
        "1" -> "비"
        "2" -> "비/눈"
        "3" -> "눈"
        "4" -> "소나기"
        "5" -> "빗방울"
        "6" -> "빗방울/눈날림"
        "7" -> "눈날림"
        else -> "error"
    }


    private fun getBaseTime(h: String): String = when {
        h.toInt() in 2..4 -> "0200"
        h.toInt() in 5..7 -> "0500"
        h.toInt() in 8..10 -> "0800"
        h.toInt() in 11..13 -> "1100"
        h.toInt() in 14..16 -> "1400"
        h.toInt() in 17..19 -> "1700"
        h.toInt() in 20..22 -> "2000"
        h.toInt() in 23..23 -> "2300"
        else -> "0000"
    }

    private data class Coordinates(
        val latitude: String,
        val longitude: String
    )

    private fun getCoordinates(destination: String): Coordinates? {
        return when (destination) {
            "서울" -> Coordinates("60", "127")
            "인천" -> Coordinates("55", "124")
            "전북" -> Coordinates("63", "89")
            "전남" -> Coordinates("51", "67")
            "경북" -> Coordinates("89", "91")
            "경남" -> Coordinates("91", "77")
            "충북" -> Coordinates("69", "107")
            "충남" -> Coordinates("68", "100")
            "강원" -> Coordinates("73", "134")
            "대구" -> Coordinates("89", "90")
            "부산" -> Coordinates("98", "76")
            "대전" -> Coordinates("67", "100")
            "제주" -> Coordinates("52", "38")
            "경기" -> Coordinates("60", "120")
            "광주" -> Coordinates("58", "74")
            "울산" -> Coordinates("102", "84")
            else -> null
        }
    }

    private fun initRecommendDataList() {
        _recommendDataList.value = listOf(
            RecommendCover(
                imageId = getTitleImageId(_destination.value!!)!!,
                destination = _destination.value!!,
                destinationSigungu = _destinationSigungu.value!!
            ),
            RecommendEmpty(
                subTitleId = R.string.place_list_recommend_sub_title_tourist_attraction,
                titleId = R.string.place_list_recommend_tourist_attraction_no_result
            ),
            RecommendEmpty(
                subTitleId = R.string.place_list_recommend_sub_title_restaurant,
                titleId = R.string.place_list_recommend_restaurant_no_result
            ),
            RecommendEmpty(
                subTitleId = R.string.place_list_recommend_sub_title_cafe,
                titleId = R.string.place_list_recommend_cafe_no_result
            ),
            RecommendEmpty(
                subTitleId = R.string.place_list_recommend_sub_title_event,
                titleId = R.string.place_list_recommend_event_no_result
            )
        )

        loadRecommendTouristAttraction()
        loadRecommendRestaurant()
        loadRecommendCafe()
        loadRecommendEvent()
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

    private fun  loadRecommendTouristAttraction(){
        RecommendPrefUtil.loadRecommendTouristAttraction()?.let{recommendTouristAttraction ->
            val newRecommendDataList = mutableListOf<RecommendData>().apply {
                addAll(_recommendDataList.value!!)
            }
            newRecommendDataList[RECOMMEND_INDEX_TOURIST_ATTRACTION] = RecommendTourItem(
                subTitleId = R.string.place_list_recommend_sub_title_tourist_attraction,
                tourItem = recommendTouristAttraction
            )
            _recommendDataList.value   = newRecommendDataList
        }
    }
    private fun  loadRecommendRestaurant(){
        RecommendPrefUtil.loadRecommendRestaurant()?.let{recommendRestaurant ->
            val newRecommendDataList = mutableListOf<RecommendData>().apply {
                addAll(_recommendDataList.value!!)
            }
            newRecommendDataList[RECOMMEND_INDEX_RESTAURANT] = RecommendTourItem(
                subTitleId = R.string.place_list_recommend_sub_title_restaurant,
                tourItem = recommendRestaurant
            )
            _recommendDataList.value   = newRecommendDataList
        }
    }
    private fun  loadRecommendCafe(){
        RecommendPrefUtil.loadRecommendCafe()?.let{recommendCafe ->
            val newRecommendDataList = mutableListOf<RecommendData>().apply {
                addAll(_recommendDataList.value!!)
            }
            newRecommendDataList[RECOMMEND_INDEX_CAFE] = RecommendTourItem(
                subTitleId = R.string.place_list_recommend_sub_title_cafe,
                tourItem = recommendCafe
            )
            _recommendDataList.value   = newRecommendDataList
        }
    }
    private fun  loadRecommendEvent(){
        RecommendPrefUtil.loadRecommendEvent()?.let{recommendEvent ->
            val newRecommendDataList = mutableListOf<RecommendData>().apply {
                addAll(_recommendDataList.value!!)
            }
            newRecommendDataList[RECOMMEND_INDEX_EVENT] = RecommendTourItem(
                subTitleId = R.string.place_list_recommend_sub_title_event,
                tourItem = recommendEvent
            )
            _recommendDataList.value   = newRecommendDataList
        }
    }

    fun pickAndSaveRecommendTouristAttraction(touristAttractionList: List<TourItem>){
        if(_recommendDataList.value!!.get(RECOMMEND_INDEX_TOURIST_ATTRACTION) is RecommendTourItem)
            return

        val recommendTouristAttraction = touristAttractionList.random()
        val newRecommendDataList = mutableListOf<RecommendData>().apply {
            addAll(_recommendDataList.value!!)
        }
        newRecommendDataList[RECOMMEND_INDEX_TOURIST_ATTRACTION] = RecommendTourItem(
            subTitleId = R.string.place_list_recommend_sub_title_tourist_attraction,
            tourItem = recommendTouristAttraction
        )
        _recommendDataList.value   = newRecommendDataList
        RecommendPrefUtil.saveRecommendTouristAttraction(recommendTouristAttraction)
    }
    fun pickAndSaveRecommendRestaurant(restaurantList: List<TourItem>){
        if(_recommendDataList.value!!.get(RECOMMEND_INDEX_RESTAURANT) is RecommendTourItem)
            return

        val recommendRestaurant = restaurantList.random()
        val newRecommendDataList = mutableListOf<RecommendData>().apply {
            addAll(_recommendDataList.value!!)
        }
        newRecommendDataList[RECOMMEND_INDEX_RESTAURANT] = RecommendTourItem(
            subTitleId = R.string.place_list_recommend_sub_title_restaurant,
            tourItem = recommendRestaurant
        )
        _recommendDataList.value   = newRecommendDataList
        RecommendPrefUtil.saveRecommendRestaurant(recommendRestaurant)
    }
    fun pickAndSaveRecommendCafe(cafeList: List<TourItem>){
        if(_recommendDataList.value!!.get(RECOMMEND_INDEX_CAFE) is RecommendTourItem)
            return

        val recommendCafe = cafeList.random()
        val newRecommendDataList = mutableListOf<RecommendData>().apply {
            addAll(_recommendDataList.value!!)
        }
        newRecommendDataList[RECOMMEND_INDEX_CAFE] = RecommendTourItem(
            subTitleId = R.string.place_list_recommend_sub_title_cafe,
            tourItem = recommendCafe
        )
        _recommendDataList.value   = newRecommendDataList
        RecommendPrefUtil.saveRecommendCafe(recommendCafe)
    }
    fun pickAndSaveRecommendEvent(eventList: List<TourItem>){
        if(_recommendDataList.value!!.get(RECOMMEND_INDEX_EVENT) is RecommendTourItem)
            return

        val recommendEvent = eventList.random()
        val newRecommendDataList = mutableListOf<RecommendData>().apply {
            addAll(_recommendDataList.value!!)
        }
        newRecommendDataList[RECOMMEND_INDEX_EVENT] = RecommendTourItem(
            subTitleId = R.string.place_list_recommend_sub_title_event,
            tourItem = recommendEvent
        )
        _recommendDataList.value   = newRecommendDataList
        RecommendPrefUtil.saveEventTouristAttraction(recommendEvent)
    }
}