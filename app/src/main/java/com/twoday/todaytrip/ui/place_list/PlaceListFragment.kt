package com.twoday.todaytrip.ui.place_list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.twoday.todaytrip.R
import com.twoday.todaytrip.place_list_adapter.PagerFragmentStateAdapter
import com.twoday.todaytrip.databinding.FragmentPlaceListBinding
import com.twoday.todaytrip.utils.DestinationPrefUtil
//import com.twoday.todaytrip.utils.SharedPreferencesUtil
import com.twoday.todaytrip.weather.Item
import com.twoday.todaytrip.weather.WeatherClient
import com.twoday.todaytrip.weather.weather
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class PlaceListFragment : Fragment() {
    private val TAG = "PlaceListFragment"

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
        binding.ivLocal.setImageResource(coordinates.image)

        binding.ivLocal.setOnClickListener {
            val intent = Intent(context, FullScreenImageActivity::class.java)
            intent.putExtra("imageResource", coordinates.image)
            startActivity(intent)
        }

        // 오전 12시나 1시인 경우 전날 데이터 사용
        if (base_time.toInt() == 0) {
            cal.add(Calendar.DATE, -1)
            base_date.format(timeToDate)
            base_time = "2300"
        } else {
            base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(timeToDate)
            base_time = getBaseTime(timeH)
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
                    Log.d("iop", sky)
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
            "1" -> R.drawable.ic_place_list_weather_sun
            "3" -> R.drawable.ic_place_list_weather_foggy
            "4" -> R.drawable.ic_place_list_weather_cloud
            else -> R.drawable.ic_place_list_weather_sun
        }
        binding.imgWeather.setImageResource(skyImageResource)

        // 온도 설정
        binding.tvWeatherInfo.text = "$temp°"
    }

    private fun getBaseTime(h: String): String {
        var currentTime = ""

        currentTime = when {
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
        return currentTime
    }

    private fun searchArea(): String? {
        val area = DestinationPrefUtil.loadDestination()
        return area
    }

    private data class Coordinates(
        val latitude: String,
        val longitude: String,
        val name: String,
        val image: Int,
    )

    private fun localLocation(location: String): Coordinates? {
        return when (location) {
            "서울" -> Coordinates("60", "127", "서울", listOf(R.drawable.img_seoul1, R.drawable.img_seoul2, R.drawable.img_seoul3, R.drawable.img_seoul4).random())
            "인천" -> Coordinates("55", "124", "인천", listOf(R.drawable.img_incheon1, R.drawable.img_incheon2, R.drawable.img_incheon3).random())
            "전북" -> Coordinates("63", "89", "전라북도", listOf(R.drawable.img_jeonbuk1, R.drawable.img_jeonbuk2).random())
            "전남" -> Coordinates("51", "67", "전라남도", listOf(R.drawable.img_jeonnam1, R.drawable.img_jeonnam2, R.drawable.img_jeonnam3,R.drawable.img_jeonnam4,R.drawable.img_jeonnam5).random())
            "경북" -> Coordinates("89", "91", "경상북도", listOf(R.drawable.img_gyeongbuk1, R.drawable.img_gyeongbuk2, R.drawable.img_gyeongbuk3).random())
            "경남" -> Coordinates("91", "77", "경상남도", listOf(R.drawable.img_gyeongnam1, R.drawable.img_gyeongnam2, R.drawable.img_gyeongnam3).random())
            "충북" -> Coordinates("69", "107", "충청북도", listOf(R.drawable.img_chungbuk1, R.drawable.img_chungbuk2, R.drawable.img_chungbuk3,R.drawable.img_chungbuk4,R.drawable.img_chungbuk5).random())
            "충남" -> Coordinates("68", "100", "충청남도",listOf(R.drawable.img_chungnam1, R.drawable.img_chungnam2, R.drawable.img_chungnam3).random())
            "강원" -> Coordinates("73", "134", "강원도", listOf(R.drawable.img_gangwon1, R.drawable.img_gangwon2, R.drawable.img_gangwon3).random())
            "대구" -> Coordinates("89", "90", "대구", listOf(R.drawable.img_daegu1, R.drawable.img_daegu2, R.drawable.img_daegu3).random())
            "부산" -> Coordinates("98", "76", "부산", listOf(R.drawable.img_busan1, R.drawable.img_busan2, R.drawable.img_busan3, R.drawable.img_busan4).random())
            "대전" -> Coordinates("67", "100", "대전", listOf(R.drawable.img_seoul1).random())
            "제주" -> Coordinates("52", "38", "제주도", listOf(R.drawable.img_jeju1, R.drawable.img_jeju2, R.drawable.img_jeju3).random())
            "경기" -> Coordinates("60", "120", "경기도", listOf(R.drawable.img_gyeonggi1, R.drawable.img_gyeonggi2, R.drawable.img_gyeonggi3).random())
            "광주" -> Coordinates("58", "74", "광주", listOf(R.drawable.img_gwangju1, R.drawable.img_gwangju2, R.drawable.img_gwangju3).random())
            "울산" -> Coordinates("102", "84", "울산", listOf(R.drawable.img_ulsan1).random())
            else -> null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}