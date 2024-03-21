package com.twoday.todaytrip.tourApi

data class LocationBasedList(
    val response: LocationBasedListResponse
)
data class LocationBasedListResponse(
    val header: LocationBasedListResponseHeader,
    val body: LocationBasedListResponseBody
)
data class LocationBasedListResponseHeader(
    val resultCode: String,
    val resultMsg: String
)
data class LocationBasedListResponseBody(
    val items: LocationBasedItems,
    val numOfRows: Int,
    val pageNo:Int,
    val totalCount:Int
)
data class LocationBasedItems(
    val item: List<LocationBasedItem>
)
data class LocationBasedItem(
    val addr1: String,
    val addr2: String,
    val areacode: String,
    val booktour:String,
    val cat1: String,
    val cat2: String,
    val cat3: String,
    val contentId:String,
    val contenttypeid:String,
    val createdtime:String,
    val dist:String,
    val firstimage:String,
    val firstimage2:String,
    val cpyrhtDivCd:String,
    val mapx:String,
    val mapy:String,
    val mlevel:String,
    val modifiedtime:String,
    val tel:String,
    val title:String
)