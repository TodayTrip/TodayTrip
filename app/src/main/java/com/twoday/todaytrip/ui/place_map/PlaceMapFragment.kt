package com.twoday.todaytrip.ui.place_map

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.transition.TransitionInflater
import com.google.android.material.tabs.TabLayout
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentPlaceMapBinding
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.ui.place_detail.PlaceDetailActivity
import com.twoday.todaytrip.utils.MapUtils.createBoundsForAllMarkers
import com.twoday.todaytrip.utils.MapUtils.resizeMapIcons
import com.twoday.todaytrip.utils.MapUtils.updateCameraToBounds
import com.twoday.todaytrip.utils.TourItemPrefUtil.loadCafeList
import com.twoday.todaytrip.utils.TourItemPrefUtil.loadEventList
import com.twoday.todaytrip.utils.TourItemPrefUtil.loadRestaurantList
import com.twoday.todaytrip.utils.TourItemPrefUtil.loadTouristAttractionList
import com.twoday.todaytrip.viewModel.PlaceMapViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class PlaceMapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentPlaceMapBinding? = null
    private val binding get() = _binding!!
    private val placeMapAdapter by lazy { PlaceMapAdapter(onItemClick) }
    private val viewModel by lazy {
        ViewModelProvider(this@PlaceMapFragment)[PlaceMapViewModel::class.java]
    }
    private val pagerSnapHelper = PagerSnapHelper()
    private lateinit var naverMap: NaverMap
    private lateinit var mapView: MapView

    private val markers = mutableListOf<Marker>()

    private var locations =
        loadTouristAttractionList().map {
            LatLng(it.getLatitude().toDouble(), it.getLongitude().toDouble())
        }

    private val onItemClick: (TourItem) -> Unit = { tourItem ->
        val placeDetailIntent = PlaceDetailActivity.newIntent(
            requireContext(),
            tourItem.getContentTypeId(),
            tourItem)
        startActivity(placeDetailIntent)
        requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaceMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = binding.mvPlaceMap
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        initImageRecyclerView()
        initTabLayout()
        initModelObserver()
    }

    private fun initModelObserver() {
        viewModel.locations.observe(viewLifecycleOwner, Observer {
            locations = it
        })
    }

    private fun initImageRecyclerView() {
        pagerSnapHelper.attachToRecyclerView(binding.rvPlaceMap)
        binding.rvPlaceMap.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = placeMapAdapter
        }
    }

    private fun initTabLayout() {
        binding.tlTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewModel.onTabSeleted(tab.position)
                when (tab.position) {
                    0 -> {
                        onMarkerReady()
                        placeMapAdapter.submitList(loadTouristAttractionList())
                    }
                    1 -> {
                        onMarkerReady()
                        placeMapAdapter.submitList(loadRestaurantList())
                    }
                    2 -> {
                        onMarkerReady()
                        placeMapAdapter.submitList(loadCafeList())
                    }
                    3 -> {
                        onMarkerReady()
                        placeMapAdapter.submitList(loadEventList())
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // 탭이 선택 해제될 때 호출
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // 이미 선택된 탭이 다시 선택될 때 호출
            }
        })
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        onMarkerReady()
    }

    private fun onMarkerReady() {
        clearMarkers()

        if (locations.isNotEmpty()) {
            val markerIconBitmap =
                resizeMapIcons(requireContext(), R.drawable.ic_marker, 120, 120)

            locations.forEach { latLng ->
                Log.d("latLng", latLng.toString())
                val marker = Marker().apply {
                    position = latLng
                    icon = OverlayImage.fromBitmap(markerIconBitmap)
                    map = naverMap
                }
                markers.add(marker) // 마커 리스트에 추가
            }

            Log.d("markers", markers.size.toString())

            val bounds = createBoundsForAllMarkers(markers)

            updateCameraToBounds(naverMap, bounds, 250)
        }
    }

    //TODO ViewModel로 옮기려고 하는데 마커를 지도에서 제거하는 건 View에서 없애는 거니까 옮기면 안될까요?
    private fun clearMarkers() {
        markers.forEach { marker ->
            marker.map = null // 마커를 지도에서 제거
        }
        markers.clear() // 마커 리스트 비우기
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
        _binding = null
    }
}