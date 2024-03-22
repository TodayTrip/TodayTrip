package com.twoday.todaytrip.ui.place_list

import com.naver.maps.geometry.LatLng
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
    var locations: List<LatLng>,
    var isAllAdded: Boolean = false
): RecommendData