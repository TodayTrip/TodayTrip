package com.twoday.todaytrip.tourApi

import com.twoday.todaytrip.tourData.CulturalFacilities
import com.twoday.todaytrip.tourData.Restaurant
import com.twoday.todaytrip.tourData.TourCategoryId1
import com.twoday.todaytrip.tourData.TourCategoryId2
import com.twoday.todaytrip.tourData.TourCategoryId3
import com.twoday.todaytrip.tourData.TourContentTypeId
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.tourData.TouristDestination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

object TourNetworkInterfaceUtils {
    fun getTourInfoTabList(areaCode:String): List<TourItem> = runBlocking(Dispatchers.IO) {
        val touristDestinationList: AreaBasedList = TourNetworkClient.tourNetWork.getAreaBasedList(
            areaCode = areaCode,
            contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
            numOfRows = 5
        )
        val culturalFacilitiesList: AreaBasedList = TourNetworkClient.tourNetWork.getAreaBasedList(
            areaCode = areaCode,
            contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
            numOfRows = 5
        )

        val tourInfoTabList = mutableListOf<TourItem>()
        touristDestinationList.response.body.items.item.forEach {
            tourInfoTabList.add(
                TouristDestination(
                    it,
                    getIntroDetail(it.contentId, it.contentTypeId)[0]
                )
            )
        }
        culturalFacilitiesList.response.body.items.item.forEach {
            tourInfoTabList.add(
                CulturalFacilities(
                    it,
                    getIntroDetail(it.contentId, it.contentTypeId)[0]
                )
            )
        }
        return@runBlocking tourInfoTabList.toList()
    }
    fun getTourInfoTabListWithTheme(theme:String, areaCode: String): List<TourItem> = runBlocking(Dispatchers.IO){
        val tourInfoTabList = mutableListOf<TourItem>()
        //TODO 테마 별 관광지 정보 가져오기
        return@runBlocking tourInfoTabList.toList()
    }

    fun getRestaurantTabList(areaCode:String): List<TourItem> = runBlocking(Dispatchers.IO) {
        val restaurantList = listOf(
            TourNetworkClient.tourNetWork.getAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.RESTAURANT.contentTypeId,
                category1 = TourCategoryId1.FOOD.id,
                category2 = TourCategoryId2.FOOD.id,
                category3 = TourCategoryId3.KOREAN_FOOD.id,
                numOfRows = 2
            ),
            TourNetworkClient.tourNetWork.getAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.RESTAURANT.contentTypeId,
                category1 = TourCategoryId1.FOOD.id,
                category2 = TourCategoryId2.FOOD.id,
                category3 = TourCategoryId3.WESTERN_FOOD.id,
                numOfRows = 2
            ),
            TourNetworkClient.tourNetWork.getAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.RESTAURANT.contentTypeId,
                category1 = TourCategoryId1.FOOD.id,
                category2 = TourCategoryId2.FOOD.id,
                category3 = TourCategoryId3.JAPANESE_FOOD.id,
                numOfRows = 2
            ),
            TourNetworkClient.tourNetWork.getAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.RESTAURANT.contentTypeId,
                category1 = TourCategoryId1.FOOD.id,
                category2 = TourCategoryId2.FOOD.id,
                category3 = TourCategoryId3.CHINESE_FOOD.id,
                numOfRows = 2
            ),
            TourNetworkClient.tourNetWork.getAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.RESTAURANT.contentTypeId,
                category1 = TourCategoryId1.FOOD.id,
                category2 = TourCategoryId2.FOOD.id,
                category3 = TourCategoryId3.UNIQUE_FOOD.id,
                numOfRows = 2
            )
        )

        val restaurantTabList = mutableListOf<TourItem>()
        restaurantList.forEach { areaBasedList ->
            areaBasedList.response.body.items.item.forEach { item ->
                restaurantTabList.add(
                    Restaurant(
                        item,
                        getIntroDetail(item.contentId, item.contentTypeId)[0]
                    )
                )
            }
        }
        return@runBlocking restaurantTabList.toList()
    }

    private fun getIntroDetail(contentId: String, contentTypeId: String): List<IntroDetailItem> =
        runBlocking(Dispatchers.IO) {
            val introDetail = TourNetworkClient.tourNetWork.getIntroDetail(
                contentId = contentId,
                contentTypeId = contentTypeId
            )
            return@runBlocking introDetail.response.body.items.item
        }
}