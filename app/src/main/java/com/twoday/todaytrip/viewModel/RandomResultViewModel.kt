package com.twoday.todaytrip.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.tourApi.TourNetworkInterfaceUtils
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.utils.DestinationData
import com.twoday.todaytrip.utils.PrefConstants
import com.twoday.todaytrip.utils.SharedPreferencesUtil
import com.twoday.todaytrip.utils.TourItemSharedPreferenceUtil
import com.twoday.todaytrip.utils.TourItemSharedPreferenceUtil.saveCafeList
import com.twoday.todaytrip.utils.TourItemSharedPreferenceUtil.saveEventList
import com.twoday.todaytrip.utils.TourItemSharedPreferenceUtil.saveRestaurantList
import com.twoday.todaytrip.utils.TourItemSharedPreferenceUtil.saveTouristAttractionList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RandomResultViewModel : ViewModel() {
    private val TAG = "RandomResultViewModel"
    private val theme by lazy{
        loadTheme()
    }
    private val areaCode by lazy{
        loadDestinationAreaCode(loadDestination())
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

    private fun loadTheme(): String =
        SharedPreferencesUtil.loadDestination(MyApplication.appContext!!, PrefConstants.THEME_KEY)
            ?: ""
    private fun loadDestination(): String =
        SharedPreferencesUtil.loadDestination(
            MyApplication.appContext!!,
            PrefConstants.DESTINATION_KEY
        ) ?: ""
    private fun loadDestinationAreaCode(destination: String?): String =
        if (destination.isNullOrBlank()) ""
        else DestinationData.destinationAreaCodes[destination] ?: ""

    private fun fetchAndSaveTouristAttractionList() {
        val touristAttractionList =
            if (theme.isNullOrBlank())
                TourNetworkInterfaceUtils.fetchTouristAttractionList(areaCode)
            else
                TourNetworkInterfaceUtils.fetchTouristAttractionListWithTheme(theme, areaCode)
        saveTouristAttractionList(touristAttractionList)
    }

    private fun fetchAndSaveRestaurantList() {
        val restaurantList = TourNetworkInterfaceUtils.fetchRestaurantTabList(areaCode)
        saveRestaurantList(restaurantList)
    }
    private fun fetchAndSaveCafeList() {
        val cafeList = TourNetworkInterfaceUtils.getCafeTabList(areaCode)
        saveCafeList(cafeList)

    }
    private fun fetchAndSaveEventList() {
        val eventList = TourNetworkInterfaceUtils.getEventTabList(areaCode)
        saveEventList(eventList)
    }
}