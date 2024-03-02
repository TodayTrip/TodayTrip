package com.twoday.todaytrip.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.tourApi.TourNetworkInterfaceUtils
import com.twoday.todaytrip.utils.DestinationData
import com.twoday.todaytrip.utils.PrefConstants
import com.twoday.todaytrip.utils.SharedPreferencesUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RandomResultViewModel : ViewModel() {
    private val TAG = "RandomResultViewModel"

    private val _isTourDataListReady = MutableLiveData<Boolean>()
    val isTourDataListReady: LiveData<Boolean>
        get() = _isTourDataListReady

    init {
        _isTourDataListReady.value = false
        CoroutineScope(Dispatchers.IO).launch {
            loadTourItemList()
        }
    }

    private suspend fun loadTourItemList() {
        val theme = getTheme()
        val areaCode = getDestinationAreaCode(getDestination())
        if (areaCode.isNullOrBlank()) {
            Log.d(TAG, "loadTourITemList) error! no destination area code!")
            return
        }

        val tourInfoJob = CoroutineScope(Dispatchers.IO).launch {
            loadTourInfoTabList(theme, areaCode!!)
        }
        val restaurantJob = CoroutineScope(Dispatchers.IO).launch {
            loadRestaurantTabList(areaCode!!)
        }
        val cafeJob = CoroutineScope(Dispatchers.IO).launch {
            loadCafeTabList(areaCode!!)
        }
        val eventJob = CoroutineScope(Dispatchers.IO).launch {
            loadEventTabList(areaCode!!)
        }

        tourInfoJob.join()
        restaurantJob.join()
        cafeJob.join()
        eventJob.join()
        withContext(Dispatchers.Main){
            _isTourDataListReady.value = true
        }
    }

    private fun getTheme(): String? =
        SharedPreferencesUtil.loadDestination(MyApplication.appContext!!, PrefConstants.THEME_KEY)
            ?: null

    private fun getDestination(): String? =
        SharedPreferencesUtil.loadDestination(
            MyApplication.appContext!!,
            PrefConstants.DESTINATION_KEY
        ) ?: null

    private fun getDestinationAreaCode(destination: String?): String? =
        if (destination == null) null
        else DestinationData.destinationAreaCodes[destination] ?: null

    private fun loadTourInfoTabList(theme: String?, areaCode: String) =
        SharedPreferencesUtil.saveTourItemList(
            MyApplication.appContext!!,
            if (theme.isNullOrBlank())
                TourNetworkInterfaceUtils.getTourInfoTabList(areaCode)
            else
                TourNetworkInterfaceUtils.getTourInfoTabListWithTheme(theme, areaCode),
            PrefConstants.TOUR_INFO_TAB_LIST_KEY
        )
}

private fun loadRestaurantTabList(areaCode: String) {
    SharedPreferencesUtil.saveTourItemList(
        MyApplication.appContext!!,
        TourNetworkInterfaceUtils.getRestaurantTabList(areaCode),
        PrefConstants.RESTAURANT_TAB_LIST_KEY
    )
}

private fun loadCafeTabList(areaCode: String){
    SharedPreferencesUtil.saveTourItemList(
        MyApplication.appContext!!,
        TourNetworkInterfaceUtils.getCafeTabList(areaCode),
        PrefConstants.CAFE_TAB_LIST_KEY
    )
}

private suspend fun loadEventTabList(areaCode: String) {
    SharedPreferencesUtil.saveTourItemList(
        MyApplication.appContext!!,
        TourNetworkInterfaceUtils.getEventTabList(areaCode),
        PrefConstants.EVENT_TAB_LIST_KEY
    )
}