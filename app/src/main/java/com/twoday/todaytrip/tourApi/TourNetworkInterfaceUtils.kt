package com.twoday.todaytrip.tourApi

import android.util.Log
import com.twoday.todaytrip.tourData.CulturalFacilities
import com.twoday.todaytrip.tourData.EventPerformanceFestival
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
        touristDestinationList.response.body.items.item.forEach { item ->
            getIntroDetail(item.contentId, item.contentTypeId)?.let {
                tourInfoTabList.add(TouristDestination(item, it))
            }
        }
        culturalFacilitiesList.response.body.items.item.forEach { item ->
            getIntroDetail(item.contentId, item.contentTypeId)?.let {
                tourInfoTabList.add(CulturalFacilities(item, it))
            }
        }
        return@runBlocking tourInfoTabList.toList()
    }

    fun getTourInfoTabListWithTheme(theme: String, areaCode: String): List<TourItem> =
        runBlocking(Dispatchers.IO) {
            val tourInfoTabList = mutableListOf<TourItem>()
            when (theme) {
                "산" -> {
                    val mountainThemeList = listOf(
                        TourNetworkClient.tourNetWork.getAreaBasedList(
                            areaCode = areaCode,
                            contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                            category1 = TourCategoryId1.NATURE.id,
                            category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                            category3 = TourCategoryId3.MOUNTAIN.id,
                            numOfRows = 3
                        ),
                        TourNetworkClient.tourNetWork.getAreaBasedList(
                            areaCode = areaCode,
                            contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                            category1 = TourCategoryId1.NATURE.id,
                            category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                            category3 = TourCategoryId3.NATURAL_RECREATION_FOREST.id,
                            numOfRows = 3
                        ),
                        TourNetworkClient.tourNetWork.getAreaBasedList(
                            areaCode = areaCode,
                            contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                            category1 = TourCategoryId1.NATURE.id,
                            category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                            category3 = TourCategoryId3.ARBORETUM.id,
                            numOfRows = 3
                        )
                    )
                    mountainThemeList.forEach { areaBasedList ->
                        areaBasedList.response.body.items.item.forEach { item ->
                            getIntroDetail(item.contentId, item.contentTypeId)?.let {
                                tourInfoTabList.add(
                                    TouristDestination(item, it)
                                )
                            }
                        }
                    }
                }

                "바다" -> {
                    val seaThemeList = listOf(
                        TourNetworkClient.tourNetWork.getAreaBasedList(
                            areaCode = areaCode,
                            contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                            category1 = TourCategoryId1.NATURE.id,
                            category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                            category3 = TourCategoryId3.COASTAL_SCENERY.id,
                            numOfRows = 2
                        ),
                        TourNetworkClient.tourNetWork.getAreaBasedList(
                            areaCode = areaCode,
                            contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                            category1 = TourCategoryId1.NATURE.id,
                            category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                            category3 = TourCategoryId3.PORT.id,
                            numOfRows = 2
                        ),
                        TourNetworkClient.tourNetWork.getAreaBasedList(
                            areaCode = areaCode,
                            contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                            category1 = TourCategoryId1.NATURE.id,
                            category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                            category3 = TourCategoryId3.LIGHTHOUSE.id,
                            numOfRows = 2
                        ),
                        TourNetworkClient.tourNetWork.getAreaBasedList(
                            areaCode = areaCode,
                            contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                            category1 = TourCategoryId1.NATURE.id,
                            category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                            category3 = TourCategoryId3.ISLAND.id,
                            numOfRows = 2
                        ),
                        TourNetworkClient.tourNetWork.getAreaBasedList(
                            areaCode = areaCode,
                            contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                            category1 = TourCategoryId1.NATURE.id,
                            category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                            category3 = TourCategoryId3.BEACH.id,
                            numOfRows = 2
                        )
                    )
                    seaThemeList.forEach { areaBasedList ->
                        areaBasedList.response.body.items.item.forEach { item ->
                            getIntroDetail(item.contentId, item.contentTypeId)?.let {
                                tourInfoTabList.add(
                                    TouristDestination(item, it)
                                )
                            }
                        }
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
                    historicalList.response.body.items.item.forEach { item ->
                        getIntroDetail(item.contentId, item.contentTypeId)?.let {
                            tourInfoTabList.add(
                                TouristDestination(item, it)
                            )
                        }
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
                    recreationalList.response.body.items.item.forEach { item ->
                        getIntroDetail(item.contentId, item.contentTypeId)?.let {
                            tourInfoTabList.add(
                                TouristDestination(item, it)
                            )
                        }
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
                    experientialList.response.body.items.item.forEach { item ->
                        getIntroDetail(item.contentId, item.contentTypeId)?.let {
                            tourInfoTabList.add(
                                TouristDestination(item, it)
                            )
                        }
                    }
                }

                "레포츠" -> {
                    val leisureSportsList = TourNetworkClient.tourNetWork.getAreaBasedList(
                        areaCode = areaCode,
                        contentTypeId = TourContentTypeId.LEISURE_SPORTS.contentTypeId,
                        numOfRows = 10
                    )
                    leisureSportsList.response.body.items.item.forEach { item ->
                        getIntroDetail(item.contentId, item.contentTypeId)?.let {
                            tourInfoTabList.add(
                                TouristDestination(item, it)
                            )
                        }
                    }
                }

                "문화시설" -> {
                    val culturalThemeList = listOf(
                        TourNetworkClient.tourNetWork.getAreaBasedList(
                            areaCode = areaCode,
                            contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                            category1 = TourCategoryId1.HUMANITIES.id,
                            category2 = TourCategoryId2.CULTURAL_FACILITIES.id,
                            category3 = TourCategoryId3.MUSEUM.id,
                            numOfRows = 2
                        ),
                        TourNetworkClient.tourNetWork.getAreaBasedList(
                            areaCode = areaCode,
                            contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                            category1 = TourCategoryId1.HUMANITIES.id,
                            category2 = TourCategoryId2.CULTURAL_FACILITIES.id,
                            category3 = TourCategoryId3.MEMORIAL_HALL.id,
                            numOfRows = 2
                        ), TourNetworkClient.tourNetWork.getAreaBasedList(
                            areaCode = areaCode,
                            contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                            category1 = TourCategoryId1.HUMANITIES.id,
                            category2 = TourCategoryId2.CULTURAL_FACILITIES.id,
                            category3 = TourCategoryId3.EXHIBITION.id,
                            numOfRows = 2
                        ), TourNetworkClient.tourNetWork.getAreaBasedList(
                            areaCode = areaCode,
                            contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                            category1 = TourCategoryId1.HUMANITIES.id,
                            category2 = TourCategoryId2.CULTURAL_FACILITIES.id,
                            category3 = TourCategoryId3.ART_GALLERY.id,
                            numOfRows = 2
                        ), TourNetworkClient.tourNetWork.getAreaBasedList(
                            areaCode = areaCode,
                            contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                            category1 = TourCategoryId1.HUMANITIES.id,
                            category2 = TourCategoryId2.CULTURAL_FACILITIES.id,
                            category3 = TourCategoryId3.CONVENTION_CENTER.id,
                            numOfRows = 2
                        )
                    )
                    culturalThemeList.forEach { areaBasedList ->
                        areaBasedList.response.body.items.item.forEach { item ->
                            getIntroDetail(item.contentId, item.contentTypeId)?.let {
                                tourInfoTabList.add(
                                    CulturalFacilities(item, it)
                                )
                            }
                        }
                    }
                }

                else -> {
                    Log.d("getTourInfoTabListWithTheme", "error! theme does not exist!")
                }
            }
            return@runBlocking tourInfoTabList.toList()
        }

    fun getRestaurantTabList(areaCode: String): List<TourItem> =
        runBlocking(Dispatchers.IO) {
            val restaurantList =
                TourNetworkClient.tourNetWork.getAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.RESTAURANT.contentTypeId,
                    category1 = TourCategoryId1.FOOD.id,
                    category2 = TourCategoryId2.FOOD.id,
                    numOfRows = 15
                )
            val restaurantTabList = mutableListOf<TourItem>()
            restaurantList.response.body.items.item
                .filter { it.category3 != TourCategoryId3.CAFE_AND_TEA.id }
                .forEach { item ->
                    getIntroDetail(item.contentId, item.contentTypeId)?.let {
                        restaurantTabList.add(Restaurant(item, it))
                    }
                }
            return@runBlocking restaurantTabList.toList()
        }

    fun getCafeTabList(areaCode: String)

            : List<TourItem> = runBlocking(Dispatchers.IO) {
        val cafeList = TourNetworkClient.tourNetWork.getAreaBasedList(
            areaCode = areaCode,
            contentTypeId = TourContentTypeId.RESTAURANT.contentTypeId,
            category1 = TourCategoryId1.FOOD.id,
            category2 = TourCategoryId2.FOOD.id,
            category3 = TourCategoryId3.CAFE_AND_TEA.id,
            numOfRows = 10
        )
        val cafeTabList = mutableListOf<TourItem>()
        cafeList.response.body.items.item.forEach { item ->
            getIntroDetail(item.contentId, item.contentTypeId)?.let {
                cafeTabList.add(Restaurant(item, it))
            }
        }
        return@runBlocking cafeTabList.toList()
    }

    fun getEventTabList(areaCode: String): List<TourItem> =
        runBlocking(Dispatchers.IO) {
            val eventList = TourNetworkClient.tourNetWork.getAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.EVENT_PERFORMANCE_FESTIVAL.contentTypeId,
                category1 = TourCategoryId1.HUMANITIES.id,
                category2 = TourCategoryId2.PERFORMANCE_EVENT.id,
                numOfRows = 5
            )
            val festivalList = TourNetworkClient.tourNetWork.getAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.EVENT_PERFORMANCE_FESTIVAL.contentTypeId,
                category1 = TourCategoryId1.HUMANITIES.id,
                category2 = TourCategoryId2.FESTIVAL.id,
                numOfRows = 5
            )
            val eventTabList = mutableListOf<TourItem>()
            eventList.response.body.items.item.forEach { item ->
                getIntroDetail(item.contentId, item.contentTypeId)?.let {
                    eventTabList.add(EventPerformanceFestival(item, it))
                }
            }
            festivalList.response.body.items.item.forEach { item ->
                getIntroDetail(item.contentId, item.contentTypeId)?.let {
                    eventTabList.add(EventPerformanceFestival(item, it))
                }
            }
            return@runBlocking eventTabList.toList()
        }

    private fun getIntroDetail(
        contentId: String,
        contentTypeId: String
    ): IntroDetailItem? =
        runBlocking(Dispatchers.IO) {
            val introDetail = TourNetworkClient.tourNetWork.getIntroDetail(
                contentId = contentId,
                contentTypeId = contentTypeId
            )
            if (introDetail.response.body.numOfRows == "0")
                return@runBlocking null
            return@runBlocking introDetail.response.body.items.item[0]
        }
}