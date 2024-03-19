package com.twoday.todaytrip.ui.place_list

import com.twoday.todaytrip.tourData.TourItem

enum class RecommendDataViewType(val viewType: Int){
    COVER(0),
    TOUR_ITEM(1)
}
interface RecommendData{
    fun getViewType():Int
}
data class RecommendCover(
    val viewType: Int = RecommendDataViewType.COVER.viewType,
    val imageId: Int,
    val subTitleId: Int,
    val title: String
): RecommendData{
    override fun getViewType(): Int = viewType
}

data class RecommendTourItem(
    val viewType: Int = RecommendDataViewType.TOUR_ITEM.viewType,
    val imageUrl: String,
    val subTitleId: Int,
    val title: String,
    val tourItem: TourItem
): RecommendData{
    override fun getViewType(): Int = viewType
}