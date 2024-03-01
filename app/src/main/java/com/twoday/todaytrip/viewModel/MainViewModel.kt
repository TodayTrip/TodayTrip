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
import com.twoday.todaytrip.utils.SharedPreferencesUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        CoroutineScope(Dispatchers.IO).launch {
            loadTourItemList()
        }
    }

    private fun loadTourItemList() {
        destAreaCode = getDestinationAreaCode(getDestination())
        if (destAreaCode.isNullOrBlank()) {
            Log.d(TAG, "loadTourITemList) error! no destination area code!")
            return
        }

        // TODO shared preference에 저장된 정보 있는지 확인하기
        // TODO 완전 랜덤인 지, 테마가 있는지 확인하기

        CoroutineScope(Dispatchers.IO).launch {
            loadTourInfoTabList()
        }
        CoroutineScope(Dispatchers.IO).launch {
            loadRestaurantTabList()
        }
    }

    private fun getDestination(): String? =
        SharedPreferencesUtil.loadDestination(MyApplication.appContext!!, DESTINATION_KEY) ?: null

    private fun getDestinationAreaCode(destination: String?): String? =
        if (destination == null) null
        else DestinationData.destinationAreaCodes[destination] ?: null


    private suspend fun loadTourInfoTabList() = withContext(Dispatchers.Main) {
        // TODO 테마가 있는 경우, 관광지 탭에 테마에 해당되는 정보만 필터링하기
        _tourInfoTabList.value = TourNetworkInterfaceUtils.getTourInfoTabList(destAreaCode!!)
        SharedPreferencesUtil.saveTourItemList(
            MyApplication.appContext!!,
            _tourInfoTabList.value!!,
            PrefConstants.TOUR_INFO_TAB_LIST_KEY
        )
    }

    private suspend fun loadRestaurantTabList() = withContext(Dispatchers.Main) {
        _restaurantTabList.value =
            TourNetworkInterfaceUtils.getRestaurantTabList(destAreaCode!!)
        SharedPreferencesUtil.saveTourItemList(
            MyApplication.appContext!!,
            _restaurantTabList.value!!,
            PrefConstants.RESTAURANT_TAB_LIST_KEY
        )
    }
}