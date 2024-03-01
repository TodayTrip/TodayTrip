package com.twoday.todaytrip.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.tourApi.AreaBasedList
import com.twoday.todaytrip.tourApi.IntroDetailItem
import com.twoday.todaytrip.tourApi.TourNetworkClient
import com.twoday.todaytrip.tourData.CulturalFacilities
import com.twoday.todaytrip.tourData.Restaurant
import com.twoday.todaytrip.tourData.TourCategoryId1
import com.twoday.todaytrip.tourData.TourCategoryId2
import com.twoday.todaytrip.tourData.TourCategoryId3
import com.twoday.todaytrip.tourData.TourContentTypeId
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.tourData.TouristDestination
import com.twoday.todaytrip.utils.DestinationData
import com.twoday.todaytrip.utils.PrefConstants
import com.twoday.todaytrip.utils.PrefConstants.DESTINATION_KEY
import com.twoday.todaytrip.utils.PrefConstants.TOUR_INFO_TAB_LIST_KEY
import com.twoday.todaytrip.utils.SharedPreferencesUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MainViewModel : ViewModel() {
    private val TAG = "MainViewModel"

    private var destAreaCode: String? = null

    private val _tourInfoTabList = MutableLiveData<List<TourItem>>()
    val tourInfoTabList: LiveData<List<TourItem>>
        get() = _tourInfoTabList
    private val _restaurantTabList = MutableLiveData<List<TourItem>>()
    val restaurantTabList: LiveData<List<TourItem>>
        get() = _restaurantTabList

    init {
        loadTourItemList()
    }

    private fun loadTourItemList() {
        destAreaCode = getDestinationAreaCode(getDestination())
        if (destAreaCode.isNullOrBlank()) {
            Log.d(TAG, "loadTourITemList) error! no destination area code!")
            return
        }

        // TODO shared preference에 저장된 정보 있는지 확인하기
        // TODO 완전 랜덤인 지, 테마가 있는지 확인하기

        loadTourInfoTabList()
        loadRestaurantTabList()
        //loadCafeTabList()
        //loadEventPerformanceFestivalTabList()
    }

    private fun getDestination(): String? =
        SharedPreferencesUtil.loadDestination(MyApplication.appContext!!, DESTINATION_KEY) ?: null

    private fun getDestinationAreaCode(destination: String?): String? =
        if (destination == null) null
        else DestinationData.destinationAreaCodes[destination] ?: null

    private fun loadTourInfoTabList() {
        // TODO 테마가 있는 경우, 관광지 탭에 테마에 해당되는 정보만 필터링하기
        _tourInfoTabList.value = getTourInfoTabList()
        SharedPreferencesUtil.saveTourItemList(
            MyApplication.appContext!!,
            _tourInfoTabList.value!!,
            TOUR_INFO_TAB_LIST_KEY
        )
    }

    private fun getTourInfoTabList(): List<TourItem> = runBlocking(Dispatchers.IO) {
        val touristDestinationList: AreaBasedList = TourNetworkClient.tourNetWork.getAreaBasedList(
            areaCode = destAreaCode,
            contentTypeId = TourContentTypeId.TOURIST_DESTINATION.contentTypeId,
            numOfRows = 5
        )
        val culturalFacilitiesList: AreaBasedList = TourNetworkClient.tourNetWork.getAreaBasedList(
            areaCode = destAreaCode,
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

    private fun loadRestaurantTabList() {
        _restaurantTabList.value = getRestaurantTabList()
        SharedPreferencesUtil.saveTourItemList(
            MyApplication.appContext!!,
            _restaurantTabList.value!!,
            PrefConstants.RESTAURANT_TAB_LIST_KEY
        )
    }

    private fun getRestaurantTabList(): List<TourItem> = runBlocking(Dispatchers.IO) {
        val restaurantList = listOf(
            TourNetworkClient.tourNetWork.getAreaBasedList(
                areaCode = destAreaCode,
                contentTypeId = TourContentTypeId.RESTAURANT.contentTypeId,
                category1 = TourCategoryId1.FOOD.id,
                category2 = TourCategoryId2.FOOD.id,
                category3 = TourCategoryId3.KOREAN_FOOD.id,
                numOfRows = 2
            ),
            TourNetworkClient.tourNetWork.getAreaBasedList(
                areaCode = destAreaCode,
                contentTypeId = TourContentTypeId.RESTAURANT.contentTypeId,
                category1 = TourCategoryId1.FOOD.id,
                category2 = TourCategoryId2.FOOD.id,
                category3 = TourCategoryId3.WESTERN_FOOD.id,
                numOfRows = 2
            ),
            TourNetworkClient.tourNetWork.getAreaBasedList(
                areaCode = destAreaCode,
                contentTypeId = TourContentTypeId.RESTAURANT.contentTypeId,
                category1 = TourCategoryId1.FOOD.id,
                category2 = TourCategoryId2.FOOD.id,
                category3 = TourCategoryId3.JAPANESE_FOOD.id,
                numOfRows = 2
            ),
            TourNetworkClient.tourNetWork.getAreaBasedList(
                areaCode = destAreaCode,
                contentTypeId = TourContentTypeId.RESTAURANT.contentTypeId,
                category1 = TourCategoryId1.FOOD.id,
                category2 = TourCategoryId2.FOOD.id,
                category3 = TourCategoryId3.CHINESE_FOOD.id,
                numOfRows = 2
            ),
            TourNetworkClient.tourNetWork.getAreaBasedList(
                areaCode = destAreaCode,
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