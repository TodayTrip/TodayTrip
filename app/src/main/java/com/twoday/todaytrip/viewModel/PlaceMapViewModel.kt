package com.twoday.todaytrip.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.twoday.todaytrip.utils.TourItemPrefUtil

class PlaceMapViewModel : ViewModel() {

    // 가장 먼 거리의 마커쌍을 저장하는 라이브 데이터
    private val _furthestPair = MutableLiveData<Pair<Marker, Marker>?>()
    val furthestPair: LiveData<Pair<Marker, Marker>?>
        get() {
            return _furthestPair
        }
    private val _locations = MutableLiveData<List<LatLng>>()
    val locations: LiveData<List<LatLng>> = _locations

    // 지도에서 가장 멀리 있는 마커 구하는 함수(최적 경로 구할 때 사용)
    fun findFurthestMarkers(markers: List<Marker>) {
        if (markers.size < 2) {
            _furthestPair.value = null
            return
        }

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
        _furthestPair.value = furthestPair
    }

    // 탭 레이아웃 클릭 시 각 관광지 리스트에 맞는 위도, 경도
    fun onTabSeleted(position: Int) {
        when (position) {
            0 -> loadTouristAttractionList()
            1 -> loadRestaurantList()
            2 -> loadCafeList()
            3 -> loadEventList()
        }
    }
    private fun loadTouristAttractionList() {
        val attractions = TourItemPrefUtil.loadTouristAttractionList()
        val newLocations = attractions.map {
            LatLng(it.getLatitude().toDouble(), it.getLongitude().toDouble())
        }
        _locations.value = newLocations
    }
    private fun loadRestaurantList() {
        val attractions = TourItemPrefUtil.loadRestaurantList()
        val newLocations = attractions.map {
            LatLng(it.getLatitude().toDouble(), it.getLongitude().toDouble())
        }
        _locations.value = newLocations
    }
    private fun loadCafeList() {
        val attractions = TourItemPrefUtil.loadCafeList()
        val newLocations = attractions.map {
            LatLng(it.getLatitude().toDouble(), it.getLongitude().toDouble())
        }
        _locations.value = newLocations
    }
    private fun loadEventList() {
        val attractions = TourItemPrefUtil.loadEventList()
        val newLocations = attractions.map {
            LatLng(it.getLatitude().toDouble(), it.getLongitude().toDouble())
        }
        _locations.value = newLocations
    }

}