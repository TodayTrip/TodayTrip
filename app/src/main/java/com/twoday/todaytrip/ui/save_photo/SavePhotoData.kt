package com.twoday.todaytrip.ui.save_photo

import android.os.Parcelable
import com.twoday.todaytrip.tourData.TourItem
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class SavePhotoData(
    val tourItem: TourItem,
//    var imageUri: String? = null,
    var imageUriList: MutableList<String> = mutableListOf(),
    val position: Int = 0
): java.io.Serializable, Parcelable
