package com.twoday.todaytrip.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.tourApi.TourNetworkInterfaceUtils
import com.twoday.todaytrip.tourData.TourCategoryId3
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.ui.place_detail.MemoryData
import com.twoday.todaytrip.ui.place_list.adapter.OnTourItemAddClickListener
import com.twoday.todaytrip.utils.ContentIdPrefUtil
import com.twoday.todaytrip.utils.RecordPrefUtil
import com.twoday.todaytrip.utils.TourItemPrefUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PlaceDetailViewModel() : ViewModel() {
    private val TAG = "PlaceDetailViewModel"

    private var tourItem: TourItem? = null

    private val _isTourItemAdded = MutableLiveData<Boolean>()
    val isTourItemAdded: LiveData<Boolean> get() = _isTourItemAdded

    private val _placeInfoList = MutableLiveData<List<Pair<String, String>>>()
    val placeInfoList: LiveData<List<Pair<String, String>>> get() = _placeInfoList

    private val _nearByList = MutableLiveData<List<TourItem>>()
    val nearByList: LiveData<List<TourItem>> get() = _nearByList

    private val _memoryDataList = MutableLiveData<List<MemoryData>>()
    val memoryDataList: LiveData<List<MemoryData>> = _memoryDataList

    fun initTourItem(tourItemIntent: TourItem) {
        tourItem = tourItemIntent.apply {
            isAdded = ContentIdPrefUtil.isSavedContentId(this.getContentId())
        }
        _isTourItemAdded.value = tourItem?.isAdded

        initPlaceInfoList()
        initNearbyList()
        initMemoryDataList()
    }

    fun addButtonClicked() {
        OnTourItemAddClickListener.onTourItemAddClick(tourItem!!)
        _isTourItemAdded.value = tourItem?.isAdded
    }

    private fun initPlaceInfoList(){
        _placeInfoList.value = tourItem?.getDetailInfoWithLabel()
    }

    private fun initNearbyList() = CoroutineScope(Dispatchers.IO).launch {
        tourItem?.let {
            val fetchedNearByList = async{
                TourNetworkInterfaceUtils.fetchNearByList(tourItem!!)
            }.await()

            withContext(Dispatchers.Main){
                Log.d(TAG, "initNearbyList) fetchedNearByList: ${fetchedNearByList.toString()}")
                _nearByList.value = fetchedNearByList
                saveNearByList()
            }
        }
    }
    private fun saveNearByList(){
        _nearByList.value?.forEach { tourItem ->
            when(tourItem){
                is TourItem.Restaurant ->{
                    if((!tourItem.tourItemInfo.category3.isNullOrEmpty()) &&
                        (tourItem.tourItemInfo.category3 == TourCategoryId3.CAFE_AND_TEA.id))
                        TourItemPrefUtil.saveMoreCafeList(listOf(tourItem))
                    else
                        TourItemPrefUtil.saveMoreRestaurantList(listOf(tourItem))
                }
                is TourItem.EventPerformanceFestival -> {
                    TourItemPrefUtil.saveMoreEventList(listOf(tourItem))
                }
                else ->{ // TouristDestination, CulturalFacilities, LeisureSports
                    TourItemPrefUtil.saveMoreTouristAttractionList(listOf(tourItem))
                }
            }
        }
    }
    private fun initMemoryDataList() {
        val loadedMemoryDataList = mutableListOf<MemoryData>()
        RecordPrefUtil.loadRecordList().forEach { record ->
            record.savePhotoDataList.forEach { savePhotoData ->
                if (savePhotoData.tourItem.getContentId() == tourItem!!.getContentId()) {
                    loadedMemoryDataList.addAll(
                        savePhotoData.imageUriList.map { imageUri ->
                            MemoryData(record.travelDate, imageUri)
                        }
                    )
                }
            }
        }

        Log.d(TAG, "initMemoryDataList) loadedMemoryDataList.size: ${loadedMemoryDataList.size}")
        _memoryDataList.value = loadedMemoryDataList
    }
}