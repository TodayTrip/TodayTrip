package com.twoday.todaytrip.tourData

open class TourItem (
    open val tourItemInfo: TourItemInfo
        ){
    fun getTitle() = tourItemInfo.defaultInfo.title
    fun getAddress() = tourItemInfo.addressInfo?: "주소 정보 없음"

    fun getContentId() = tourItemInfo.contentId
    fun getContentTypeId() = tourItemInfo.contentTypeId

    data class TourItemInfo(
        val contentId:String,
        val contentTypeId:String,
        val defaultInfo:DefaultInfo,
        val firstImageInfo: FirstImageInfo,
        val areaCodeInfo:AreaCodeInfo,
        val categoryInfo: CategoryInfo,
        val addressInfo: AddressInfo,
        val mapInfo: MapInfo
    )
    data class DefaultInfo(
        val title:String,
        val tel:String? = null,
        val telName:String?= null,
        val homepage:String? = null,
        val createdTime:String,
        val modifiedTime:String
    )
    data class FirstImageInfo(
        val imageUrl:String? = null,
        val thumbnailUrl:String?= null
    )
    data class AreaCodeInfo(
        val areaCode:String?=null,
        val siGunGuCode:String?=null
    )
    data class CategoryInfo(
        val category1:String?=null,
        val category2:String?=null,
        val category3:String?=null,
    )
    data class AddressInfo(
        val address:String? = null,
        val zipcode:String? = null
    )
    data class MapInfo(
        val mapX:String?=null,
        val mapY:String?=null,
    )
}
