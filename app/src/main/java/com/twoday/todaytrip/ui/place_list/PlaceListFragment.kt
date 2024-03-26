package com.twoday.todaytrip.ui.place_list

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentPlaceListBinding
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.ui.MainActivity
import com.twoday.todaytrip.ui.place_detail.PlaceDetailActivity
import com.twoday.todaytrip.ui.place_list.adapter.OnAddAllRecommendClickListener
import com.twoday.todaytrip.ui.place_list.adapter.OnRefreshRecommendClickListener
import com.twoday.todaytrip.ui.place_list.adapter.OnTourItemClickListener
import com.twoday.todaytrip.ui.place_list.adapter.PagerFragmentStateAdapter
import com.twoday.todaytrip.ui.place_list.adapter.RecommendViewPagerAdapter
import com.twoday.todaytrip.viewModel.MainViewModel
import com.twoday.todaytrip.viewModel.PlaceListViewModel
import java.lang.ref.WeakReference

class PlaceListFragment : Fragment(),
    OnRefreshRecommendClickListener,
    OnTourItemClickListener,
    OnAddAllRecommendClickListener {
    private val TAG = "PlaceListFragment"

    private var _binding: FragmentPlaceListBinding? = null
    private val binding get() = _binding!!

    private val model by viewModels<PlaceListViewModel>()

    private val mainModel: MainViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                MainViewModel() as T
        }
    }

    private val recommendAdapter = RecommendViewPagerAdapter()

    private val myHandler = MyHandler(this@PlaceListFragment)
    private val intervalTime = 3000.toLong() // 몇초 간격으로 페이지를 넘길것인지 (1500 = 1.5초)

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
        initModelObserver()
        initMainModelObserver()
    }

    private fun initAdapter() {
        initRecommendAdapter()
        initMainAdapter()
    }

    private fun autoScrollStart(intervalTime: Long) {
        myHandler.removeMessages(0) // 이거 안하면 핸들러가 1개, 2개, 3개 ... n개 만큼 계속 늘어남
        myHandler.sendEmptyMessageDelayed(0, intervalTime) // intervalTime 만큼 반복해서 핸들러를 실행하게 함
    }

    private fun autoScrollStop() {
        myHandler.removeMessages(0) // 핸들러를 중지시킴
    }

    private class MyHandler(fragment: PlaceListFragment) : Handler() {
        private val fragmentWeakRef = WeakReference(fragment)
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            fragmentWeakRef.get()?.let { fragment ->
                if (msg.what == 0 && fragment.isAdded) {
                    fragment.binding.viewpagerRecommend.setCurrentItem(
                        fragment.model.recommendPosition.value!! + 1,
                        true
                    )
                    fragment.autoScrollStart(fragment.intervalTime)
                }
            }
        }
    }

    private fun initRecommendAdapter() {
        recommendAdapter.run {
            onRefreshRecommendClickListener = this@PlaceListFragment
            onTourItemClickListener = this@PlaceListFragment
            onAddAllRecommendClickListener = this@PlaceListFragment
        }
        binding.viewpagerRecommend.run {
            adapter = recommendAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL

            setCurrentItem(model.recommendPosition.value!!, false)
        }
        binding.viewpagerRecommend.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                when (state) {
                    ViewPager2.SCROLL_STATE_IDLE -> autoScrollStart(intervalTime)
                    ViewPager2.SCROLL_STATE_DRAGGING -> autoScrollStop()
                }
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d(TAG, "position % 6  = ${position % 6}")
                model.setRecommendPosition(position)
            }
        })
    }

    private fun setRecommendPageIndicator(position: Int) {
        val indicatorList =
            listOf(
                binding.placeIndicator1,
                binding.placeIndicator2,
                binding.placeIndicator3,
                binding.placeIndicator4,
                binding.placeIndicator5,
                binding.placeIndicator6
            )
        indicatorList.forEach {
            it.setBackgroundResource(R.drawable.shape_circle_gray)
        }
        indicatorList[position % 6].setBackgroundResource(R.drawable.shape_circle_blue)
    }

    override fun onRefreshRecommendClick() {
        model.refreshRecommendList()
        model.pickAndSaveRecommendTouristAttraction(mainModel.touristAttractionList.value)
        model.pickAndSaveRecommendRestaurant(mainModel.restaurantList.value)
        model.pickAndSaveRecommendCafe(mainModel.cafeList.value)
        model.pickAndSaveRecommendEvent(mainModel.eventList.value)

        Toast.makeText(
            requireActivity(),
            R.string.place_list_recommend_refresh_toast,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onTourItemClick(tourItem: TourItem) {
        Log.d(TAG, "onTourItemClick) called, ${tourItem.getTitle()}")
        val placeDetailIntent = PlaceDetailActivity.newIntent(
            requireContext(),
            tourItem.getContentTypeId(),
            tourItem
        )
        startActivity(placeDetailIntent)
    }

    override fun onAddAllRecommendClick(optimizedOrder: List<Int>) {
        Toast.makeText(
            requireActivity(),
            R.string.place_list_recommend_add_all_toast,
            Toast.LENGTH_SHORT
        ).show()
        model.addAllRecommend(optimizedOrder)

        mainModel.loadOrFetchTouristAttractionList()
        mainModel.loadOrFetchRestaurantList()
        mainModel.loadOrFetchCafeList()
        mainModel.loadOrFetchEventList()
    }

    override fun onMoveToRouteClick() {
        (requireActivity() as MainActivity).moveToRouteFragment()
    }

    private fun initMainAdapter() {
        val mainViewPagerAdapter = PagerFragmentStateAdapter(requireActivity())
        with(mainViewPagerAdapter) {
            addFragment(TouristAttractionRecyclerViewFragment())
            addFragment(RestaurantRecyclerViewFragment())
            addFragment(CafeRecyclerViewFragment())
            addFragment(EventRecyclerViewFragment())
        }
        binding.vpViewpagerMain.adapter = mainViewPagerAdapter
        binding.vpViewpagerMain.isUserInputEnabled = false

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

    private fun initModelObserver() {
        model.themeTitleInfo.observe(viewLifecycleOwner) { themeTitleInfo ->
            if (themeTitleInfo.first == -1) {
                binding.tvTravelTheme.isVisible = false
            } else {
                binding.tvTravelTheme.run {
                    isVisible = true
                    setText(themeTitleInfo.first)
                    setTextColor(resources.getColor(themeTitleInfo.second))
                }
            }
        }
        model.destination.observe(viewLifecycleOwner) { destination ->
            binding.tvTravelDestination.text = destination
        }

        model.weatherInfo.observe(viewLifecycleOwner) { weatherInfo ->
            try {
                val skyImageResource = when (weatherInfo.sky) {
                    "1" -> R.drawable.ic_place_list_weather_sun
                    "3" -> R.drawable.ic_place_list_weather_foggy
                    "4" -> R.drawable.ic_place_list_weather_cloud
                    else -> R.drawable.ic_place_list_weather_sun
                }
                with(binding) {
                    imgWeather.setImageResource(skyImageResource)
                    tvWeatherInfo.text = weatherInfo.temp
                    tvWeatherInfo2.text = weatherInfo.result
                }
            } catch (e: Exception) {
                e.stackTrace
            }
        }
        model.recommendDataList.observe(viewLifecycleOwner) { recommendDataList ->
            if (recommendDataList.isNotEmpty()) {
                (recommendDataList.last() as RecommendMap).run {
                    optimizedLocations = model.getOptimizedLocations()
                    optimizedOrder = model.getOptimizedOrder()
                }
            }
            recommendAdapter.changeDataSet(recommendDataList)
        }

        model.recommendPosition.observe(viewLifecycleOwner) { recommendPosition ->
            setRecommendPageIndicator(recommendPosition)
        }

        model.isAllRecommendAdded.observe(viewLifecycleOwner) { isAllRecommendAdded ->
            val recommendDataList = recommendAdapter.getDataSet()
            if (recommendDataList.isNotEmpty()) {
                (recommendDataList.last() as RecommendMap).isAllAdded = isAllRecommendAdded
            }
            recommendAdapter.changeDataSet(recommendDataList, 5)
        }
    }

    private fun initMainModelObserver() {
        mainModel.touristAttractionList.observe(viewLifecycleOwner) {
            model.pickAndSaveRecommendTouristAttraction(it)
        }
        mainModel.restaurantList.observe(viewLifecycleOwner) {
            model.pickAndSaveRecommendRestaurant(it)
        }
        mainModel.cafeList.observe(viewLifecycleOwner) {
            model.pickAndSaveRecommendCafe(it)
        }
        mainModel.eventList.observe(viewLifecycleOwner) {
            model.pickAndSaveRecommendEvent(it)
        }
    }

    override fun onResume() {
        super.onResume()
        model.setIsAllRecommendAdded()
        autoScrollStart(intervalTime)
    }

    override fun onPause() {
        super.onPause()
        autoScrollStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}