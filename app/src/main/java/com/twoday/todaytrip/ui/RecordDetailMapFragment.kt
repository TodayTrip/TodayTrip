package com.twoday.todaytrip.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PolylineOverlay
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentRecordDetailMapBinding

class RecordDetailMapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentRecordDetailMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var naverMap: NaverMap
    private lateinit var mapView: MapView

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

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        // 마커 리스트 생성
        val markers = mutableListOf<Marker>()

        // 임의의 위치 데이터 목록
        val locations = listOf(
            LatLng(37.5158, 127.0734), // 서울 시청
            LatLng(37.5651, 126.9784), // 광화문
            LatLng(37.5641, 126.9980),  // 동대문
            LatLng(37.7914, 127.5259),
            LatLng(37.5130, 127.1020),
            LatLng(37.5668, 127.0096),
            LatLng(37.5779, 126.9770),
            LatLng(37.2942, 127.2024)
        )

        locations.forEach { latLng ->
            val marker = Marker().apply {
                position = latLng
                icon = OverlayImage.fromBitmap(resizeMapIcons(R.drawable.ic_marker, 100, 100))
                map = naverMap
            }
            markers.add(marker) // 마커 리스트에 추가
        }

        connectMarkersSequentiallyFromFurthest(naverMap, markers)

        // 카메라 위치와 줌 레벨 조정 (선택 사항)
        val cameraUpdate = CameraUpdate.scrollTo(locations.first()).animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)
        naverMap.moveCamera(CameraUpdate.zoomTo(10.0))
    }

    private fun resizeMapIcons(iconId: Int, width: Int, height: Int): Bitmap {
        val imageBitmap = BitmapFactory.decodeResource(resources, iconId)
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }

    // 지도에서 가장 멀리 있는 마커 구하는 함수
    private fun findFurthestMarkers(markers: List<Marker>): Pair<Marker, Marker>? {
        if (markers.size < 2) return null

        var furthestPair: Pair<Marker, Marker>? = null
        var longestDistance = 0.0

        markers.forEach { marker1 ->
            markers.forEach { marker2 ->
                val distance = marker1.position.distanceTo(marker2.position)
                if (distance > longestDistance) {
                    longestDistance = distance
                    furthestPair = Pair(marker1, marker2)
                }
            }
        }

        return furthestPair
    }

    // 마커끼리 폴리라인 연결하는 함수
    private fun connectMarkersSequentiallyFromFurthest(naverMap: NaverMap, markers: MutableList<Marker>) {
        val furthestPair = findFurthestMarkers(markers)
        furthestPair?.let {
            var currentMarker = it.first // 시작점으로 설정할 마커
            val connectedMarkers = mutableListOf(currentMarker)
            markers.remove(currentMarker)

            while (markers.isNotEmpty()) {
                val closestMarker = markers.minByOrNull { marker -> currentMarker.position.distanceTo(marker.position) }
                closestMarker?.let { marker ->
                    PolylineOverlay().apply {
                        coords = listOf(currentMarker.position, marker.position)
                        color = 0xFF0085FF.toInt()
                        map = naverMap
                    }
                    currentMarker = marker
                    connectedMarkers.add(marker)
                    markers.remove(marker)
                }
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