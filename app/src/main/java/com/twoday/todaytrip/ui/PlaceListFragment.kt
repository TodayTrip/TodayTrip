package com.twoday.todaytrip.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.twoday.todaytrip.R
import com.twoday.todaytrip.adapter.PagerFragmentStateAdapter
import com.twoday.todaytrip.databinding.FragmentPlaceListBinding
import com.twoday.todaytrip.weather.Item
import com.twoday.todaytrip.weather.WeatherClient
import com.twoday.todaytrip.weather.weather
import retrofit2.Call
import retrofit2.Response


class PlaceListFragment : Fragment() {
    private var _binding: FragmentPlaceListBinding? = null
    private val binding get() = _binding!!

    private lateinit var ViewPagerAdapter: PagerFragmentStateAdapter

    private var base_date = "20240229"  // 발표 일자
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
        weatherInfo()
    }

    private fun initAdapter() {

        ViewPagerAdapter = PagerFragmentStateAdapter(requireActivity())

        with(ViewPagerAdapter) {
            addFragment(FirstRecyclerViewFragment())
            addFragment(SecondRecyclerViewFragment())
            addFragment(ThirdRecyclerViewFragment())
        }

        binding.vpViewpagerMain.adapter = ViewPagerAdapter
        setUpClickListener()
    }

    private fun setUpClickListener() {

    }

    fun setWeather(rainType: String, sky: String, temp: String) {
        // 강수 형태
        var result = ""
        when (rainType) {
            "0" -> result = "맑음/흐림"
            "1" -> result = "비"
            "2" -> result = "비/눈"
            "3" -> result = "눈"
            "4" -> result = "소나기"
            "5" -> result = "빗방울"
            "6" -> result = "빗방울/눈날림"
            "7" -> result = "눈날림"
            else -> "오류"
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

    private fun weatherInfo() {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}