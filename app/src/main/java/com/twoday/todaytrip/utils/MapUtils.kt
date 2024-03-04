package com.twoday.todaytrip.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PolylineOverlay

object MapUtils {
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
    // 마커 이미지와 일반 이미지를 합치는 함수
    fun combineImages(markerIcon: Bitmap, photo: Bitmap): Bitmap {
        val combinedImage = Bitmap.createBitmap(markerIcon.width, markerIcon.height, markerIcon.config)
        val canvas = Canvas(combinedImage)

        canvas.drawBitmap(markerIcon, Matrix(), null)

        val left = (markerIcon.width - photo.width) / 2f // 가로 중앙 위치 계산
        val top = (markerIcon.height / 7f) - (photo.height / 8f)
        canvas.drawBitmap(photo, left, top, null) // 조정된 위치에 사진을 그립니다.

        return combinedImage
    }
    // 폴리라인 그리는 함수
    fun drawPolyline(naverMap: NaverMap, sequence: List<LatLng>) {
        PolylineOverlay().apply {
            coords = sequence
            color = 0xFF0085FF.toInt()
            map = naverMap
        }
    }
}