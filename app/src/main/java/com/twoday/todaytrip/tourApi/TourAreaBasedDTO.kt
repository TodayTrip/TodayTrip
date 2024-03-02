package com.twoday.todaytrip.tourApi

import com.google.gson.annotations.SerializedName

data class AreaBasedListSize(
    val totalCnt:String
)

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
    val items: AreaBasedListItems
)
data class AreaBasedListItems(
    val item: List<AreaBasedListItem>
)
data class AreaBasedListItem(
    //장소 이름 (필수)
    val title:String,
    //콘텐츠 ID 정보 (필수)
    @SerializedName("contentid")
    val contentId:String, //콘텐츠 ID
    @SerializedName("contenttypeid")
    val contentTypeId:String, //콘텐츠 타입 ID
    //등록일&수정일 정보 (필수)
    @SerializedName("createdTime")
    val createdTime:String, //등록일
    @SerializedName("modifiedtime")
    val modifiedTime:String, //수정일

    //연락처 정보 (옵션)
    val tel:String,

    //주소 정보 (옵션)
    @SerializedName("addr1")
    val address:String, // 주소
    @SerializedName("addr2")
    val addressDetail:String, // 상세 주소
    val zipcode:String, // 우편 번호
    //좌표 정보 (옵션)
    @SerializedName("mapx")
    val mapX:String, // GPS X좌표 (WGS84 경도 좌표)
    @SerializedName("mapy")
    val mapY:String, // GPS Y좌표 (WGS84 위도 좌표)
    @SerializedName("mlevel")
    val mapLevel:String, //Map Level

    //지역 코드 정보 (옵션)
    @SerializedName("areacode")
    val areaCode:String, // 지역 코드 (옵션)
    @SerializedName("sigungucode")
    val siGunGuCode:String, // 시군구 코드 (옵션)
    //카테고리 정보 (옵션)
    @SerializedName("cat1")
    val category1:String, // 카테고리 대분류
    @SerializedName("cat2")
    val category2:String, // 카테고리 중분류
    @SerializedName("cat3")
    val category3:String, // 카테고리 소분류

    //이미지 정보 (옵션)
    @SerializedName("firstimage")
    val firstImage:String, //대표 이미지 원본 URL
    @SerializedName("firstimage2")
    val firstImageThumbnail:String, //대표 썸네일 이미지 URL

    //기타 정보 (옵션)
    @SerializedName("booktour")
    val bookTour:String, // 교과서 속 여행지 여부
    @SerializedName("cpyrhtDivCd")
    val copyrightType:String //저작권 유형
)