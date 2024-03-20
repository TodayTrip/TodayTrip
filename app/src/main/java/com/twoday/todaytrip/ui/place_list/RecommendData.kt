package com.twoday.todaytrip.ui.place_list

import com.twoday.todaytrip.tourData.TourItem

sealed interface RecommendData{}
data class RecommendCover(
    val imageId: Int,
    val subTitleId: Int,
    val title: String
): RecommendData

data class RecommendTourItem(
    val subTitleId: Int,
    val tourItem: TourItem
): RecommendData

data class RecommendEmpty(
    val subTitleId: Int,
    val titleId: Int
): RecommendData