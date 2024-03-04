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
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

object TourNetworkInterfaceUtils {
    private val TAG = "TourNetworkInterfaceUtils"
    fun getTourInfoTabList(areaCode: String): List<TourItem> = runBlocking(Dispatchers.IO) {
        val tourInfoTabList = mutableListOf<TourItem>()

        val touristDestinationList = async {
            getAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                numOfRows = 5
            )
        }
        touristDestinationList.await()?.let { areaBasedList ->
            areaBasedList.response.body.items.item.forEach { item ->
                getIntroDetail(item.contentId, item.contentTypeId)?.let {
                    tourInfoTabList.add(TouristDestination(item, it))
                }
            }
        }

        val culturalFacilitiesList = async {
            getAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                numOfRows = 5
            )
        }
        culturalFacilitiesList.await()?.let { areaBasedList ->
            areaBasedList.response.body.items.item.forEach { item ->
                getIntroDetail(item.contentId, item.contentTypeId)?.let {
                    tourInfoTabList.add(CulturalFacilities(item, it))
                }
            }
        }

        return@runBlocking tourInfoTabList.toList()
    }

    fun getTourInfoTabListWithTheme(theme: String, areaCode: String): List<TourItem> =
        runBlocking(Dispatchers.IO) {
            val tourInfoTabList = mutableListOf<TourItem>()
            when (theme) {
                "산" -> {
                    Log.d(TAG, "theme = 산, loading AreaBasedLists from API")
                    val mountainThemeList = getMountainThemeList(areaCode)

                    Log.d(TAG, "theme = 산, loading IntroDetailItem from API")
                    mountainThemeList.forEach { areaBasedList ->
                        areaBasedList?.response?.body?.items?.item?.forEach { item ->
                            getIntroDetail(item.contentId, item.contentTypeId)?.let {
                                tourInfoTabList.add(
                                    TouristDestination(item, it)
                                )
                            }
                        }
                    }
                }
                "바다" -> {
                    Log.d(TAG, "theme = 바다, loading AreaBasedLists from API")
                    val seaThemeList = getSeaThemeList(areaCode)

                    Log.d(TAG, "theme = 바다, loading IntroDetailItem from API")
                    seaThemeList.forEach { areaBasedList ->
                        areaBasedList?.response?.body?.items?.item?.forEach { item ->
                            getIntroDetail(item.contentId, item.contentTypeId)?.let {
                                tourInfoTabList.add(
                                    TouristDestination(item, it)
                                )
                            }
                        }
                    }
                }
                "역사" -> {
                    Log.d(TAG, "theme = 역사, loading AreaBasedLists from API")
                    val historicalList = async {
                        getAreaBasedList(
                            areaCode = areaCode,
                            contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                            category1 = TourCategoryId1.HUMANITIES.id,
                            category2 = TourCategoryId2.HISTORICAL_TOURIST_ATTRACTION.id,
                            numOfRows = 10
                        )
                    }
                    Log.d(TAG, "theme = 역사, loading IntroDetailItem from API")
                    historicalList.await()?.let { areaBasedList ->
                        areaBasedList.response.body.items.item.forEach { item ->
                            getIntroDetail(item.contentId, item.contentTypeId)?.let {
                                tourInfoTabList.add(
                                    TouristDestination(item, it)
                                )
                            }
                        }
                    }
                }
                "휴양" -> {
                    Log.d(TAG, "theme = 휴양, loading AreaBasedLists from API")
                    val recreationalList = async {
                        getAreaBasedList(
                            areaCode = areaCode,
                            contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                            category1 = TourCategoryId1.HUMANITIES.id,
                            category2 = TourCategoryId2.RECREATIONAL_TOURIST_ATTRACTION.id,
                            numOfRows = 10
                        )
                    }
                    Log.d(TAG, "theme = 휴양, loading IntroDetailItem from API")
                    recreationalList.await()?.let { areaBasedList ->
                        areaBasedList.response.body.items.item.forEach { item ->
                            getIntroDetail(item.contentId, item.contentTypeId)?.let {
                                tourInfoTabList.add(
                                    TouristDestination(item, it)
                                )
                            }
                        }
                    }
                }

                "체험" -> {
                    Log.d(TAG, "theme = 체험, loading AreaBasedLists from API")
                    val experientialList = async {
                        getAreaBasedList(
                            areaCode = areaCode,
                            contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                            category1 = TourCategoryId1.HUMANITIES.id,
                            category2 = TourCategoryId2.EXPERIENTIAL_TOURIST_ATTRACTION.id,
                            numOfRows = 10
                        )
                    }
                    Log.d(TAG, "theme = 체험, loading IntroDetailItem from API")
                    experientialList.await()?.let { areaBasedList ->
                        areaBasedList.response.body.items.item.forEach { item ->
                            getIntroDetail(item.contentId, item.contentTypeId)?.let {
                                tourInfoTabList.add(
                                    TouristDestination(item, it)
                                )
                            }
                        }
                    }
                }

                "레포츠" -> {
                    Log.d(TAG, "theme = 레포츠, loading AreaBasedLists from API")
                    val leisureSportsList = async {
                        getAreaBasedList(
                            areaCode = areaCode,
                            contentTypeId = TourContentTypeId.LEISURE_SPORTS.contentTypeId,
                            numOfRows = 10
                        )
                    }
                    Log.d(TAG, "theme = 레포츠, loading IntroDetailItem from API")
                    leisureSportsList.await()?.let { areaBasedList ->
                        areaBasedList.response.body.items.item.forEach { item ->
                            getIntroDetail(item.contentId, item.contentTypeId)?.let {
                                tourInfoTabList.add(
                                    TouristDestination(item, it)
                                )
                            }
                        }
                    }
                }

                "문화시설" -> {
                    Log.d(TAG, "theme = 문화시설, loading AreaBasedLists from API")
                    val culturalThemeList = getCulturalThemeList(areaCode)

                    Log.d(TAG, "theme = 문화시설, loading IntroDetailItem from API")
                    culturalThemeList.forEach { areaBasedList ->
                        areaBasedList?.response?.body?.items?.item?.forEach { item ->
                            getIntroDetail(item.contentId, item.contentTypeId)?.let {
                                tourInfoTabList.add(
                                    CulturalFacilities(item, it)
                                )
                            }
                        }
                    }
                }

                else -> {
                    Log.d(TAG, "error! theme does not exist!")
                }
            }

            return@runBlocking tourInfoTabList.toList()
        }

    private fun getMountainThemeList(areaCode: String)
            : List<AreaBasedList?> =
        runBlocking(Dispatchers.IO) {
            val mountainList = async {
                getAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                    category1 = TourCategoryId1.NATURE.id,
                    category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                    category3 = TourCategoryId3.MOUNTAIN.id,
                    numOfRows = 3
                )
            }
            val naturalRecreationForestList = async {
                getAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                    category1 = TourCategoryId1.NATURE.id,
                    category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                    category3 = TourCategoryId3.NATURAL_RECREATION_FOREST.id,
                    numOfRows = 3
                )
            }
            val arboretumList = async {
                getAreaBasedList(
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

    private fun getSeaThemeList(areaCode: String)
            : List<AreaBasedList?> =
        runBlocking(Dispatchers.IO) {
            val coastalSceneryList = async {
                getAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                    category1 = TourCategoryId1.NATURE.id,
                    category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                    category3 = TourCategoryId3.COASTAL_SCENERY.id,
                    numOfRows = 2
                )
            }
            val postList = async {
                getAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                    category1 = TourCategoryId1.NATURE.id,
                    category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                    category3 = TourCategoryId3.PORT.id,
                    numOfRows = 2
                )
            }
            val lightHouseList = async {
                getAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                    category1 = TourCategoryId1.NATURE.id,
                    category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                    category3 = TourCategoryId3.LIGHTHOUSE.id,
                    numOfRows = 2
                )
            }
            val islandList = async {
                getAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                    category1 = TourCategoryId1.NATURE.id,
                    category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                    category3 = TourCategoryId3.ISLAND.id,
                    numOfRows = 2
                )
            }
            val beachList = async {
                getAreaBasedList(
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

    private fun getCulturalThemeList(areaCode: String): List<AreaBasedList?> =
        runBlocking(Dispatchers.IO) {
            val museumList = async {
                getAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                    category1 = TourCategoryId1.HUMANITIES.id,
                    category2 = TourCategoryId2.CULTURAL_FACILITIES.id,
                    category3 = TourCategoryId3.MUSEUM.id,
                    numOfRows = 2
                )
            }
            val memorialHallList = async {
                getAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                    category1 = TourCategoryId1.HUMANITIES.id,
                    category2 = TourCategoryId2.CULTURAL_FACILITIES.id,
                    category3 = TourCategoryId3.MEMORIAL_HALL.id,
                    numOfRows = 2
                )
            }
            val exhibitionList = async {
                getAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                    category1 = TourCategoryId1.HUMANITIES.id,
                    category2 = TourCategoryId2.CULTURAL_FACILITIES.id,
                    category3 = TourCategoryId3.EXHIBITION.id,
                    numOfRows = 2
                )
            }
            val areGalleryList = async {
                getAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                    category1 = TourCategoryId1.HUMANITIES.id,
                    category2 = TourCategoryId2.CULTURAL_FACILITIES.id,
                    category3 = TourCategoryId3.ART_GALLERY.id,
                    numOfRows = 2
                )
            }
            val conventionCenterList = async {
                getAreaBasedList(
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

    fun getRestaurantTabList(areaCode: String): List<TourItem> =
        runBlocking(Dispatchers.IO) {
            val restaurantList = getAreaBasedList(
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
                    getIntroDetail(item.contentId, item.contentTypeId)?.let {
                        restaurantTabList.add(Restaurant(item, it))
                    }
                }
            return@runBlocking restaurantTabList.toList()
        }

    fun getCafeTabList(areaCode: String)

            : List<TourItem> = runBlocking(Dispatchers.IO) {
        val cafeList = getAreaBasedList(
            areaCode = areaCode,
            contentTypeId = TourContentTypeId.RESTAURANT.contentTypeId,
            category1 = TourCategoryId1.FOOD.id,
            category2 = TourCategoryId2.FOOD.id,
            category3 = TourCategoryId3.CAFE_AND_TEA.id,
            numOfRows = 10
        )
        val cafeTabList = mutableListOf<TourItem>()
        cafeList?.response?.body?.items?.item?.forEach { item ->
            getIntroDetail(item.contentId, item.contentTypeId)?.let {
                cafeTabList.add(Restaurant(item, it))
            }
        }
        return@runBlocking cafeTabList.toList()
    }

    fun getEventTabList(areaCode: String): List<TourItem> =
        runBlocking(Dispatchers.IO) {
            val eventList = getAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.EVENT_PERFORMANCE_FESTIVAL.contentTypeId,
                category1 = TourCategoryId1.HUMANITIES.id,
                category2 = TourCategoryId2.PERFORMANCE_EVENT.id,
                numOfRows = 5
            )
            val festivalList = getAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.EVENT_PERFORMANCE_FESTIVAL.contentTypeId,
                category1 = TourCategoryId1.HUMANITIES.id,
                category2 = TourCategoryId2.FESTIVAL.id,
                numOfRows = 5
            )
            val eventTabList = mutableListOf<TourItem>()
            eventList?.response?.body?.items?.item?.forEach { item ->
                getIntroDetail(item.contentId, item.contentTypeId)?.let {
                    eventTabList.add(EventPerformanceFestival(item, it))
                }
            }
            festivalList?.response?.body?.items?.item?.forEach { item ->
                getIntroDetail(item.contentId, item.contentTypeId)?.let {
                    eventTabList.add(EventPerformanceFestival(item, it))
                }
            }
            return@runBlocking eventTabList.toList()
        }

    private suspend fun getAreaBasedList(
        areaCode: String,
        contentTypeId: String,
        category1: String? = null,
        category2: String? = null,
        category3: String? = null,
        numOfRows: Int
    ): AreaBasedList? {
        try {
            val areaBasedResponse = TourNetworkClient.tourNetWork.getAreaBasedList(
                areaCode = areaCode,
                contentTypeId = contentTypeId,
                category1 = category1,
                category2 = category2,
                category3 = category3,
                numOfRows = numOfRows
            )
            if (areaBasedResponse.response.body.totalCount == 0){
                Log.d(TAG, "getAreaBasedList) totalCount = 0")
                return null
            }
            return areaBasedResponse
        } catch (e: Exception) {
            Log.d(TAG, "getAreaBasedList) error!")
            return null
        }
    }

    private suspend fun getIntroDetail(
        contentId: String,
        contentTypeId: String
    ): IntroDetailItem?{
        try {
            val introDetailResponse = TourNetworkClient.tourNetWork.getIntroDetail(
                contentId = contentId,
                contentTypeId = contentTypeId
            )
            if (introDetailResponse.response.body.totalCount == 0) {
                Log.d(TAG, "getIntroDetail) totalCount = 0")
                return null
            }
            return introDetailResponse.response.body.items.item[0]
        }catch (e:Exception){
            Log.d(TAG, "getIntroDetail) error!")
            return null
        }
    }
}