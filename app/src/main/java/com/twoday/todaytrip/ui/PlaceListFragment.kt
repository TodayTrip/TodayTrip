package com.twoday.todaytrip.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.twoday.todaytrip.R
import com.twoday.todaytrip.adapter.PagerFragmentStateAdapter
import com.twoday.todaytrip.databinding.FragmentPlaceListBinding
import com.twoday.todaytrip.ui.place_map.PlaceMapActivity
import com.twoday.todaytrip.weather.Item
import com.twoday.todaytrip.weather.WeatherClient
import com.twoday.todaytrip.weather.weather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.await


class PlaceListFragment : Fragment() {
    private var _binding: FragmentPlaceListBinding? = null
    private val binding get() = _binding!!
    private lateinit var ViewPagerAdapter: PagerFragmentStateAdapter

    private var base_date = "20240229"  // 발표 일자
    private var base_time = "0800"      // 발표 시각
    private var nx = "55"               // 예보지점 X 좌표
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
        CoroutineScope(Dispatchers.IO).launch {
            val weather = WeatherClient.weatherNetWork.getWeather(
                dataType = "JSON",
                numOfRows = 20,
                pageNo = 1,
                baseDate = base_date,
                baseTime = base_time,
                nx = nx,
                ny = ny
            )

            val it: List<Item> = weather.response.body.items.item

//            it.filter { it.category == "SKY" }.forEach {
//                it.fcstValue
//            }
             it.filter { it.category == "T3H" }.forEach {
                it.fcstValue

                 val temp = binding.tvWeatherInfo
                 temp.text = it.fcstValue
                Log.d("asd",it.fcstValue) }

        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}