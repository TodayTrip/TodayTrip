//package com.twoday.todaytrip.ui.place_list
//
//import android.util.Log
//import android.view.View
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.twoday.todaytrip.R
//import com.twoday.todaytrip.utils.DestinationPrefUtil
//import com.twoday.todaytrip.weather.Item
//import com.twoday.todaytrip.weather.WeatherClient
//import com.twoday.todaytrip.weather.weather
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.text.SimpleDateFormat
//import java.util.Calendar
//import java.util.Locale
//
//class PlaceListViewModel : ViewModel() {
//    private val _weatherInfo = MutableLiveData<WeatherInfo>()
//    val weatherInfo: LiveData<WeatherInfo>
//        get() = _weatherInfo
//
//    private val _locationInfo = MutableLiveData<LocationInfo>()
//    val locationInfo: LiveData<LocationInfo>
//        get() = _locationInfo
//
//    init {
//        fetchWeatherInfo()
//    }
//
//    private fun fetchWeatherInfo() {
//        val cal = Calendar.getInstance()
//        val timeToDate = cal.time
//        val timeH = SimpleDateFormat("HH", Locale.getDefault()).format(timeToDate)
//        var baseTime = getBaseTime(timeH)
//        var baseDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(timeToDate)
//        val coordinates = localLocation(searchArea().toString())
//
//        // 오전 12시나 1시인 경우 전날 데이터 사용
//        if (baseTime == "0000") {
//            cal.add(Calendar.DATE, -1)
//            baseDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
//            baseTime = "2300"
//        }
//
//        coordinates?.let {
//            _locationInfo.value = it
//            WeatherClient.weatherNetWork.getWeather(
//                dataType = "JSON",
//                numOfRows = 12,
//                pageNo = 1,
//                baseDate = baseDate,
//                baseTime = baseTime,
//                nx = it.latitude,
//                ny = it.longitude
//            ).enqueue(object : Callback<weather> {
//                override fun onResponse(call: Call<weather>, response: Response<weather>) {
//                    if (response.isSuccessful) {
//                        val it: List<Item> = response.body()?.response?.body?.items?.item ?: return
//                        var temp = ""
//                        var sky = ""
//                        var rainType = ""
//
//                        it.forEach { item ->
//                            when (item.category) {
//                                "SKY" -> sky = item.fcstValue
//                                "TMP" -> temp = item.fcstValue
//                                "PTY" -> rainType = item.fcstValue
//                            }
//                        }
//                        _weatherInfo.value = WeatherInfo(temp, sky, rainType)
//                    }
//                }
//
//                override fun onFailure(call: Call<weather>, t: Throwable) {
//                    Log.d("api fail", t.message.toString())
//                }
//            })
//        }
//    }
//
//    private fun getBaseTime(h: String): String {
//        return when (h.toInt()) {
//            in 0..1 -> "0000"
//            in 2..4 -> "0200"
//            in 5..7 -> "0500"
//            in 8..10 -> "0800"
//            in 11..13 -> "1100"
//            in 14..16 -> "1400"
//            in 17..19 -> "1700"
//            in 20..22 -> "2000"
//            23 -> "2300"
//            else -> "0000"
//        }
//    }
//
//    private fun searchArea(): String? {
//        return DestinationPrefUtil.loadDestination()
//    }
//
//    private fun localLocation(location: String): LocationInfo? {
//        return when (location) {
//            "서울" -> LocationInfo("37.5665", "126.9780", "서울")
//            "인천" -> LocationInfo("37.4561", "126.7059", "인천")
//            "전북" -> LocationInfo("35.8201", "127.1091", "전라북도")
//            "전남" -> LocationInfo("34.8159", "126.4628", "전라남도")
//            "경북" -> LocationInfo("36.5758", "128.5060", "경상북도")
//            "경남" -> LocationInfo("35.2373", "128.6920", "경상남도")
//            "충북" -> LocationInfo("36.6357", "127.4913", "충청북도")
//            "충남" -> LocationInfo("36.6584", "126.6722", "충청남도")
//            "강원" -> LocationInfo("37.8228", "128.1555", "강원도")
//            "대구" -> LocationInfo("35.8714", "128.6014", "대구")
//            "부산" -> LocationInfo("35.1796", "129.0756", "부산")
//            "대전" -> LocationInfo("36.3504", "127.3845", "대전")
//            "제주" -> LocationInfo("33.4890", "126.4983", "제주도")
//            "경기" -> LocationInfo("37.4138", "127.5183", "경기도")
//            "광주" -> LocationInfo("35.1595", "126.8526", "광주")
//            "울산" -> LocationInfo("35.5384", "129.3114", "울산")
//            else -> null
//        }
//    }
//
//    private fun setWeather(temp: String, sky: String, rainType: String) {
//        // 강수 형태 설정
//        when (rainType) {
//            "0" -> View.GONE
//            "1" -> "비"
//            "2" -> "비/눈"
//            "3" -> "눈"
//            "4" -> "소나기"
//            "5" -> "빗방울"
//            "6" -> "빗방울/눈날림"
//            "7" -> "눈날림"
//            else -> "error"
//        }
//
//
//        // 하늘 상태 설정
//        val skyImageResource = when (sky) {
//            "1" -> R.drawable.img_weather_sun
//            "3" -> R.drawable.img_foggy
//            "4" -> R.drawable.img_cloud
//            else -> R.drawable.img_weather_sun
//        }
//
//
//    }
//}
//
//data class WeatherInfo(val temperature: String, val sky: String, val rainType: String)
//data class LocationInfo(val latitude: String, val longitude: String, val name: String)