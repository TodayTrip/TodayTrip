package com.twoday.todaytrip.ui.place_list

import com.twoday.todaytrip.tourData.TourItem

data class RecommendData(
    val imageUri: String,
    val subTitle: String,
    val title: String,
    val tourItem: TourItem? = null
)