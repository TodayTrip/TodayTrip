package com.twoday.todaytrip.ui.place_list

//import com.twoday.todaytrip.utils.SharedPreferencesUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentPlaceListBinding
import com.twoday.todaytrip.ui.place_list.adapter.PagerFragmentStateAdapter
import com.twoday.todaytrip.ui.place_list.adapter.ViewPagerAdapter
import com.twoday.todaytrip.viewModel.PlaceListViewModel


class PlaceListFragment : Fragment() {
    private val TAG = "PlaceListFragment"

    private var _binding: FragmentPlaceListBinding? = null
    private val binding get() = _binding!!

    private val model by lazy {
        ViewModelProvider(this@PlaceListFragment)[PlaceListViewModel::class.java]
    }

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
    }

    private fun initAdapter() {
        initRecommendAdapter()
        initMainAdapter()
    }

    private fun initRecommendAdapter() {
        val recommendAdapter = ViewPagerAdapter()
        binding.viewpagerRecommend.adapter = recommendAdapter
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
        model.destination.observe(viewLifecycleOwner) { destination ->
            binding.tvTravelAddress.text = destination
        }
        model.titleImageId.observe(viewLifecycleOwner) {
//        binding.ivLocal.setImageResource(coordinates?.image!!)
//        binding.ivLocal.setOnClickListener {
//            val intent = Intent(context, FullScreenImageActivity::class.java)
//            intent.putExtra("imageResource", coordinates.image)
//            startActivity(intent)
//        }
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}