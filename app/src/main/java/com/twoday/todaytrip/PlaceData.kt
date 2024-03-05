package com.twoday.todaytrip

data class PlaceData(
    val placeId: String? = null,
    val placePhotoUrl: String? = null,
    val placeTitle: String? = null,
    val placeAddress: String? = null,
    val placeDetailInfo: List<String> = emptyList()
) {
}
