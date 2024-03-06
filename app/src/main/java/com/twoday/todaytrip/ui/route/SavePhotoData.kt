package com.twoday.todaytrip.ui.route

import android.net.Uri
import com.twoday.todaytrip.tourData.TourItem

data class SavePhotoData(
    val tourItem: TourItem,
    val imageUrl: String? = null
)
