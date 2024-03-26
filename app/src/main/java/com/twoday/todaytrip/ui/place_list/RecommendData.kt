package com.twoday.todaytrip.ui.place_list

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.twoday.todaytrip.R
import com.twoday.todaytrip.tourData.TourItem

sealed interface RecommendData{}
data class RecommendCover(
    val imageId: Int,
    val destination: String
): RecommendData

data class RecommendTourItem(
    val subTitleId: Int,
    val tourItem: TourItem
): RecommendData

data class RecommendEmpty(
    val subTitleId: Int,
    val titleId: Int
): RecommendData

data class RecommendMap(
    val destination: String,
    var optimizedLocations: List<LatLng> = listOf(),
    var optimizedOrder: List<Int> = listOf(),
    var isAllAdded: Boolean = false
): RecommendData