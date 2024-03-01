package com.twoday.todaytrip.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PolylineOverlay

object MapUtils {
    // 폴리라인 그리는 함수
    fun drawPolyline(naverMap: NaverMap, sequence: List<LatLng>) {
        PolylineOverlay().apply {
            coords = sequence
            color = 0xFF0085FF.toInt()
            map = naverMap
        }
    }
    // 마커 아이콘 크기 조절하는 함수
    fun resizeMapIcons(context: Context, iconId: Int, width: Int, height: Int): Bitmap {
        val imageBitmap = BitmapFactory.decodeResource(context.resources, iconId)
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }
    // 모든 마커들을 포함하는 경계 생성 함수
    fun createBoundsForAllMarkers(markers: List<Marker>): LatLngBounds {
        val bounds = LatLngBounds.Builder().apply {
            markers.forEach { marker ->
                include(marker.position)
            }
        }.build()
        return bounds
    }
    // 모든 마커를 포함하는 경계를 구해 카메라 확대 비율(기본:200) 조정하는 함수
    fun updateCameraToBounds(naverMap: NaverMap, bounds: LatLngBounds, padding: Int = 200) {
        val cameraUpdate = CameraUpdate.fitBounds(bounds, padding)
        naverMap.moveCamera(cameraUpdate)
    }

}