package com.twoday.todaytrip.ui.place_list

import com.twoday.todaytrip.tourData.TourItem

interface RecommendData{}
data class RecommendCover(
    val imageId: Int,
    val subTitleId: Int,
    val title: String
): RecommendData

data class RecommendTourItem(
    val imageUrl: String?,
    val subTitleId: Int,
    val title: String,
    val tourItem: TourItem
): RecommendData