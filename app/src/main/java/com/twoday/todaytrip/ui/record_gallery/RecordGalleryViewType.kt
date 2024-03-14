package com.twoday.todaytrip.ui.record_gallery

import com.twoday.todaytrip.tourData.TourItem

sealed class RecordGalleryViewType {
    data class RecordGalleryTag(val item: TourItem) : RecordGalleryViewType()
    data class RecordGalleryPhoto(val item: TourItem) : RecordGalleryViewType()

}