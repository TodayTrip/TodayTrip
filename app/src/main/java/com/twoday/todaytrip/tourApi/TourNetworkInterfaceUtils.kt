package com.twoday.todaytrip.tourApi

import android.util.Log
import com.twoday.todaytrip.tourData.CulturalFacilities
import com.twoday.todaytrip.tourData.LeisureSports
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
    fun getTourInfoTabList(areaCode: String): List<TourItem> = runBlocking(Dispatchers.IO) {
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

    fun getTourInfoTabListWithTheme(theme: String, areaCode: String): List<TourItem> =
        runBlocking(Dispatchers.IO) {
            val tourInfoTabList = mutableListOf<TourItem>()
            when (theme) {
                "산" -> {
                    val mountainList = TourNetworkClient.tourNetWork.getAreaBasedList(
                        areaCode = areaCode,
                        contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                        category1 = TourCategoryId1.NATURE.id,
                        category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                        category3 = TourCategoryId3.MOUNTAIN.id,
                        numOfRows = 3
                    )
                    val naturalRecreationForestList =
                        TourNetworkClient.tourNetWork.getAreaBasedList(
                            areaCode = areaCode,
                            contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                            category1 = TourCategoryId1.NATURE.id,
                            category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                            category3 = TourCategoryId3.NATURAL_RECREATION_FOREST.id,
                            numOfRows = 3
                        )
                    val arboretumList = TourNetworkClient.tourNetWork.getAreaBasedList(
                        areaCode = areaCode,
                        contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                        category1 = TourCategoryId1.NATURE.id,
                        category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                        category3 = TourCategoryId3.ARBORETUM.id,
                        numOfRows = 3
                    )
                    mountainList.response.body.items.item.forEach {
                        tourInfoTabList.add(
                            TouristDestination(
                                it, getIntroDetail(it.contentId, it.contentTypeId)[0]
                            )
                        )
                    }
                    naturalRecreationForestList.response.body.items.item.forEach {
                        tourInfoTabList.add(
                            TouristDestination(
                                it, getIntroDetail(it.contentId, it.contentTypeId)[0]
                            )
                        )
                    }
                    arboretumList.response.body.items.item.forEach {
                        tourInfoTabList.add(
                            TouristDestination(
                                it, getIntroDetail(it.contentId, it.contentTypeId)[0]
                            )
                        )
                    }
                }

                "바다" -> {
                    val coastalSceneryList = TourNetworkClient.tourNetWork.getAreaBasedList(
                        areaCode = areaCode,
                        contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                        category1 = TourCategoryId1.NATURE.id,
                        category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                        category3 = TourCategoryId3.COASTAL_SCENERY.id,
                        numOfRows = 2
                    )
                    val portList = TourNetworkClient.tourNetWork.getAreaBasedList(
                        areaCode = areaCode,
                        contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                        category1 = TourCategoryId1.NATURE.id,
                        category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                        category3 = TourCategoryId3.PORT.id,
                        numOfRows = 2
                    )
                    val lighthouseList = TourNetworkClient.tourNetWork.getAreaBasedList(
                        areaCode = areaCode,
                        contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                        category1 = TourCategoryId1.NATURE.id,
                        category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                        category3 = TourCategoryId3.LIGHTHOUSE.id,
                        numOfRows = 2
                    )
                    val islandList = TourNetworkClient.tourNetWork.getAreaBasedList(
                        areaCode = areaCode,
                        contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                        category1 = TourCategoryId1.NATURE.id,
                        category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                        category3 = TourCategoryId3.ISLAND.id,
                        numOfRows = 2
                    )
                    val beachList = TourNetworkClient.tourNetWork.getAreaBasedList(
                        areaCode = areaCode,
                        contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                        category1 = TourCategoryId1.NATURE.id,
                        category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                        category3 = TourCategoryId3.BEACH.id,
                        numOfRows = 2
                    )
                    coastalSceneryList.response.body.items.item.forEach {
                        tourInfoTabList.add(
                            TouristDestination(
                                it,
                                getIntroDetail(it.contentId, it.contentTypeId)[0]
                            )
                        )
                    }
                    portList.response.body.items.item.forEach {
                        tourInfoTabList.add(
                            TouristDestination(
                                it,
                                getIntroDetail(it.contentId, it.contentTypeId)[0]
                            )
                        )
                    }
                    lighthouseList.response.body.items.item.forEach {
                        tourInfoTabList.add(
                            TouristDestination(
                                it,
                                getIntroDetail(it.contentId, it.contentTypeId)[0]
                            )
                        )
                    }
                    islandList.response.body.items.item.forEach {
                        tourInfoTabList.add(
                            TouristDestination(
                                it,
                                getIntroDetail(it.contentId, it.contentTypeId)[0]
                            )
                        )
                    }
                    beachList.response.body.items.item.forEach {
                        tourInfoTabList.add(
                            TouristDestination(
                                it,
                                getIntroDetail(it.contentId, it.contentTypeId)[0]
                            )
                        )
                    }
                }

                "역사" -> {
                    val historicalList = TourNetworkClient.tourNetWork.getAreaBasedList(
                        areaCode = areaCode,
                        contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                        category1 = TourCategoryId1.HUMANITIES.id,
                        category2 = TourCategoryId2.HISTORICAL_TOURIST_ATTRACTION.id,
                        numOfRows = 10
                    )
                    historicalList.response.body.items.item.forEach {
                        tourInfoTabList.add(
                            TouristDestination(
                                it,
                                getIntroDetail(it.contentId, it.contentTypeId)[0]
                            )
                        )
                    }
                }

                "휴양" -> {
                    val recreationalList = TourNetworkClient.tourNetWork.getAreaBasedList(
                        areaCode = areaCode,
                        contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                        category1 = TourCategoryId1.HUMANITIES.id,
                        category2 = TourCategoryId2.RECREATIONAL_TOURIST_ATTRACTION.id,
                        numOfRows = 10
                    )
                    recreationalList.response.body.items.item.forEach {
                        tourInfoTabList.add(
                            TouristDestination(
                                it,
                                getIntroDetail(it.contentId, it.contentTypeId)[0]
                            )
                        )
                    }
                }

                "체험" -> {
                    val experientialList = TourNetworkClient.tourNetWork.getAreaBasedList(
                        areaCode = areaCode,
                        contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                        category1 = TourCategoryId1.HUMANITIES.id,
                        category2 = TourCategoryId2.EXPERIENTIAL_TOURIST_ATTRACTION.id,
                        numOfRows = 10
                    )
                    experientialList.response.body.items.item.forEach {
                        tourInfoTabList.add(
                            TouristDestination(
                                it,
                                getIntroDetail(it.contentId, it.contentTypeId)[0]
                            )
                        )
                    }
                }

                "레포츠" -> {
                    val leisureSportsList = TourNetworkClient.tourNetWork.getAreaBasedList(
                        areaCode = areaCode,
                        contentTypeId = TourContentTypeId.LEISURE_SPORTS.contentTypeId,
                        numOfRows = 10
                    )
                    leisureSportsList.response.body.items.item.forEach {
                        tourInfoTabList.add(
                            LeisureSports(it, getIntroDetail(it.contentId, it.contentTypeId)[0])
                        )
                    }
                }

                "문화시설" -> {
                    val museumList = TourNetworkClient.tourNetWork.getAreaBasedList(
                        areaCode = areaCode,
                        contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                        category1 = TourCategoryId1.HUMANITIES.id,
                        category2 = TourCategoryId2.CULTURAL_FACILITIES.id,
                        category3 = TourCategoryId3.MUSEUM.id,
                        numOfRows = 2
                    )
                    val memorialHallList = TourNetworkClient.tourNetWork.getAreaBasedList(
                        areaCode = areaCode,
                        contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                        category1 = TourCategoryId1.HUMANITIES.id,
                        category2 = TourCategoryId2.CULTURAL_FACILITIES.id,
                        category3 = TourCategoryId3.MEMORIAL_HALL.id,
                        numOfRows = 2
                    )
                    val exhibitionList = TourNetworkClient.tourNetWork.getAreaBasedList(
                        areaCode = areaCode,
                        contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                        category1 = TourCategoryId1.HUMANITIES.id,
                        category2 = TourCategoryId2.CULTURAL_FACILITIES.id,
                        category3 = TourCategoryId3.EXHIBITION.id,
                        numOfRows = 2
                    )
                    val artGalleryList = TourNetworkClient.tourNetWork.getAreaBasedList(
                        areaCode = areaCode,
                        contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                        category1 = TourCategoryId1.HUMANITIES.id,
                        category2 = TourCategoryId2.CULTURAL_FACILITIES.id,
                        category3 = TourCategoryId3.ART_GALLERY.id,
                        numOfRows = 2
                    )
                    val conventionCenterList = TourNetworkClient.tourNetWork.getAreaBasedList(
                        areaCode = areaCode,
                        contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                        category1 = TourCategoryId1.HUMANITIES.id,
                        category2 = TourCategoryId2.CULTURAL_FACILITIES.id,
                        category3 = TourCategoryId3.CONVENTION_CENTER.id,
                        numOfRows = 2
                    )
                    museumList.response.body.items.item.forEach {
                        tourInfoTabList.add(
                            CulturalFacilities(
                                it,
                                getIntroDetail(it.contentId, it.contentTypeId)[0]
                            )
                        )
                    }
                    memorialHallList.response.body.items.item.forEach {
                        tourInfoTabList.add(
                            CulturalFacilities(
                                it,
                                getIntroDetail(it.contentId, it.contentTypeId)[0]
                            )
                        )
                    }
                    exhibitionList.response.body.items.item.forEach {
                        tourInfoTabList.add(
                            CulturalFacilities(
                                it,
                                getIntroDetail(it.contentId, it.contentTypeId)[0]
                            )
                        )
                    }
                    artGalleryList.response.body.items.item.forEach {
                        tourInfoTabList.add(
                            CulturalFacilities(
                                it,
                                getIntroDetail(it.contentId, it.contentTypeId)[0]
                            )
                        )
                    }
                    conventionCenterList.response.body.items.item.forEach {
                        tourInfoTabList.add(
                            CulturalFacilities(
                                it,
                                getIntroDetail(it.contentId, it.contentTypeId)[0]
                            )
                        )
                    }
                }

                else -> {
                    Log.d("getTourInfoTabListWithTheme", "error! theme does not exist!")
                }
            }
            return@runBlocking tourInfoTabList.toList()
        }

    fun getRestaurantTabList(areaCode: String): List<TourItem> = runBlocking(Dispatchers.IO) {
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

    fun getCafeTabList(areaCode: String): List<TourItem> = runBlocking(Dispatchers.IO) {
        val cafeList = TourNetworkClient.tourNetWork.getAreaBasedList(
            areaCode = areaCode,
            contentTypeId = TourContentTypeId.RESTAURANT.contentTypeId,
            category1 = TourCategoryId1.FOOD.id,
            category2 = TourCategoryId2.FOOD.id,
            category3 = TourCategoryId3.CAFE_AND_TEA.id,
            numOfRows = 10
        )
        val cafeTabList = mutableListOf<TourItem>()
        cafeList.response.body.items.item.forEach {
            cafeTabList.add(
                Restaurant(it, getIntroDetail(it.contentId, it.contentTypeId)[0])
            )
        }
        return@runBlocking cafeTabList.toList()
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