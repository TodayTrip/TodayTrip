package com.twoday.todaytrip.ui.save_photo

import android.icu.text.Transliterator.Position
import android.net.Uri
import com.twoday.todaytrip.tourData.TourItem
import kotlinx.serialization.Serializable

@Serializable
data class SavePhotoData(
    val tourItem: TourItem,
    val imageUrl: String? = null,
    val position: Int = 0
): java.io.Serializable
