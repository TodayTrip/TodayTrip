package com.twoday.todaytrip.ui.save_photo

import android.net.Uri
import com.twoday.todaytrip.tourData.TourItem
import kotlinx.serialization.Serializable

@Serializable
data class SavePhotoData(
    val tourItem: TourItem,
    val imageUrl: String? = null
): java.io.Serializable
