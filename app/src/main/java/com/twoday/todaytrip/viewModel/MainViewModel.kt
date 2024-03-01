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
import com.twoday.todaytrip.utils.PrefConstants.DESTINATION_KEY
import com.twoday.todaytrip.utils.PrefConstants.THEME_KEY
import com.twoday.todaytrip.utils.SharedPreferencesUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    private val TAG = "MainViewModel"

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
        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG, "call loadTourItemList()")
            loadTourItemList()
        }
    }

    //TODO 새로운 여행지 선택 시, Shared Preference에 저장된 관광지 정보 삭제

    private fun loadTourItemList() {
        val theme = getTheme()
        val areaCode = getDestinationAreaCode(getDestination())
        if (areaCode.isNullOrBlank()) {
            Log.d(TAG, "loadTourITemList) error! no destination area code!")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG, "call loadTourInfoTabList()")
            loadTourInfoTabList(theme, areaCode!!)
        }
        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG, "call loadRestaurantTabList()")
            loadRestaurantTabList(areaCode!!)
        }
        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG, "call loadCafeTabList()")
            loadCafeTabList(areaCode!!)
        }
        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG, "call loadEventTabList()")
            loadEventTabList(areaCode!!)
        }
    }

    private fun getTheme(): String? =
        SharedPreferencesUtil.loadDestination(MyApplication.appContext!!, THEME_KEY)?:null
    private fun getDestination(): String? =
        SharedPreferencesUtil.loadDestination(MyApplication.appContext!!, DESTINATION_KEY) ?: null

    private fun getDestinationAreaCode(destination: String?): String? =
        if (destination == null) null
        else DestinationData.destinationAreaCodes[destination] ?: null

    private suspend fun loadTourInfoTabList(theme:String?, areaCode:String) = withContext(Dispatchers.Main) {
        val loadedTourInfoList = SharedPreferencesUtil.loadTourItemList(
            MyApplication.appContext!!,
            PrefConstants.TOUR_INFO_TAB_LIST_KEY
        )
        if(loadedTourInfoList != emptyList<TourItem>()){
            Log.d(TAG, "load TourInfoList from Shared Preferences")
            _tourInfoTabList.value = loadedTourInfoList
            return@withContext
        }
        Log.d(TAG, "load TourInfoList from Tour API")
        _tourInfoTabList.value =
            if(theme == null) TourNetworkInterfaceUtils.getTourInfoTabList(areaCode)
            else TourNetworkInterfaceUtils.getTourInfoTabListWithTheme(theme, areaCode)
        Log.d(TAG, "loaded TourInfoList from Tour API, size: ${_tourInfoTabList.value!!.size}")

        Log.d(TAG, "save TourInfoList to SharedPreferences")
        SharedPreferencesUtil.saveTourItemList(
            MyApplication.appContext!!,
            _tourInfoTabList.value!!,
            PrefConstants.TOUR_INFO_TAB_LIST_KEY
        )
    }

    private suspend fun loadRestaurantTabList(areaCode:String) = withContext(Dispatchers.Main) {
        val loadedRestaurantList = SharedPreferencesUtil.loadTourItemList(
            MyApplication.appContext!!,
            PrefConstants.RESTAURANT_TAB_LIST_KEY
        )
        if(loadedRestaurantList != emptyList<TourItem>()){
            Log.d(TAG, "load RestaurantList from SharedPreferences")
            _restaurantTabList.value = loadedRestaurantList
            return@withContext
        }
        Log.d(TAG, "load RestaurantList from Tour API")
        _restaurantTabList.value =
            TourNetworkInterfaceUtils.getRestaurantTabList(areaCode)
        Log.d(TAG, "loaded RestaurantList from Tour API, size: ${_restaurantTabList.value!!.size}")

        Log.d(TAG, "save RestaurantList to SharedPreferences")
        SharedPreferencesUtil.saveTourItemList(
            MyApplication.appContext!!,
            _restaurantTabList.value!!,
            PrefConstants.RESTAURANT_TAB_LIST_KEY
        )
    }
    private suspend fun loadCafeTabList(areaCode:String) = withContext(Dispatchers.Main) {
        val loadedCafeList = SharedPreferencesUtil.loadTourItemList(
            MyApplication.appContext!!,
            PrefConstants.CAFE_TAB_LIST_KEY
        )
        if(loadedCafeList != emptyList<TourItem>()){
            Log.d(TAG, "load CafeList from SharedPreferences")
            _cafeTabList.value = loadedCafeList
            return@withContext
        }
        Log.d(TAG, "load CafeList from Tour API")
        _cafeTabList.value =
            TourNetworkInterfaceUtils.getCafeTabList(areaCode)
        Log.d(TAG, "loaded CafeList from Tour API, size: ${_cafeTabList.value!!.size}")

        Log.d(TAG, "save CafeList to SharedPreferences")
        SharedPreferencesUtil.saveTourItemList(
            MyApplication.appContext!!,
            _cafeTabList.value!!,
            PrefConstants.CAFE_TAB_LIST_KEY
        )
    }
    private suspend fun loadEventTabList(areaCode:String) = withContext(Dispatchers.Main) {
        val loadedEventList = SharedPreferencesUtil.loadTourItemList(
            MyApplication.appContext!!,
            PrefConstants.EVENT_TAB_LIST_KEY
        )
        if(loadedEventList != emptyList<TourItem>()){
            Log.d(TAG, "load EventList from SharedPreferences")
            _eventTabList.value = loadedEventList
            return@withContext
        }
        Log.d(TAG, "load EventList from Tour API")
        _eventTabList.value =
            TourNetworkInterfaceUtils.getEventTabList(areaCode)
        Log.d(TAG, "loaded EventList from Tour API, size: ${_eventTabList.value!!.size}")

        Log.d(TAG, "save EventList to SharedPreferences")
        SharedPreferencesUtil.saveTourItemList(
            MyApplication.appContext!!,
            _eventTabList.value!!,
            PrefConstants.EVENT_TAB_LIST_KEY
        )
    }
}