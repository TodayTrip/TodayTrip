package com.twoday.todaytrip.tourData

import com.twoday.todaytrip.tourApi.AreaBasedListItem

abstract class TourItem(
    var isAdded: Boolean = false,
    protected val tourItemInfo: AreaBasedListItem
) {
    fun getTitle() = tourItemInfo.title
    fun getAddress() = tourItemInfo.address ?: "주소 정보 없음"
    abstract fun getTimeInfoWithLabel(): List<Pair<String, String>>
    abstract fun getDetailInfoWithLabel(): List<Pair<String, String>>

    fun getContentId() = tourItemInfo.contentId
    fun getContentTypeId() = tourItemInfo.contentTypeId
}
