package com.twoday.todaytrip.ui.save_photo

import com.squareup.moshi.JsonClass
import com.twoday.todaytrip.tourData.TourItem

@JsonClass(generateAdapter = true)
data class SavePhotoData(
    val tourItem: TourItem,
    var imageUriList: MutableList<String> = mutableListOf(),
    val position: Int = 0
)
