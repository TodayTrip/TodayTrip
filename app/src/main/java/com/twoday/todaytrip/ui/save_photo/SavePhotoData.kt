package com.twoday.todaytrip.ui.save_photo

import com.twoday.todaytrip.tourData.TourItem
import kotlinx.serialization.Serializable

@Serializable
data class SavePhotoData(
    val tourItem: TourItem,
    var imageUri: String? = null,
    val position: Int = 0
): java.io.Serializable
