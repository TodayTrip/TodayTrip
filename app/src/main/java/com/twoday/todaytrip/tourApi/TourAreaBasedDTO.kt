package com.twoday.todaytrip.tourApi

import com.squareup.moshi.Json

data class AreaBasedList(
    val response: AreaBasedListResponse
)
data class AreaBasedListResponse(
    val header: AreaBasedListResponseHeader,
    val body: AreaBasedListResponseBody,
)
data class AreaBasedListResponseHeader(
    val resultCode: String,
    val resultMsg: String
)
data class AreaBasedListResponseBody(
    val items: AreaBasedListItems,
    val numOfRows:Int,
    val pageNo:Int,
    val totalCount:Int
)
data class AreaBasedListItems(
    val item: List<AreaBasedListItem>
)

data class AreaBasedListItem(
    val title:String,
    @Json(name="contentid")
    val contentId:String,
    @Json(name="contenttypeid")
    val contentTypeId:String,
    @Json(name="createdtime")
    val createdTime:String,
    @Json(name="modifiedtime")
    val modifiedTime:String,
    val tel:String,
    @Json(name="addr1")
    val address:String,
    @Json(name="addr2")
    val addressDetail:String,
    val zipcode:String,
    @Json(name="mapx")
    val mapX:String="",
    @Json(name="mapy")
    val mapY:String="",
    @Json(name="mlevel")
    val mapLevel:String,
    @Json(name="areacode")
    val areaCode:String,
    @Json(name="sigungucode")
    val siGunGuCode:String,
    @Json(name="cat1")
    val category1:String,
    @Json(name="cat2")
    val category2:String,
    @Json(name="cat3")
    val category3:String,
    @Json(name="firstimage")
    val firstImage:String,
    @Json(name="firstimage2")
    val firstImageThumbnail:String,
    @Json(name="booktour")
    val bookTour:String,
    @Json(name="cpyrhtDivCd")
    val copyrightType:String
)