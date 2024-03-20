package com.twoday.todaytrip.ui.place_list

import com.naver.maps.geometry.LatLng
import com.twoday.todaytrip.R
import com.twoday.todaytrip.tourData.TourItem

sealed interface RecommendData{}
data class RecommendCover(
    val imageId: Int,
    val subTitleId: Int = R.string.place_list_recommend_sub_title_cover,
    val titleId: Int = R.string.place_list_recommend_title_cover,
    val destination: String,
    val destinationSigungu: String
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
    val locations: List<LatLng>
): RecommendData