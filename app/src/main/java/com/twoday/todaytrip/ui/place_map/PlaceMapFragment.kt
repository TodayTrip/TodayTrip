package com.twoday.todaytrip.ui.place_map

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentPlaceMapBinding
import com.twoday.todaytrip.utils.MapUtils.createBoundsForAllMarkers
import com.twoday.todaytrip.utils.MapUtils.resizeMapIcons
import com.twoday.todaytrip.utils.MapUtils.updateCameraToBounds
import com.twoday.todaytrip.utils.TourItemPrefUtil.loadCafeList
import com.twoday.todaytrip.utils.TourItemPrefUtil.loadEventList
import com.twoday.todaytrip.utils.TourItemPrefUtil.loadRestaurantList
import com.twoday.todaytrip.utils.TourItemPrefUtil.loadTouristAttractionList
import com.twoday.todaytrip.viewModel.PlaceMapViewModel

class PlaceMapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentPlaceMapBinding? = null
    private val binding get() = _binding!!
    private val placeMapAdapter by lazy { PlaceMapAdapter() }
    private val viewModel by lazy {
        ViewModelProvider(this@PlaceMapFragment)[PlaceMapViewModel::class.java]
    }
    private lateinit var naverMap: NaverMap
    private lateinit var mapView: MapView

    // 마커 리스트 생성
    private val markers = mutableListOf<Marker>()

    private var locations =
        loadTouristAttractionList().map {
            LatLng(it.getLatitude().toDouble(), it.getLongitude().toDouble())
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

        // mapView 변수 초기화
        mapView = binding.mvPlaceMap
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        setupImageRecyclerView()
        setupTabLayout()
    }

    private fun observeFurthestPairAndConnectMarkers() {
        viewModel.findFurthestMarkers(markers) // LiveData를 업데이트하도록 요청
    }

    private fun setupImageRecyclerView() {
        binding.rvPlaceMap.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = placeMapAdapter
        }
    }

    private fun setupTabLayout() {
        binding.tlTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        Log.d("locations0", locations.toString())
                        locations = emptyList()
                        locations =
                            loadTouristAttractionList().map {
                                LatLng(it.getLatitude().toDouble(), it.getLongitude().toDouble())
                            }
                        onMarkerReady()
                        placeMapAdapter.submitList(loadTouristAttractionList())
                    }

                    1 -> {
                        Log.d("locations1", locations.toString())
                        locations = emptyList()
                        locations =
                            loadRestaurantList().map {
                                LatLng(it.getLatitude().toDouble(), it.getLongitude().toDouble())
                            }
                        onMarkerReady()
                        placeMapAdapter.submitList(loadRestaurantList())
                    }

                    2 -> {
                        Log.d("locations2", locations.toString())
                        locations = emptyList()
                        locations =
                            loadCafeList().map {
                                LatLng(it.getLatitude().toDouble(), it.getLongitude().toDouble())
                            }
                        onMarkerReady()
                        placeMapAdapter.submitList(loadCafeList())
                    }

                    3 -> {
                        Log.d("locations3", locations.toString())
                        locations = emptyList()
                        locations =
                            loadEventList().map {
                                LatLng(it.getLatitude().toDouble(), it.getLongitude().toDouble())
                            }
                        onMarkerReady()
                        placeMapAdapter.submitList(loadEventList())
                    }

                    else -> placeMapAdapter.submitList(loadTouristAttractionList())
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

        val markerIconBitmap =
            resizeMapIcons(requireContext(), R.drawable.ic_marker, 120, 120)


        Log.d("onMarkerReady 안 locations", locations.toString())
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

        // 마커를 추가 한 후 아래 함수를 호출해야 함
        observeFurthestPairAndConnectMarkers()
        updateCameraToBounds(naverMap, bounds, 250)
    }

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