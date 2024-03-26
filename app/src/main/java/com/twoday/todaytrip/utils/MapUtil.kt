package com.twoday.todaytrip.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PolylineOverlay
import com.twoday.todaytrip.R

object MapUtils {
    // 마커 아이콘 크기 조절하는 함수 - Int
    fun resizeMapIcons(context: Context, iconId: Int, width: Int, height: Int): Bitmap {
        val imageBitmap = BitmapFactory.decodeResource(context.resources, iconId)
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }

    // 마커 아이콘 크기 조절하는 함수 - Bitmap
    fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, width, height, false)
    }

    // 텍스트를 포함하는 아이콘 생성하는 함수(마커 위에 숫자 띄울 때)
    fun createIconWithText(context: Context, iconRes: Int, text: String): Bitmap {
        val resources = context.resources
        val scale = resources.displayMetrics.density
        val bitmap = BitmapFactory.decodeResource(resources, iconRes)
        var resultBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(resultBitmap)

        // 텍스트를 그리기 위한 페인트 설정
        val typeface = ResourcesCompat.getFont(context, R.font.mapo)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE // 텍스트 색상
            textSize = 70 * scale // 텍스트 크기
            setTypeface(Typeface.create(typeface, Typeface.BOLD)) // 기본 폰트를 굵은 스타일로 설정
        }

        // 텍스트의 위치를 계산
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        val x = (bitmap.width - bounds.width()) / 2f-10
        val y = (bitmap.height + bounds.height()) / 2f

        // 비트맵 위에 텍스트 그리기
        canvas.drawText(text, x, y, paint)

        return resultBitmap
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

    // 모든 마커를 포함하는 경계를 구해 카메라 확대 비율(기본:200) 조정하는 함수, 숫자가 클수록 화면이 축소됨
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