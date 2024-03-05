package com.twoday.todaytrip.tourData

import android.util.Log
import com.twoday.todaytrip.tourApi.AreaBasedListItem
import com.twoday.todaytrip.tourApi.IntroDetailItem


open class TourItem(
    var isAdded: Boolean = false,
    protected val tourItemInfo: AreaBasedListItem
) {
    fun getTitle() = tourItemInfo.title
    fun getAddress() = tourItemInfo.address ?: "주소 정보 없음"
    fun getThumbnailImage() = tourItemInfo.firstImageThumbnail?: null
    open fun getTimeInfoWithLabel(): List<Pair<String, String>>{
        Log.d("TourItem", "getTimeInfoWithLabel()")
        return emptyList()
    }
    open fun getDetailInfoWithLabel(): List<Pair<String, String>> = emptyList()


    fun getContentId() = tourItemInfo.contentId
    fun getContentTypeId() = tourItemInfo.contentTypeId
}
