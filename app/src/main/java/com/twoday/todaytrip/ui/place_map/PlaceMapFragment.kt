package com.twoday.todaytrip.ui.place_map

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.twoday.todaytrip.databinding.FragmentPlaceMapBinding
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

    // 임의의 위치 데이터 목록
    private val locations = listOf(
        LatLng(37.4979, 127.0276), // 서울 시청
        LatLng(37.5124, 127.0589), // 광화문
        LatLng(37.2942, 127.2024),  // 동대문
        LatLng(37.5273, 127.0390),
        LatLng(37.5195, 127.0378),
        LatLng(37.5089, 127.0468)
    )

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
    }

    private fun setupImageRecyclerView() {
        binding.rvPlaceMap.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = placeMapAdapter
        }
    }

    override fun onMapReady(naverMap: NaverMap) {

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