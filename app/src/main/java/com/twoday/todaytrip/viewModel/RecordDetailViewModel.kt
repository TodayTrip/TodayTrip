package com.twoday.todaytrip.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.twoday.todaytrip.ui.save_photo.SavePhotoData

class RecordDetailViewModel : ViewModel() {
    private val _isOptionMap: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isOptionMap: LiveData<Boolean>
        get() = _isOptionMap

    // 가장 먼 거리의 마커 쌍을 저장하는 라이브 데이터
    private val _furthestPair = MutableLiveData<Pair<Marker, Marker>?>()
    val furthestPair: LiveData<Pair<Marker, Marker>?>
        get() {
            Log.d("furthestPair", "RecordDetailViewModel에서 furthestPair 라이브데이터")
            return _furthestPair
        }

    private val _marker = MutableLiveData<List<LatLng>>()
    val marker: LiveData<List<LatLng>> = _marker

    init {
        _isOptionMap.value = true
    }

    fun toggleIsOptionMap() {
        _isOptionMap.value = !(_isOptionMap.value!!)
    }

    // 지도에서 가장 멀리 있는 마커 구하는 함수
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

    fun setMarkersFromSavePhotoDataList(savePhotoDataList: List<SavePhotoData>?) {
        Log.d("TAG1 RecordDetailViewModel", "${savePhotoDataList?.size}")
        val latLngList = mutableListOf<LatLng>()
        savePhotoDataList?.forEach { savePhotoData ->
            latLngList.add(
                LatLng(
                    savePhotoData.tourItem.getLatitude().toDouble(),
                    savePhotoData.tourItem.getLongitude().toDouble()
                )
            )
            Log.d("TAG1 RecordDetailViewModel", "$latLngList")
        }
        _marker.value = latLngList
    }

}