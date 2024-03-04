package com.twoday.todaytrip.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.tourApi.TourNetworkInterfaceUtils
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.utils.DestinationData
import com.twoday.todaytrip.utils.PrefConstants
import com.twoday.todaytrip.utils.SharedPreferencesUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    private val TAG = "MainViewModel"

    private val theme by lazy{
        loadTheme()
    }
    private val areaCode by lazy{
        loadDestinationAreaCode(loadDestination())
    }


    private val _tourInfoTabList = MutableLiveData<List<TourItem>>()
    val tourInfoTabList: LiveData<List<TourItem>>
        get() = _tourInfoTabList

    private val _restaurantTabList = MutableLiveData<List<TourItem>>()
    val restaurantTabList: LiveData<List<TourItem>>
        get() = _restaurantTabList

    private val _cafeTabList = MutableLiveData<List<TourItem>>()
    val cafeTabList: LiveData<List<TourItem>>
        get() = _cafeTabList

    private val _eventTabList = MutableLiveData<List<TourItem>>()
    val eventTabList: LiveData<List<TourItem>>
        get() = _eventTabList

    init {
        loadTourItemList()
    }

    private fun loadTheme(): String =
        SharedPreferencesUtil.loadDestination(
            MyApplication.appContext!!,
            PrefConstants.THEME_KEY)
            ?: ""

    private fun loadDestination(): String =
        SharedPreferencesUtil.loadDestination(
            MyApplication.appContext!!,
            PrefConstants.DESTINATION_KEY
        ) ?: ""

    private fun loadDestinationAreaCode(destination: String): String =
        if (destination.isNullOrBlank()) ""
        else DestinationData.destinationAreaCodes[destination] ?: ""

    private fun loadTourItemList(){
        _tourInfoTabList.value = SharedPreferencesUtil
            .loadTourItemList(MyApplication.appContext!!, PrefConstants.TOURIST_ATTRACTION_LIST_KEY)
        _restaurantTabList.value = SharedPreferencesUtil
            .loadTourItemList(MyApplication.appContext!!, PrefConstants.RESTAURANT_LIST_KEY)
        _cafeTabList.value = SharedPreferencesUtil
            .loadTourItemList(MyApplication.appContext!!, PrefConstants.CAFE_LIST_KEY)
        _eventTabList.value = SharedPreferencesUtil
            .loadTourItemList(MyApplication.appContext!!, PrefConstants.EVENT_LIST_KEY)
    }

    fun fetchAndSaveTouristAttractionList() {
        val touristAttractionList =
            if (theme.isNullOrBlank())
                TourNetworkInterfaceUtils.fetchTouristAttractionList(areaCode)
            else
                TourNetworkInterfaceUtils.fetchTouristAttractionListWithTheme(theme, areaCode)

        saveTouristAttractionList(touristAttractionList)
    }
    private fun saveTouristAttractionList(touristAttractionList:List<TourItem>) =
        SharedPreferencesUtil.saveTourItemList(
            MyApplication.appContext!!,
            touristAttractionList,
            PrefConstants.TOURIST_ATTRACTION_LIST_KEY
        )

    fun fetchAndSaveRestaurantList() {
        val restaurantList = TourNetworkInterfaceUtils.fetchRestaurantTabList(areaCode)
        saveRestaurantList(restaurantList)
    }
    private fun saveRestaurantList(restaurantList:List<TourItem>) =
        SharedPreferencesUtil.saveTourItemList(
            MyApplication.appContext!!,
            restaurantList,
            PrefConstants.RESTAURANT_LIST_KEY
        )

    fun fetchAndSaveCafeList() {
        val cafeList = TourNetworkInterfaceUtils.getCafeTabList(areaCode)
        saveCafeList(cafeList)

    }
    private fun saveCafeList(cafeList: List<TourItem>) =
        SharedPreferencesUtil.saveTourItemList(
            MyApplication.appContext!!,
            cafeList,
            PrefConstants.CAFE_LIST_KEY
        )

    fun fetchAndSaveEventList() {
        val eventList = TourNetworkInterfaceUtils.getEventTabList(areaCode)
        saveEventList(eventList)
    }
    private fun saveEventList(eventList:List<TourItem>) =
        SharedPreferencesUtil.saveTourItemList(
            MyApplication.appContext!!,
            eventList,
            PrefConstants.EVENT_LIST_KEY
        )
}