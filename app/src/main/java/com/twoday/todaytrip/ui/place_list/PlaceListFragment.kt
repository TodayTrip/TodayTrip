package com.twoday.todaytrip.ui.place_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.R
import com.twoday.todaytrip.place_list_adapter.PagerFragmentStateAdapter
import com.twoday.todaytrip.databinding.FragmentPlaceListBinding
import com.twoday.todaytrip.utils.DestinationPrefUtil
import com.twoday.todaytrip.utils.PrefConstants
//import com.twoday.todaytrip.utils.SharedPreferencesUtil
import com.twoday.todaytrip.weather.Item
import com.twoday.todaytrip.weather.WeatherClient
import com.twoday.todaytrip.weather.weather
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class PlaceListFragment : Fragment() {
    private var _binding: FragmentPlaceListBinding? = null
    private val binding get() = _binding!!

    private var base_date = "20240305"  // 발표 일자
    private var base_time = "0800"      // 발표 시각
    private var nx = "37"               // 예보지점 X 좌표
    private var ny = "127"              // 예보지점 Y 좌표

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
        searchArea()
//        weatherInfo()
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
        var cal = Calendar.getInstance()
        var timeToDate = cal.time
        val timeH = SimpleDateFormat("HH", Locale.getDefault()).format(timeToDate)

        base_time = getBaseTime(timeH)

        // 3시간 마다 불러올 수 있기 때문에 오전 12시나 1시 같은 경우는 하루 전날의 데이터를 써야함
        if (base_time.toInt() == 0) {
            cal.add(Calendar.DATE, -1)
            base_date.format(timeToDate)
            base_time = "2300"
        } else {
            base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(timeToDate)
            base_time = getBaseTime(timeH)
        }

        val weather = WeatherClient.weatherNetWork.getWeather(
            dataType = "JSON",
            numOfRows = 12,
            pageNo = 1,
            baseDate = base_date,
            baseTime = base_time,
            nx = nx,
            ny = ny
        )
        weather.enqueue(object : retrofit2.Callback<weather> {
            override fun onResponse(call: Call<weather>, response: Response<weather>) {
                if (response.isSuccessful) {
                    val it: List<Item> = response.body()!!.response.body.items.item
                    var temp = ""
                    var sky = ""
                    var rainType = ""

                    for (i in 0..11) {
                        when (it[i].category) {
                            "SKY" -> sky = it[i].fcstValue
                            "TMP" -> temp = it[i].fcstValue
                            "PTY" -> rainType = it[i].fcstValue
                            else -> continue
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
        // 강수 형태
        var result = ""
        when (rainType) {
            "0" -> binding.tvWeatherInfo2.visibility = View.GONE
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

        // 하늘 상태
        when (sky) {
            "1" -> binding.imgWeather.setImageResource(R.drawable.img_weather_sun)
            "3" -> binding.imgWeather.setImageResource(R.drawable.img_foggy)
            "4" -> binding.imgWeather.setImageResource(R.drawable.img_cloud)
            else -> binding.imgWeather.setImageResource(R.drawable.img_weather_sun)
        }
        // 온도
        binding.tvWeatherInfo.text = temp + "°"
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}