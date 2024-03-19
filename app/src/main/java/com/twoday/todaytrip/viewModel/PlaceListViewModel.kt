package com.twoday.todaytrip.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.R
import com.twoday.todaytrip.utils.DestinationPrefUtil
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

    private val _weatherInfo = MutableLiveData<WeatherInfo>()
    val weatherInfo: LiveData<WeatherInfo> get() = _weatherInfo

    init {
        initDestination()
        initWeatherInfo()
    }

    private fun initDestination() {
        _destination.value = DestinationPrefUtil.loadDestination()!!
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

}