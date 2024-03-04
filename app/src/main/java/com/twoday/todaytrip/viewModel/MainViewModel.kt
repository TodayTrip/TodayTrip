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
        getDestinationAreaCode(loadDestination())
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
        _tourInfoTabList.value = SharedPreferencesUtil
            .loadTourItemList(MyApplication.appContext!!, PrefConstants.TOUR_INFO_TAB_LIST_KEY)
        _restaurantTabList.value = SharedPreferencesUtil
            .loadTourItemList(MyApplication.appContext!!, PrefConstants.RESTAURANT_TAB_LIST_KEY)
        _cafeTabList.value = SharedPreferencesUtil
            .loadTourItemList(MyApplication.appContext!!, PrefConstants.CAFE_TAB_LIST_KEY)
        _eventTabList.value = SharedPreferencesUtil
            .loadTourItemList(MyApplication.appContext!!, PrefConstants.EVENT_TAB_LIST_KEY)
    }

    private fun loadTheme(): String? =
        SharedPreferencesUtil.loadDestination(
            MyApplication.appContext!!,
            PrefConstants.THEME_KEY)
            ?: null

    private fun loadDestination(): String? =
        SharedPreferencesUtil.loadDestination(
            MyApplication.appContext!!,
            PrefConstants.DESTINATION_KEY
        ) ?: null

    private fun getDestinationAreaCode(destination: String?): String? =
        if (destination == null) null
        else DestinationData.destinationAreaCodes[destination] ?: null

    fun retryTourInfoTabList() {
        if (areaCode.isNullOrBlank()) {
            Log.d(TAG, "loadTourITemList) error! no destination area code!")
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            loadTourInfoTabList(theme, areaCode!!)
        }
    }
    private suspend fun loadTourInfoTabList(theme: String?, areaCode: String) {
        withContext(Dispatchers.Main){
            _tourInfoTabList.value =
                if (theme.isNullOrBlank())
                    TourNetworkInterfaceUtils.getTourInfoTabList(areaCode)
                else
                    TourNetworkInterfaceUtils.getTourInfoTabListWithTheme(theme, areaCode)
        }
        SharedPreferencesUtil.saveTourItemList(
            MyApplication.appContext!!,
            _tourInfoTabList.value!!,
            PrefConstants.TOUR_INFO_TAB_LIST_KEY
        )
    }
}