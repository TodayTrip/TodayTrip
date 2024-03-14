package com.twoday.todaytrip.tourApi

import android.util.Log
import com.twoday.todaytrip.tourData.TourCategoryId1
import com.twoday.todaytrip.tourData.TourCategoryId2
import com.twoday.todaytrip.tourData.TourCategoryId3
import com.twoday.todaytrip.tourData.TourContentTypeId
import com.twoday.todaytrip.tourData.TourItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

object TourNetworkInterfaceUtils {
    private val TAG = "TourNetworkInterfaceUtils"
    fun fetchTouristAttractionList(areaCode: String): List<TourItem> = runBlocking(Dispatchers.IO) {
        val touristAttractionList = mutableListOf<TourItem>()

        withContext(Dispatchers.Default) {
            fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                numOfRows = 5
            )
        }?.let { areaBasedList ->
            areaBasedList.response.body.items.item.forEach { item ->
                fetchIntroDetail(item.contentId, item.contentTypeId)?.let {
                    touristAttractionList.add(TourItem.TouristDestination(item, it))
                }
            }
        }
        withContext(Dispatchers.Default) {
            fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                numOfRows = 5
            )
        }?.let { areaBasedList ->
            areaBasedList.response.body.items.item.forEach { item ->
                fetchIntroDetail(item.contentId, item.contentTypeId)?.let {
                    touristAttractionList.add(TourItem.CulturalFacilities(item, it))
                }
            }
        }

        return@runBlocking touristAttractionList.toList()
    }

    fun fetchTouristAttractionListWithTheme(theme: String, areaCode: String): List<TourItem> =
        runBlocking(Dispatchers.IO) {
            val touristAttractionList = mutableListOf<TourItem>()
            when (theme) {
                "산" -> {
                    fetchMountainThemeList(areaCode).forEach { areaBasedList ->
                        areaBasedList?.response?.body?.items?.item?.forEach { item ->
                            fetchIntroDetail(item.contentId, item.contentTypeId)?.let {
                                touristAttractionList.add(
                                    TourItem.TouristDestination(item, it)
                                )
                            }
                        }
                    }
                }

                "바다" -> {
                    fetchSeaThemeList(areaCode).forEach { areaBasedList ->
                        areaBasedList?.response?.body?.items?.item?.forEach { item ->
                            fetchIntroDetail(item.contentId, item.contentTypeId)?.let {
                                touristAttractionList.add(
                                    TourItem.TouristDestination(item, it)
                                )
                            }
                        }
                    }
                }

                "역사" -> {
                    fetchHistoricalTheme(areaCode)?.let { areaBasedList ->
                        areaBasedList.response.body.items.item.forEach { item ->
                            fetchIntroDetail(item.contentId, item.contentTypeId)?.let {
                                touristAttractionList.add(
                                    TourItem.TouristDestination(item, it)
                                )
                            }
                        }
                    }
                }

                "휴양" -> {
                    fetchRecreationalTheme(areaCode)?.let { areaBasedList ->
                        areaBasedList.response.body.items.item.forEach { item ->
                            fetchIntroDetail(item.contentId, item.contentTypeId)?.let {
                                touristAttractionList.add(
                                    TourItem.TouristDestination(item, it)
                                )
                            }
                        }
                    }
                }

                "체험" -> {
                    fetchExperientialTheme(areaCode)?.let { areaBasedList ->
                        areaBasedList.response.body.items.item.forEach { item ->
                            fetchIntroDetail(item.contentId, item.contentTypeId)?.let {
                                touristAttractionList.add(
                                    TourItem.TouristDestination(item, it)
                                )
                            }
                        }
                    }
                }

                "레포츠" -> {
                    fetchLeisureSportsTheme(areaCode)?.let { areaBasedList ->
                        areaBasedList.response.body.items.item.forEach { item ->
                            fetchIntroDetail(item.contentId, item.contentTypeId)?.let {
                                touristAttractionList.add(
                                    TourItem.LeisureSports(item, it)
                                )
                            }
                        }
                    }
                }

                "문화시설" -> {
                    fetchCulturalThemeList(areaCode).forEach { areaBasedList ->
                        areaBasedList?.response?.body?.items?.item?.forEach { item ->
                            fetchIntroDetail(item.contentId, item.contentTypeId)?.let {
                                touristAttractionList.add(
                                    TourItem.CulturalFacilities(item, it)
                                )
                            }
                        }
                    }
                }

                else -> {
                    Log.d(TAG, "error! theme does not exist!")
                }
            }
            return@runBlocking touristAttractionList.toList()
        }

    private fun fetchMountainThemeList(areaCode: String)
            : List<AreaBasedList?> =
        runBlocking(Dispatchers.IO) {
            val mountainList = async {
                fetchAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                    category1 = TourCategoryId1.NATURE.id,
                    category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                    category3 = TourCategoryId3.MOUNTAIN.id,
                    numOfRows = 3
                )
            }
            val naturalRecreationForestList = async {
                fetchAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                    category1 = TourCategoryId1.NATURE.id,
                    category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                    category3 = TourCategoryId3.NATURAL_RECREATION_FOREST.id,
                    numOfRows = 3
                )
            }
            val arboretumList = async {
                fetchAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                    category1 = TourCategoryId1.NATURE.id,
                    category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                    category3 = TourCategoryId3.ARBORETUM.id,
                    numOfRows = 3
                )
            }
            return@runBlocking listOf(
                mountainList.await(),
                naturalRecreationForestList.await(),
                arboretumList.await()
            )
        }

    private fun fetchSeaThemeList(areaCode: String)
            : List<AreaBasedList?> =
        runBlocking(Dispatchers.IO) {
            val coastalSceneryList = async {
                fetchAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                    category1 = TourCategoryId1.NATURE.id,
                    category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                    category3 = TourCategoryId3.COASTAL_SCENERY.id,
                    numOfRows = 2
                )
            }
            val postList = async {
                fetchAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                    category1 = TourCategoryId1.NATURE.id,
                    category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                    category3 = TourCategoryId3.PORT.id,
                    numOfRows = 2
                )
            }
            val lightHouseList = async {
                fetchAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                    category1 = TourCategoryId1.NATURE.id,
                    category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                    category3 = TourCategoryId3.LIGHTHOUSE.id,
                    numOfRows = 2
                )
            }
            val islandList = async {
                fetchAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                    category1 = TourCategoryId1.NATURE.id,
                    category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                    category3 = TourCategoryId3.ISLAND.id,
                    numOfRows = 2
                )
            }
            val beachList = async {
                fetchAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                    category1 = TourCategoryId1.NATURE.id,
                    category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                    category3 = TourCategoryId3.BEACH.id,
                    numOfRows = 2
                )
            }
            return@runBlocking listOf(
                coastalSceneryList.await(),
                postList.await(),
                lightHouseList.await(),
                islandList.await(),
                beachList.await()
            )
        }

    private fun fetchHistoricalTheme(areaCode: String): AreaBasedList? =
        runBlocking(Dispatchers.IO) {
            fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                category1 = TourCategoryId1.HUMANITIES.id,
                category2 = TourCategoryId2.HISTORICAL_TOURIST_ATTRACTION.id,
                numOfRows = 10
            )
        }

    private fun fetchRecreationalTheme(areaCode: String): AreaBasedList? =
        runBlocking(Dispatchers.IO) {
            fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                category1 = TourCategoryId1.HUMANITIES.id,
                category2 = TourCategoryId2.RECREATIONAL_TOURIST_ATTRACTION.id,
                numOfRows = 10
            )
        }

    private fun fetchExperientialTheme(areaCode: String): AreaBasedList? =
        runBlocking(Dispatchers.IO) {
            fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                category1 = TourCategoryId1.HUMANITIES.id,
                category2 = TourCategoryId2.EXPERIENTIAL_TOURIST_ATTRACTION.id,
                numOfRows = 10
            )
        }

    private fun fetchLeisureSportsTheme(areaCode: String): AreaBasedList? =
        runBlocking(Dispatchers.IO) {
            fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.LEISURE_SPORTS.contentTypeId,
                numOfRows = 10
            )
        }

    private fun fetchCulturalThemeList(areaCode: String): List<AreaBasedList?> =
        runBlocking(Dispatchers.IO) {
            val museumList = async {
                fetchAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                    category1 = TourCategoryId1.HUMANITIES.id,
                    category2 = TourCategoryId2.CULTURAL_FACILITIES.id,
                    category3 = TourCategoryId3.MUSEUM.id,
                    numOfRows = 2
                )
            }
            val memorialHallList = async {
                fetchAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                    category1 = TourCategoryId1.HUMANITIES.id,
                    category2 = TourCategoryId2.CULTURAL_FACILITIES.id,
                    category3 = TourCategoryId3.MEMORIAL_HALL.id,
                    numOfRows = 2
                )
            }
            val exhibitionList = async {
                fetchAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                    category1 = TourCategoryId1.HUMANITIES.id,
                    category2 = TourCategoryId2.CULTURAL_FACILITIES.id,
                    category3 = TourCategoryId3.EXHIBITION.id,
                    numOfRows = 2
                )
            }
            val areGalleryList = async {
                fetchAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                    category1 = TourCategoryId1.HUMANITIES.id,
                    category2 = TourCategoryId2.CULTURAL_FACILITIES.id,
                    category3 = TourCategoryId3.ART_GALLERY.id,
                    numOfRows = 2
                )
            }
            val conventionCenterList = async {
                fetchAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                    category1 = TourCategoryId1.HUMANITIES.id,
                    category2 = TourCategoryId2.CULTURAL_FACILITIES.id,
                    category3 = TourCategoryId3.CONVENTION_CENTER.id,
                    numOfRows = 2
                )
            }
            return@runBlocking listOf(
                museumList.await(),
                memorialHallList.await(),
                exhibitionList.await(),
                areGalleryList.await(),
                conventionCenterList.await()
            )
        }

    fun fetchRestaurantTabList(areaCode: String): List<TourItem> =
        runBlocking(Dispatchers.IO) {
            val restaurantList = fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.RESTAURANT.contentTypeId,
                numOfRows = 15
            )
            val restaurantTabList = mutableListOf<TourItem>()
            restaurantList?.response?.body?.items?.item
                ?.filter {
                    (!it.category3.isNullOrBlank()) && (it.category3 != TourCategoryId3.CAFE_AND_TEA.id)
                }
                ?.forEach { item ->
                    fetchIntroDetail(item.contentId, item.contentTypeId)?.let {
                        restaurantTabList.add(TourItem.Restaurant(item, it))
                    }
                }
            return@runBlocking restaurantTabList.toList()
        }

    fun getCafeTabList(areaCode: String)

            : List<TourItem> = runBlocking(Dispatchers.IO) {
        val cafeList = fetchAreaBasedList(
            areaCode = areaCode,
            contentTypeId = TourContentTypeId.RESTAURANT.contentTypeId,
            category1 = TourCategoryId1.FOOD.id,
            category2 = TourCategoryId2.FOOD.id,
            category3 = TourCategoryId3.CAFE_AND_TEA.id,
            numOfRows = 10
        )
        val cafeTabList = mutableListOf<TourItem>()
        cafeList?.response?.body?.items?.item?.forEach { item ->
            fetchIntroDetail(item.contentId, item.contentTypeId)?.let {
                cafeTabList.add(TourItem.Restaurant(item, it))
            }
        }
        return@runBlocking cafeTabList.toList()
    }

    fun getEventTabList(areaCode: String): List<TourItem> =
        runBlocking(Dispatchers.IO) {
            val eventList = fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.EVENT_PERFORMANCE_FESTIVAL.contentTypeId,
                category1 = TourCategoryId1.HUMANITIES.id,
                category2 = TourCategoryId2.PERFORMANCE_EVENT.id,
                numOfRows = 5
            )
            val festivalList = fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.EVENT_PERFORMANCE_FESTIVAL.contentTypeId,
                category1 = TourCategoryId1.HUMANITIES.id,
                category2 = TourCategoryId2.FESTIVAL.id,
                numOfRows = 5
            )
            val eventTabList = mutableListOf<TourItem>()
            eventList?.response?.body?.items?.item?.forEach { item ->
                fetchIntroDetail(item.contentId, item.contentTypeId)?.let {
                    eventTabList.add(TourItem.EventPerformanceFestival(item, it))
                }
            }
            festivalList?.response?.body?.items?.item?.forEach { item ->
                fetchIntroDetail(item.contentId, item.contentTypeId)?.let {
                    eventTabList.add(TourItem.EventPerformanceFestival(item, it))
                }
            }
            return@runBlocking eventTabList.toList()
        }

    private suspend fun fetchAreaBasedList(
        areaCode: String,
        contentTypeId: String,
        category1: String? = null,
        category2: String? = null,
        category3: String? = null,
        numOfRows: Int
    ): AreaBasedList? {
        try {
            val areaBasedResponse = TourNetworkClient.tourNetWork.fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = contentTypeId,
                category1 = category1,
                category2 = category2,
                category3 = category3,
                numOfRows = numOfRows
            )
            if (areaBasedResponse.response.body.totalCount == 0) {
                Log.d(TAG, "getAreaBasedList) totalCount = 0")
                return null
            }
            areaBasedResponse.response.body.items.item
                .toMutableList()
                .run {
                    val filteredList = this.filter {
                        (!it.mapX.isNullOrBlank()) && (!it.mapY.isNullOrBlank())
                    }
                    clear()
                    addAll(filteredList)
                }
            return areaBasedResponse
        } catch (e: Exception) {
            Log.d(TAG, "getAreaBasedList) error!")
            return null
        }
    }

    private suspend fun fetchIntroDetail(
        contentId: String,
        contentTypeId: String
    ): IntroDetailItem? {
        try {
            val introDetailResponse = TourNetworkClient.tourNetWork.fetchIntroDetail(
                contentId = contentId,
                contentTypeId = contentTypeId
            )
            if (introDetailResponse.response.body.totalCount == 0) {
                Log.d(TAG, "getIntroDetail) totalCount = 0")
                return null
            }
            return introDetailResponse.response.body.items.item[0]
        } catch (e: Exception) {
            Log.d(TAG, "getIntroDetail) error!")
            return null
        }
    }
}