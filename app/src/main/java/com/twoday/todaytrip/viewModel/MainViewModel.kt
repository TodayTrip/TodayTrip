package com.twoday.todaytrip.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.utils.PrefConstants
import com.twoday.todaytrip.utils.SharedPreferencesUtil

class MainViewModel : ViewModel() {
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
}