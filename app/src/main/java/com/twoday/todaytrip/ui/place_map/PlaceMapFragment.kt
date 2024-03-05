package com.twoday.todaytrip.ui.place_map

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.twoday.todaytrip.databinding.FragmentPlaceMapBinding
import com.twoday.todaytrip.utils.MapUtils
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

    private val locations by lazy { createLocationsList() }

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
        setupLatLng()
    }
    private fun observeFurthestPairAndConnectMarkers() {
        viewModel.findFurthestMarkers(markers) // LiveData를 업데이트하도록 요청
        viewModel.furthestPair.observe(viewLifecycleOwner, Observer { furthestPair ->
            furthestPair?.let {
                connectMarkersSequentiallyFromFurthest(naverMap, markers, it)
            }
        })
    }
    private fun setupLatLng() {
//        loadTouristAttractionList()[0].getLongitude()
//        loadTouristAttractionList()[0].getLatitude()
//        loadTouristAttractionList().forEach {
//
//        }
    }
    private fun createLocationsList(): List<LatLng> {
        Log.d("경도위도", locations.toString())
        return loadTouristAttractionList().map {
            LatLng(it.getLatitude().toDouble(), it.getLongitude().toDouble())
        }
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
                    0 -> placeMapAdapter.submitList(loadTouristAttractionList())
                    1 -> placeMapAdapter.submitList(loadRestaurantList())
                    2 -> placeMapAdapter.submitList(loadCafeList())
                    3 -> placeMapAdapter.submitList(loadEventList())
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

    }

    // 마커끼리 폴리라인 연결하는 함수
    private fun connectMarkersSequentiallyFromFurthest(naverMap: NaverMap, markers: MutableList<Marker>, furthestPair: Pair<Marker, Marker>) {
        var currentMarker = furthestPair.first // 시작점으로 설정할 마커
        val connectedMarkers = mutableListOf(currentMarker)
        markers.remove(currentMarker)

        while (markers.isNotEmpty()) {
            val closestMarker = markers.minByOrNull { marker -> currentMarker.position.distanceTo(marker.position) }
            closestMarker?.let { marker ->
                MapUtils.drawPolyline(naverMap, listOf(currentMarker.position, marker.position))
                currentMarker = marker
                connectedMarkers.add(marker)
                markers.remove(marker)
            }
        }
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}