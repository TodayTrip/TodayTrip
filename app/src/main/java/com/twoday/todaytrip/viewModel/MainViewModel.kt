package com.twoday.todaytrip.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.storage.FirebaseStorage
import com.twoday.todaytrip.tourApi.TourNetworkInterfaceUtils
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.utils.DestinationData
import com.twoday.todaytrip.utils.DestinationPrefUtil
import com.twoday.todaytrip.utils.TourItemPrefUtil

class MainViewModel : ViewModel() {
    private val TAG = "MainViewModel"

    private val theme by lazy{
        DestinationPrefUtil.loadTheme()
    }
    private val areaCode by lazy{
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

//    private val storageRef = FirebaseStorage.getInstance().reference
//    private val databaseRef = FirebaseDatabase.getInstance().reference

    init {
        loadTourItemList()
    }

    private fun getDestinationAreaCode(destination: String): String =
        if (destination.isNullOrBlank()) ""
        else DestinationData.destinationAreaCodes[destination] ?: ""

    private fun loadTourItemList(){
        _touristAttractionList.value = TourItemPrefUtil.loadTouristAttractionList()
        _restaurantList.value = TourItemPrefUtil.loadRestaurantList()
        _cafeList.value = TourItemPrefUtil.loadCafeList()
        _eventList.value = TourItemPrefUtil.loadEventList()
    }

    fun fetchAndSaveTouristAttractionList() {
        val touristAttractionList =
            if (theme.isNullOrBlank())
                TourNetworkInterfaceUtils.fetchTouristAttractionList(areaCode)
            else
                TourNetworkInterfaceUtils.fetchTouristAttractionListWithTheme(theme, areaCode)
        TourItemPrefUtil.saveTouristAttractionList(touristAttractionList)
    }

    fun fetchAndSaveRestaurantList() {
        val restaurantList = TourNetworkInterfaceUtils.fetchRestaurantTabList(areaCode)
        TourItemPrefUtil.saveRestaurantList(restaurantList)
    }
    fun fetchAndSaveCafeList() {
        val cafeList = TourNetworkInterfaceUtils.getCafeTabList(areaCode)
        TourItemPrefUtil.saveCafeList(cafeList)

    }
    fun fetchAndSaveEventList() {
        val eventList = TourNetworkInterfaceUtils.getEventTabList(areaCode)
        TourItemPrefUtil.saveEventList(eventList)
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