package com.twoday.todaytrip.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.tourApi.TourNetworkInterfaceUtils
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.utils.DestinationData
import com.twoday.todaytrip.utils.PrefConstants
import com.twoday.todaytrip.utils.SharedPreferencesUtil
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.twoday.todaytrip.utils.TourItemSharedPreferenceUtil
import com.twoday.todaytrip.utils.TourItemSharedPreferenceUtil.saveCafeList
import com.twoday.todaytrip.utils.TourItemSharedPreferenceUtil.saveEventList
import com.twoday.todaytrip.utils.TourItemSharedPreferenceUtil.saveRestaurantList
import com.twoday.todaytrip.utils.TourItemSharedPreferenceUtil.saveTouristAttractionList
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

    private val storageRef = FirebaseStorage.getInstance().reference
    private val databaseRef = FirebaseDatabase.getInstance().reference


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

    fun fetchAndSaveRestaurantList() {
        val restaurantList = TourNetworkInterfaceUtils.fetchRestaurantTabList(areaCode)
        saveRestaurantList(restaurantList)
    }
    fun fetchAndSaveCafeList() {
        val cafeList = TourNetworkInterfaceUtils.getCafeTabList(areaCode)
        saveCafeList(cafeList)

    }
    fun fetchAndSaveEventList() {
        val eventList = TourNetworkInterfaceUtils.getEventTabList(areaCode)
        saveEventList(eventList)
    }
}

data class AreaBasedListItem(
    val title: String,
    val contentId: String,
    val contentTypeId: String,
    val createdTime: String,
    val modifiedTime: String,
    val tel: String = "",
    val address: String = "",
    val addressDetail: String = "",
    val zipcode: String = "",
    val mapX: String = "",
    val mapY: String = "",
    val mapLevel: String = "",
    val areaCode: String = "",
    val siGunGuCode: String = "",
    val category1: String = "",
    val category2: String = "",
    val category3: String = "",
    val firstImage: String? = null, // 이미지 URL을 nullable로 변경
    val firstImageThumbnail: String? = null,
    val bookTour: String = "",
    val copyrightType: String = ""
)