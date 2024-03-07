package com.twoday.todaytrip.ui.record_detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentRecordDetailMapBinding
import com.twoday.todaytrip.utils.MapUtils.combineImages
import com.twoday.todaytrip.utils.MapUtils.createBoundsForAllMarkers
import com.twoday.todaytrip.utils.MapUtils.drawPolyline
import com.twoday.todaytrip.utils.MapUtils.resizeMapIcons
import com.twoday.todaytrip.utils.MapUtils.updateCameraToBounds
import com.twoday.todaytrip.viewModel.RecordDetailViewModel

class RecordDetailMapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentRecordDetailMapBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this@RecordDetailMapFragment)[RecordDetailViewModel::class.java]
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
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRecordDetailMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // mapView 변수 초기화
        mapView = binding.mvRecordDetailFullMap
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }
    private fun observeFurthestPairAndConnectMarkers() {
        viewModel.findFurthestMarkers(markers) // LiveData를 업데이트하도록 요청
        viewModel.furthestPair.observe(viewLifecycleOwner, Observer { furthestPair ->
            furthestPair?.let {
                connectMarkersSequentiallyFromFurthest(naverMap, markers, it)
            }
        })
    }
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        val markerIconBitmap =
            resizeMapIcons(requireContext(), R.drawable.ic_marker, 120, 120)
        val photoBitmap =
            resizeMapIcons(requireContext(), R.drawable.img_pic_marker, 80, 80)
        val combinedBitmap = combineImages(markerIconBitmap, photoBitmap)

        locations.forEach { latLng ->
            Log.d("latLng", latLng.toString())
            val marker = Marker().apply {
                position = latLng
                icon = OverlayImage.fromBitmap(combinedBitmap)
                map = naverMap
            }
            markers.add(marker) // 마커 리스트에 추가
        }

        val bounds = createBoundsForAllMarkers(markers)

        // 마커를 추가 한 후 아래 함수를 호출해야 함
        observeFurthestPairAndConnectMarkers()
        updateCameraToBounds(naverMap, bounds, 200)
    }

    // 마커끼리 폴리라인 연결하는 함수
    private fun connectMarkersSequentiallyFromFurthest(naverMap: NaverMap, markers: MutableList<Marker>, furthestPair: Pair<Marker, Marker>) {
        var currentMarker = furthestPair.first // 시작점으로 설정할 마커
        val connectedMarkers = mutableListOf(currentMarker)
        markers.remove(currentMarker)

        while (markers.isNotEmpty()) {
            val closestMarker = markers.minByOrNull { marker -> currentMarker.position.distanceTo(marker.position) }
            closestMarker?.let { marker ->
                drawPolyline(naverMap, listOf(currentMarker.position, marker.position))
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

}