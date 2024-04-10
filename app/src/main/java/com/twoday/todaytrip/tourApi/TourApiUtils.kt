package com.twoday.todaytrip.tourApi

import android.util.Log
import com.twoday.todaytrip.tourData.TourCategoryId1
import com.twoday.todaytrip.tourData.TourCategoryId2
import com.twoday.todaytrip.tourData.TourCategoryId3
import com.twoday.todaytrip.tourData.TourContentTypeId
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.tourData.TourItemDTOConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

object TourApiUtils {
    private val TAG = "TourApiUtils"
    suspend fun fetchTouristAttractionList(areaCode: String, pageNo: Int): List<TourItem> =
        CoroutineScope(Dispatchers.IO).async {
            val touristAttractionList = mutableListOf<TourItem>()

            withContext(Dispatchers.Default) {
                fetchAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                    numOfRows = 5,
                    pageNo = pageNo
                )
            }?.let { areaBasedList ->
                val itemList = areaBasedList.response.body.items.item
                val deferredList = itemList.map { item ->
                    async {
                        fetchIntroDetail(item.contentId, item.contentTypeId)
                    }
                }
                deferredList.forEachIndexed { index, deferred ->
                    deferred.await()?.let {
                        touristAttractionList.add(TourItem.TouristDestination(itemList[index], it))
                    }
                }
            }
            withContext(Dispatchers.Default)
            {
                fetchAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                    numOfRows = 5,
                    pageNo = pageNo
                )
            }?.let { areaBasedList ->
                val itemList = areaBasedList.response.body.items.item
                val deferredList = itemList.map { item ->
                    async {
                        fetchIntroDetail(item.contentId, item.contentTypeId)
                    }
                }
                deferredList.forEachIndexed { index, deferred ->
                    deferred.await()?.let {
                        touristAttractionList.add(TourItem.CulturalFacilities(itemList[index], it))
                    }
                }
            }
            return@async touristAttractionList
        }.await().toList()

    suspend fun fetchTouristAttractionListWithTheme(
        theme: String,
        areaCode: String,
        pageNo: Int
    ): List<TourItem> = CoroutineScope(Dispatchers.IO).async {
        val touristAttractionList = mutableListOf<TourItem>()
        when (theme) {
            "산" -> {
                val mountainThemeList = async {
                    fetchMountainThemeList(areaCode, pageNo)
                }.await()
                mountainThemeList.forEach { areaBasedList ->
                    val itemList = areaBasedList?.response?.body?.items?.item
                    val deferredList = itemList?.map { item ->
                        async {
                            fetchIntroDetail(item.contentId, item.contentTypeId)
                        }
                    }
                    deferredList?.forEachIndexed { index, deferred ->
                        deferred.await()?.let {
                            touristAttractionList.add(
                                TourItem.TouristDestination(
                                    itemList[index],
                                    it
                                )
                            )
                        }
                    }
                }
            }

            "바다" -> {
                val seaThemeList = async {
                    fetchSeaThemeList(areaCode, pageNo)
                }.await()
                seaThemeList.forEach { areaBasedList ->
                    val itemList = areaBasedList?.response?.body?.items?.item
                    val deferredList = itemList?.map { item ->
                        async {
                            fetchIntroDetail(item.contentId, item.contentTypeId)
                        }
                    }
                    deferredList?.forEachIndexed { index, deferred ->
                        deferred.await()?.let {
                            touristAttractionList.add(
                                TourItem.TouristDestination(
                                    itemList[index],
                                    it
                                )
                            )
                        }
                    }
                }
            }

            "역사" -> {
                val historicalTheme = async {
                    fetchHistoricalTheme(areaCode, pageNo)
                }.await()
                historicalTheme?.let { areaBasedList ->
                    val itemList = areaBasedList.response.body.items.item
                    val deferredList = itemList.map { item ->
                        async {
                            fetchIntroDetail(item.contentId, item.contentTypeId)
                        }
                    }
                    deferredList.forEachIndexed { index, deferred ->
                        deferred.await()?.let {
                            touristAttractionList.add(
                                TourItem.TouristDestination(
                                    itemList[index],
                                    it
                                )
                            )
                        }
                    }
                }
            }

            "휴양" -> {
                val recreationalTheme = async {
                    fetchRecreationalTheme(areaCode, pageNo)
                }.await()
                recreationalTheme?.let { areaBasedList ->
                    val itemList = areaBasedList.response.body.items.item
                    val deferredList = itemList.map { item ->
                        async {
                            fetchIntroDetail(item.contentId, item.contentTypeId)
                        }
                    }
                    deferredList.forEachIndexed { index, deferred ->
                        deferred.await()?.let {
                            touristAttractionList.add(
                                TourItem.TouristDestination(
                                    itemList[index],
                                    it
                                )
                            )
                        }
                    }
                }
            }

            "체험" -> {
                val experimentalTheme = async {
                    fetchExperientialTheme(areaCode, pageNo)
                }.await()
                experimentalTheme?.let { areaBasedList ->
                    val itemList = areaBasedList.response.body.items.item
                    val deferredList = itemList.map { item ->
                        async {
                            fetchIntroDetail(item.contentId, item.contentTypeId)
                        }
                    }
                    deferredList.forEachIndexed { index, deferred ->
                        deferred.await()?.let {
                            touristAttractionList.add(
                                TourItem.TouristDestination(
                                    itemList[index],
                                    it
                                )
                            )
                        }
                    }
                }
            }

            "레포츠" -> {
                val leisureSportsTheme = async {
                    fetchLeisureSportsTheme(areaCode, pageNo)
                }.await()
                leisureSportsTheme?.let { areaBasedList ->
                    val itemList = areaBasedList.response.body.items.item
                    val deferredList = itemList.map { item ->
                        async {
                            fetchIntroDetail(item.contentId, item.contentTypeId)
                        }
                    }
                    deferredList.forEachIndexed { index, deferred ->
                        deferred.await()?.let {
                            touristAttractionList.add(TourItem.LeisureSports(itemList[index], it))
                        }
                    }
                }
            }

            "문화시설" -> {
                val culturalThemeList = async {
                    fetchCulturalThemeList(areaCode, pageNo)
                }.await()
                culturalThemeList.forEach { areaBasedList ->
                    val itemList = areaBasedList?.response?.body?.items?.item
                    val deferredList = itemList?.map { item ->
                        async {
                            fetchIntroDetail(item.contentId, item.contentTypeId)
                        }
                    }
                    deferredList?.forEachIndexed { index, deferred ->
                        deferred.await()?.let {
                            touristAttractionList.add(
                                TourItem.CulturalFacilities(
                                    itemList[index],
                                    it
                                )
                            )
                        }
                    }
                }
            }

            else -> {
                Log.d(TAG, "error! theme does not exist!")
            }
        }
        return@async touristAttractionList
    }.await().toList()

    private suspend fun fetchMountainThemeList(areaCode: String, pageNo: Int)
            : List<AreaBasedList?> = CoroutineScope(Dispatchers.IO).async {
        val mountainList = async {
            fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                category1 = TourCategoryId1.NATURE.id,
                category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                category3 = TourCategoryId3.MOUNTAIN.id,
                numOfRows = 3,
                pageNo = pageNo
            )
        }
        val naturalRecreationForestList = async {
            fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                category1 = TourCategoryId1.NATURE.id,
                category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                category3 = TourCategoryId3.NATURAL_RECREATION_FOREST.id,
                numOfRows = 3,
                pageNo = pageNo
            )
        }
        val arboretumList = async {
            fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                category1 = TourCategoryId1.NATURE.id,
                category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                category3 = TourCategoryId3.ARBORETUM.id,
                numOfRows = 3,
                pageNo = pageNo
            )
        }
        return@async listOf(
            mountainList.await(),
            naturalRecreationForestList.await(),
            arboretumList.await()
        )
    }.await()

    private suspend fun fetchSeaThemeList(areaCode: String, pageNo: Int)
            : List<AreaBasedList?> = CoroutineScope(Dispatchers.IO).async {
        val coastalSceneryList = async {
            fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                category1 = TourCategoryId1.NATURE.id,
                category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                category3 = TourCategoryId3.COASTAL_SCENERY.id,
                numOfRows = 2,
                pageNo = pageNo
            )
        }
        val portList = async {
            fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                category1 = TourCategoryId1.NATURE.id,
                category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                category3 = TourCategoryId3.PORT.id,
                numOfRows = 2,
                pageNo = pageNo
            )
        }
        val lightHouseList = async {
            fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                category1 = TourCategoryId1.NATURE.id,
                category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                category3 = TourCategoryId3.LIGHTHOUSE.id,
                numOfRows = 2,
                pageNo = pageNo
            )
        }
        val islandList = async {
            fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                category1 = TourCategoryId1.NATURE.id,
                category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                category3 = TourCategoryId3.ISLAND.id,
                numOfRows = 2,
                pageNo = pageNo
            )
        }
        val beachList = async {
            fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                category1 = TourCategoryId1.NATURE.id,
                category2 = TourCategoryId2.NATURE_TOURIST_ATTRACTION.id,
                category3 = TourCategoryId3.BEACH.id,
                numOfRows = 2,
                pageNo = pageNo
            )
        }
        return@async listOf(
            coastalSceneryList.await(),
            portList.await(),
            lightHouseList.await(),
            islandList.await(),
            beachList.await()
        )
    }.await()

    private suspend fun fetchHistoricalTheme(areaCode: String, pageNo: Int): AreaBasedList? =
        CoroutineScope(Dispatchers.IO).async {
            return@async fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                category1 = TourCategoryId1.HUMANITIES.id,
                category2 = TourCategoryId2.HISTORICAL_TOURIST_ATTRACTION.id,
                numOfRows = 10,
                pageNo = pageNo
            )
        }.await()

    private suspend fun fetchRecreationalTheme(areaCode: String, pageNo: Int): AreaBasedList? =
        CoroutineScope(Dispatchers.IO).async {
            return@async fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                category1 = TourCategoryId1.HUMANITIES.id,
                category2 = TourCategoryId2.RECREATIONAL_TOURIST_ATTRACTION.id,
                numOfRows = 10,
                pageNo = pageNo
            )
        }.await()

    private suspend fun fetchExperientialTheme(areaCode: String, pageNo: Int): AreaBasedList? =
        CoroutineScope(Dispatchers.IO).async {
            return@async fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
                category1 = TourCategoryId1.HUMANITIES.id,
                category2 = TourCategoryId2.EXPERIENTIAL_TOURIST_ATTRACTION.id,
                numOfRows = 10,
                pageNo = pageNo
            )
        }.await()

    private suspend fun fetchLeisureSportsTheme(areaCode: String, pageNo: Int): AreaBasedList? =
        CoroutineScope(Dispatchers.IO).async {
            return@async fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.LEISURE_SPORTS.contentTypeId,
                numOfRows = 10,
                pageNo = pageNo
            )
        }.await()

    private suspend fun fetchCulturalThemeList(
        areaCode: String,
        pageNo: Int
    ): List<AreaBasedList?> =
        CoroutineScope(Dispatchers.IO).async {
            val museumList = async {
                fetchAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                    category1 = TourCategoryId1.HUMANITIES.id,
                    category2 = TourCategoryId2.CULTURAL_FACILITIES.id,
                    category3 = TourCategoryId3.MUSEUM.id,
                    numOfRows = 2,
                    pageNo = pageNo
                )
            }
            val memorialHallList = async {
                fetchAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                    category1 = TourCategoryId1.HUMANITIES.id,
                    category2 = TourCategoryId2.CULTURAL_FACILITIES.id,
                    category3 = TourCategoryId3.MEMORIAL_HALL.id,
                    numOfRows = 2,
                    pageNo = pageNo
                )
            }
            val exhibitionList = async {
                fetchAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                    category1 = TourCategoryId1.HUMANITIES.id,
                    category2 = TourCategoryId2.CULTURAL_FACILITIES.id,
                    category3 = TourCategoryId3.EXHIBITION.id,
                    numOfRows = 2,
                    pageNo = pageNo
                )
            }
            val areGalleryList = async {
                fetchAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                    category1 = TourCategoryId1.HUMANITIES.id,
                    category2 = TourCategoryId2.CULTURAL_FACILITIES.id,
                    category3 = TourCategoryId3.ART_GALLERY.id,
                    numOfRows = 2,
                    pageNo = pageNo
                )
            }
            val conventionCenterList = async {
                fetchAreaBasedList(
                    areaCode = areaCode,
                    contentTypeId = TourContentTypeId.CULTURAL_FACILITIES.contentTypeId,
                    category1 = TourCategoryId1.HUMANITIES.id,
                    category2 = TourCategoryId2.CULTURAL_FACILITIES.id,
                    category3 = TourCategoryId3.CONVENTION_CENTER.id,
                    numOfRows = 2,
                    pageNo = pageNo
                )
            }
            return@async listOf(
                museumList.await(),
                memorialHallList.await(),
                exhibitionList.await(),
                areGalleryList.await(),
                conventionCenterList.await()
            )
        }.await()

    suspend fun fetchRestaurantTabList(areaCode: String, pageNo: Int): List<TourItem> =
        CoroutineScope(Dispatchers.IO).async {
            val restaurantList = fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.RESTAURANT.contentTypeId,
                numOfRows = 15,
                pageNo = pageNo
            )
            val restaurantTabList = mutableListOf<TourItem>()

            val itemList = restaurantList?.response?.body?.items?.item?.filter {
                (!it.category3.isNullOrBlank()) && (it.category3 != TourCategoryId3.CAFE_AND_TEA.id)
            }
            val deferredList = itemList?.map { item ->
                async {
                    fetchIntroDetail(item.contentId, item.contentTypeId)
                }
            }
            deferredList?.forEachIndexed { index, deferred ->
                deferred.await()?.let {
                    restaurantTabList.add(TourItem.Restaurant(itemList[index], it))
                }
            }
            return@async restaurantTabList
        }.await().toList()

    suspend fun fetchCafeTabList(areaCode: String, pageNo: Int)
            : List<TourItem> = CoroutineScope(Dispatchers.IO).async {
        val cafeList = fetchAreaBasedList(
            areaCode = areaCode,
            contentTypeId = TourContentTypeId.RESTAURANT.contentTypeId,
            category1 = TourCategoryId1.FOOD.id,
            category2 = TourCategoryId2.FOOD.id,
            category3 = TourCategoryId3.CAFE_AND_TEA.id,
            numOfRows = 10,
            pageNo = pageNo
        )
        val cafeTabList = mutableListOf<TourItem>()

        val itemList = cafeList?.response?.body?.items?.item
        val deferredList = itemList?.map { item ->
            async {
                fetchIntroDetail(item.contentId, item.contentTypeId)
            }
        }
        deferredList?.forEachIndexed { index, deferred ->
            deferred.await()?.let {
                cafeTabList.add(TourItem.Restaurant(itemList[index], it))
            }
        }
        return@async cafeTabList
    }.await().toList()

    suspend fun fetchEventTabList(areaCode: String, pageNo: Int): List<TourItem> =
        CoroutineScope(Dispatchers.IO).async {
            val eventList = fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.EVENT_PERFORMANCE_FESTIVAL.contentTypeId,
                category1 = TourCategoryId1.HUMANITIES.id,
                category2 = TourCategoryId2.PERFORMANCE_EVENT.id,
                numOfRows = 5,
                pageNo = pageNo
            )
            val festivalList = fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = TourContentTypeId.EVENT_PERFORMANCE_FESTIVAL.contentTypeId,
                category1 = TourCategoryId1.HUMANITIES.id,
                category2 = TourCategoryId2.FESTIVAL.id,
                numOfRows = 5,
                pageNo = pageNo
            )
            val eventTabList = mutableListOf<TourItem>()

            val eventItemList = eventList?.response?.body?.items?.item
            val eventDeferredList = eventItemList?.map { item ->
                async {
                    fetchIntroDetail(item.contentId, item.contentTypeId)
                }
            }
            val festivalItemList = festivalList?.response?.body?.items?.item
            val festivalDeferredList = festivalItemList?.map { item ->
                async {
                    fetchIntroDetail(item.contentId, item.contentTypeId)
                }
            }

            eventDeferredList?.forEachIndexed { index, deferred ->
                deferred.await()?.let {
                    eventTabList.add(TourItem.EventPerformanceFestival(eventItemList[index], it))
                }
            }
            festivalDeferredList?.forEachIndexed { index, deferred ->
                deferred.await()?.let {
                    eventTabList.add(TourItem.EventPerformanceFestival(festivalItemList[index], it))
                }
            }
            return@async eventTabList.toList()
        }.await()

    suspend fun fetchNearByList(tourItem: TourItem): List<TourItem> =
        CoroutineScope(Dispatchers.IO).async {
            val nearByList = mutableListOf<TourItem>()

            val locationBasedList = fetchLocationBasedList(
                mapX = tourItem.getLongitude(),
                mapY = tourItem.getLatitude()
            )
            Log.d(TAG, "fetchNearByList) locationBasedList is null: ${locationBasedList == null}")

            val itemList = locationBasedList?.response?.body?.items?.item
            val deferredList = itemList?.map { item ->
                async {
                    fetchIntroDetail(item.contentid, item.contenttypeid)
                }
            }
            deferredList?.forEachIndexed { index, deferred ->
                deferred.await()?.let {
                    when (it.contentTypeId) {
                        TourContentTypeId.TOURIST_DESTINATION.contentTypeId -> {
                            nearByList.add(
                                TourItem.TouristDestination(
                                    TourItemDTOConverter.getAreaBasedFromLocationBased(itemList[index]),
                                    it
                                )
                            )
                        }

                        TourContentTypeId.CULTURAL_FACILITIES.contentTypeId -> {
                            nearByList.add(
                                TourItem.CulturalFacilities(
                                    TourItemDTOConverter.getAreaBasedFromLocationBased(itemList[index]),
                                    it
                                )
                            )
                        }

                        TourContentTypeId.LEISURE_SPORTS.contentTypeId -> {
                            nearByList.add(
                                TourItem.LeisureSports(
                                    TourItemDTOConverter.getAreaBasedFromLocationBased(itemList[index]),
                                    it
                                )
                            )
                        }

                        TourContentTypeId.RESTAURANT.contentTypeId -> {
                            nearByList.add(
                                TourItem.Restaurant(
                                    TourItemDTOConverter.getAreaBasedFromLocationBased(itemList[index]),
                                    it
                                )
                            )
                        }

                        TourContentTypeId.EVENT_PERFORMANCE_FESTIVAL.contentTypeId -> {
                            nearByList.add(
                                TourItem.EventPerformanceFestival(
                                    TourItemDTOConverter.getAreaBasedFromLocationBased(itemList[index]),
                                    it
                                )
                            )
                        }

                        else -> {
                            // do nothing
                        }
                    }
                }
            }
            return@async nearByList
        }.await()

    private suspend fun fetchAreaBasedList(
        areaCode: String,
        contentTypeId: String,
        category1: String? = null,
        category2: String? = null,
        category3: String? = null,
        numOfRows: Int,
        pageNo: Int
    ): AreaBasedList? {
        try {
            val areaBasedResponse = TourNetworkClient.tourNetWork.fetchAreaBasedList(
                areaCode = areaCode,
                contentTypeId = contentTypeId,
                category1 = category1,
                category2 = category2,
                category3 = category3,
                numOfRows = numOfRows,
                pageNo = pageNo
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

    private suspend fun fetchLocationBasedList(
        mapX: String,
        mapY: String
    ): LocationBasedList? {
        try {
            val locationBasedResponse = TourNetworkClient.tourNetWork.fetchLocationBasedList(
                mapX = mapX,
                mapY = mapY,
                radius = 20000,
                numOfRows = 10
            )
            if (locationBasedResponse.response.body.totalCount == 0) {
                Log.d(TAG, "getLocationBasedList) totalCount = 0")
                return null
            }
            locationBasedResponse.response.body.items.item
                .toMutableList()
                .run {
                    val filteredList = this
                        .filter {
                            (!it.mapx.isNullOrBlank()) && (!it.mapy.isNullOrBlank())
                        }
                        .filter {
                            (it.contenttypeid != TourContentTypeId.TRAVEL_COURSE.contentTypeId)
                                    && (it.contenttypeid != TourContentTypeId.LODGEMENT.contentTypeId)
                                    && (it.contenttypeid != TourContentTypeId.SHOPPING.contentTypeId)
                        }
                    clear()
                    addAll(filteredList)
                }
            return locationBasedResponse
        } catch (e: Exception) {
            Log.d(TAG, "getLocationBasedList) error!")
            return null
        }
    }

    private suspend fun fetchIntroDetail(
        contentId: String,
        contentTypeId: String
    ): IntroDetailItem? {
        Log.d(TAG, "fetchIntroDetail) contentId: ${contentId}")
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