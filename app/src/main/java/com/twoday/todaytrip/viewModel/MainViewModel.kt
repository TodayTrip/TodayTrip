package com.twoday.todaytrip.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.tourApi.TourNetworkInterfaceUtils
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.utils.DestinationData
import com.twoday.todaytrip.utils.DestinationPrefUtil
import com.twoday.todaytrip.utils.TourItemPrefUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val TAG = "MainViewModel"

    private val theme by lazy {
        DestinationPrefUtil.loadTheme()
    }
    private val areaCode by lazy {
        getDestinationAreaCode(
            DestinationPrefUtil.loadDestination()
        )
    }

    private val _touristAttractionList = MutableLiveData<List<TourItem>>()
    val touristAttractionList: LiveData<List<TourItem>>
        get() = _touristAttractionList

    private val _restaurantList = MutableLiveData<List<TourItem>>()
    val restaurantList: LiveData<List<TourItem>>
        get() = _restaurantList

    private val _cafeList = MutableLiveData<List<TourItem>>()
    val cafeList: LiveData<List<TourItem>>
        get() = _cafeList

    private val _eventList = MutableLiveData<List<TourItem>>()
    val eventList: LiveData<List<TourItem>>
        get() = _eventList

    init {
        getTourItemList()
    }

    private fun getDestinationAreaCode(destination: String): String =
        if (destination.isNullOrBlank()) ""
        else DestinationData.destinationAreaCodes[destination] ?: ""

    private fun getTourItemList() {
        loadOrFetchTouristAttractionList()
        loadOrFetchRestaurantList()
        loadOrFetchCafeList()
        loadOrFetchEventList()
    }

    private fun loadOrFetchTouristAttractionList() = CoroutineScope(Dispatchers.IO).launch {
        var touristAttractionList: List<TourItem>? = null
        while(true){
            touristAttractionList = TourItemPrefUtil.loadTouristAttractionList()
            if(!touristAttractionList.isNullOrEmpty())
                break

            fetchAndSaveTouristAttractionList()
        }

        CoroutineScope(Dispatchers.Main).launch {
            _touristAttractionList.value = touristAttractionList!!.toList()
        }
        return@launch
    }
    private fun loadOrFetchRestaurantList() = CoroutineScope(Dispatchers.IO).launch {
        var restaurantList: List<TourItem>? = null
        while(true){
            restaurantList = TourItemPrefUtil.loadRestaurantList()
            if(!restaurantList.isNullOrEmpty())
                break

            fetchAndSaveRestaurantList()
        }

        CoroutineScope(Dispatchers.Main).launch {
            _restaurantList.value = restaurantList!!.toList()
        }
        return@launch
    }
    private fun loadOrFetchCafeList() = CoroutineScope(Dispatchers.IO).launch {
        var cafeList: List<TourItem>? = null
        while(true){
            cafeList = TourItemPrefUtil.loadCafeList()
            if(!cafeList.isNullOrEmpty())
                break

            fetchAndSaveCafeList()
        }

        CoroutineScope(Dispatchers.Main).launch {
            _cafeList.value = cafeList!!.toList()
        }
        return@launch
    }
    private fun loadOrFetchEventList() = CoroutineScope(Dispatchers.IO).launch {
        var eventList: List<TourItem>? = null
        while(true){
            eventList = TourItemPrefUtil.loadEventList()
            if(!eventList.isNullOrEmpty())
                break

            fetchAndSaveEventList()
        }

        CoroutineScope(Dispatchers.Main).launch {
            _eventList.value = eventList!!.toList()
        }
        return@launch
    }

    private suspend fun fetchAndSaveTouristAttractionList() {
        val touristAttractionList = CoroutineScope(Dispatchers.IO).async {
            if (theme.isNullOrBlank())
                TourNetworkInterfaceUtils.fetchTouristAttractionList(areaCode)
            else
                TourNetworkInterfaceUtils.fetchTouristAttractionListWithTheme(theme, areaCode)
        }
        TourItemPrefUtil.saveTouristAttractionList(touristAttractionList.await())
    }
    private suspend fun fetchAndSaveRestaurantList() {
        val restaurantList = CoroutineScope(Dispatchers.IO).async {
            TourNetworkInterfaceUtils.fetchRestaurantTabList(areaCode)
        }
        TourItemPrefUtil.saveRestaurantList(restaurantList.await())
    }
    private suspend fun fetchAndSaveCafeList() {
        val cafeList = CoroutineScope(Dispatchers.IO).async {
            TourNetworkInterfaceUtils.getCafeTabList(areaCode)
        }
        TourItemPrefUtil.saveCafeList(cafeList.await())
    }
    private suspend fun fetchAndSaveEventList() {
        val eventList = CoroutineScope(Dispatchers.IO).async {
            TourNetworkInterfaceUtils.getEventTabList(areaCode)
        }
        TourItemPrefUtil.saveEventList(eventList.await())
    }
}