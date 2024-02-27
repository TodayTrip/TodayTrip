package com.twoday.todaytrip.tourApi

import com.google.gson.annotations.SerializedName

data class CommonDetail(
    val response: CommonDetailResponse
)

data class CommonDetailResponse(
    val header: CommonDetailResponseHeader,
    val body: CommonDetailResponseBody
)

data class CommonDetailResponseHeader(
    val resultCode: String,
    val resultMsg: String
)

data class CommonDetailResponseBody(
    val items: CommonDetailItems,
    val numOfRows: String,
    val pageNio: String,
    val totalCount: String
)

data class CommonDetailItems(
    val item: List<CommonDetailItem>
)

data class CommonDetailItem(
    // 기본 응답
    @SerializedName("contentid")
    val contentId: String,
    @SerializedName("contenttypeid")
    val contentTypeId: String,
    // 기본 정보(default)
    val title: String,
    val tel: String,
    @SerializedName("telname")
    val telName: String,
    val homepage: String,
    @SerializedName("createdtime")
    val createdTime: String,
    @SerializedName("modifiedtime")
    val modifiedTime: String,
    val booktour: String,
    // 이미지 정보(firstImage)
    @SerializedName("firstimage")
    val firstImage: String,
    @SerializedName("firstimage2")
    val firstImageThumbnail: String,
    @SerializedName("cpyrhtDivCd")
    val copyrightType: String,
    // 지역 정보(areacode)
    @SerializedName("areacode")
    val areaCode: String,
    @SerializedName("sigungucode")
    val siGunGuCode: String,
    // 카테고리 정보(catcode)
    @SerializedName("cat1")
    val category1: String, // 카테고리 대분류
    @SerializedName("cat2")
    val category2: String, // 카테고리 중분류
    @SerializedName("cat3")
    val category3: String, // 카테고리 소분류
    // 주소 정보(addrInfo)
    @SerializedName("add1")
    val address: String,
    @SerializedName("addr2")
    val addressDetail: String,
    val zipcode: String,
    // 지도 정보(mapInfo)
    @SerializedName("mapx")
    val mapX: String,
    @SerializedName("mapy")
    val mapY: String,
    @SerializedName("mlevel")
    val mapLevel: String,
    // 개요 정보(overview)
    val overview: String
)