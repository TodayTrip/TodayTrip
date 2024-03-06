package com.twoday.todaytrip.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.naver.maps.map.overlay.Marker

class PlaceMapViewModel : ViewModel() {

    // 가장 먼 거리의 마커쌍을 저장하는 라이브 데이터
    private val _furthestPair = MutableLiveData<Pair<Marker, Marker>?>()
    val furthestPair: LiveData<Pair<Marker, Marker>?>
        get() {
            Log.d("furthestPair", "RecordDetailViewModel에서 furthestPair 라이브데이터")
            return _furthestPair
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
}