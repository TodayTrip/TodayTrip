package com.twoday.todaytrip.ui.place_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.R
import com.twoday.todaytrip.place_list_adapter.PagerFragmentStateAdapter
import com.twoday.todaytrip.databinding.FragmentPlaceListBinding
import com.twoday.todaytrip.utils.DestinationPrefUtil
import com.twoday.todaytrip.utils.PrefConstants
import com.twoday.todaytrip.weather.Item
import com.twoday.todaytrip.weather.WeatherClient
import com.twoday.todaytrip.weather.weather
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class PlaceListFragment : Fragment() {
    private var _binding: FragmentPlaceListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPlaceListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        weatherInfo()

    }

    private fun initAdapter() {
        val viewPagerAdapter = PagerFragmentStateAdapter(requireActivity())
        with(viewPagerAdapter) {
            addFragment(TouristAttractionRecyclerViewFragment())
            addFragment(RestaurantRecyclerViewFragment())
            addFragment(CafeRecyclerViewFragment())
            addFragment(EventRecyclerViewFragment())
        }
        binding.vpViewpagerMain.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tlTabLayout, binding.vpViewpagerMain) { tab, position ->
            tab.text = resources.getText(
                when (position) {
                    0 -> R.string.place_list_tourist_attraction
                    1 -> R.string.place_list_restaurant
                    2 -> R.string.place_list_cafe
                    3 -> R.string.place_list_event
                    else -> R.string.place_list_tourist_attraction // not reached
                }
            )
        }.attach()
    }

    private fun weatherInfo() {
        val cal = Calendar.getInstance()
        val timeToDate = cal.time
        val timeH = SimpleDateFormat("HH", Locale.getDefault()).format(timeToDate)
        var base_time = getBaseTime(timeH)
        var base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(timeToDate)
        val coordinates = localLocation(searchArea().toString())!!
        val latitude = coordinates.latitude
        val longitude = coordinates.longitude

        binding.tvTravelAddress.text = coordinates.name

        // 오전 12시나 1시인 경우 전날 데이터 사용
        if (base_time == "0000") {
            cal.add(Calendar.DATE, -1)
            base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
            base_time = "2300"
        }

        WeatherClient.weatherNetWork.getWeather(
            dataType = "JSON",
            numOfRows = 12,
            pageNo = 1,
            baseDate = base_date,
            baseTime = base_time,
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
                    setWeather(rainType, sky, temp)
                }
            }

            override fun onFailure(call: Call<weather>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })
    }

    private fun setWeather(rainType: String, sky: String, temp: String) {
        // 강수 형태 설정
        var result = ""
        when (rainType) {
            "0" -> View.GONE
            "1" -> result = "비"
            "2" -> result = "비/눈"
            "3" -> result = "눈"
            "4" -> result = "소나기"
            "5" -> result = "빗방울"
            "6" -> result = "빗방울/눈날림"
            "7" -> result = "눈날림"
            else -> result = "error"
        }
        binding.tvWeatherInfo2.text = result

        // 하늘 상태 설정
        val skyImageResource = when (sky) {
            "1" -> R.drawable.img_weather_sun
            "3" -> R.drawable.img_foggy
            "4" -> R.drawable.img_cloud
            else -> R.drawable.img_weather_sun
        }
        binding.imgWeather.setImageResource(skyImageResource)

        // 온도 설정
        binding.tvWeatherInfo.text = "$temp°"
    }

    private fun getBaseTime(h: String): String {
        return when (h.toInt()) {
            in 0..1 -> "0000"
            in 2..4 -> "0200"
            in 5..7 -> "0500"
            in 8..10 -> "0800"
            in 11..13 -> "1100"
            in 14..16 -> "1400"
            in 17..19 -> "1700"
            in 20..22 -> "2000"
            23 -> "2300"
            else -> "0000"
        }
    }

    private fun searchArea(): String? {
        val area = DestinationPrefUtil.loadDestination()
        return area
    }

    private data class Coordinates(val latitude: String, val longitude: String, val name:String)

    private fun localLocation(location: String): Coordinates? {
        return when (location) {
            "서울" -> Coordinates("37.5665", "126.9780", "서울")
            "인천" -> Coordinates("37.4561", "126.7059", "인천")
            "전북" -> Coordinates("35.8201", "127.1091", "전라북도")
            "전남" -> Coordinates("34.8159", "126.4628", "전라남도")
            "경북" -> Coordinates("36.5758", "128.5060", "경상북도")
            "경남" -> Coordinates("35.2373", "128.6920", "경상남도")
            "충북" -> Coordinates("36.6357", "127.4913", "충청북도")
            "충남" -> Coordinates("36.6584", "126.6722", "충청남도")
            "강원" -> Coordinates("37.8228", "128.1555", "강원도")
            "대구" -> Coordinates("35.8714", "128.6014", "대구")
            "부산" -> Coordinates("35.1796", "129.0756", "부산")
            "대전" -> Coordinates("36.3504", "127.3845", "대전")
            "제주" -> Coordinates("33.4890", "126.4983", "제주도")
            "경기" -> Coordinates("37.4138", "127.5183", "경기도")
            "광주" -> Coordinates("35.1595", "126.8526", "광주")
            "울산" -> Coordinates("35.5384", "129.3114", "울산")
            else -> null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}