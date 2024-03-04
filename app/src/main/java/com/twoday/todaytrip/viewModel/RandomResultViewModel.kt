package com.twoday.todaytrip.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.tourApi.TourNetworkInterfaceUtils
import com.twoday.todaytrip.utils.DestinationData
import com.twoday.todaytrip.utils.DestinationPrefUtil
import com.twoday.todaytrip.utils.TourItemPrefUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RandomResultViewModel : ViewModel() {
    private val TAG = "RandomResultViewModel"

    private val theme by lazy{
        DestinationPrefUtil.loadTheme()
    }
    private val areaCode by lazy{
        getDestinationAreaCode(
            DestinationPrefUtil.loadDestination()
        )
    }

    private val _isTourDataListReady = MutableLiveData<Boolean>()
    val isTourDataListReady: LiveData<Boolean>
        get() = _isTourDataListReady

    init {
        _isTourDataListReady.value = false
        CoroutineScope(Dispatchers.IO).launch {
            fetchAndSaveTourItemList()
        }
    }

    private suspend fun fetchAndSaveTourItemList() {
        val touristAttractionJob = CoroutineScope(Dispatchers.IO).launch {
            fetchAndSaveTouristAttractionList()
        }
        val restaurantListJob = CoroutineScope(Dispatchers.IO).launch {
            fetchAndSaveRestaurantList()
        }
        val cafeListJob = CoroutineScope(Dispatchers.IO).launch {
            fetchAndSaveCafeList()
        }
        val eventListJob = CoroutineScope(Dispatchers.IO).launch {
            fetchAndSaveEventList()
        }

        touristAttractionJob.join()
        restaurantListJob.join()
        cafeListJob.join()
        eventListJob.join()

        withContext(Dispatchers.Main) {
            _isTourDataListReady.value = true
        }
    }

    private fun getDestinationAreaCode(destination: String?): String =
        if (destination.isNullOrBlank()) ""
        else DestinationData.destinationAreaCodes[destination] ?: ""

    private fun fetchAndSaveTouristAttractionList() {
        val touristAttractionList =
            if (theme.isNullOrBlank())
                TourNetworkInterfaceUtils.fetchTouristAttractionList(areaCode)
            else
                TourNetworkInterfaceUtils.fetchTouristAttractionListWithTheme(theme, areaCode)
        TourItemPrefUtil.saveTouristAttractionList(touristAttractionList)
    }

    private fun fetchAndSaveRestaurantList() {
        val restaurantList = TourNetworkInterfaceUtils.fetchRestaurantTabList(areaCode)
        TourItemPrefUtil.saveRestaurantList(restaurantList)
    }
    private fun fetchAndSaveCafeList() {
        val cafeList = TourNetworkInterfaceUtils.getCafeTabList(areaCode)
        TourItemPrefUtil.saveCafeList(cafeList)
    }
    private fun fetchAndSaveEventList() {
        val eventList = TourNetworkInterfaceUtils.getEventTabList(areaCode)
        TourItemPrefUtil.saveEventList(eventList)
    }
}