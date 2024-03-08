package com.twoday.todaytrip.ui.place_detail

data class PlaceData(
    val placeId: String? = null,
    val placePhotoUrl: String? = null,
    val placeTitle: String? = null,
    val placeAddress: String? = null,
    val placeDetailInfo: List<String> = emptyList()
) {
}
