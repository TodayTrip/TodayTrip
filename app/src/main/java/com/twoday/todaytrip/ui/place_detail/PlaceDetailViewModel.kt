package com.twoday.todaytrip.ui.place_detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.ui.place_list.adapter.OnTourItemAddClickListener
import com.twoday.todaytrip.utils.ContentIdPrefUtil
import com.twoday.todaytrip.utils.RecordPrefUtil


class PlaceDetailViewModel() : ViewModel() {
    private val TAG = "PlaceDetailViewModel"

    private var tourItem: TourItem? = null

    private val _isTourItemAdded = MutableLiveData<Boolean>()
    val isTourItemAdded: LiveData<Boolean> get() = _isTourItemAdded

    private val _placeInfoList = MutableLiveData<List<Pair<String, String>>>()
    val placeInfoList: LiveData<List<Pair<String, String>>> get() = _placeInfoList

    private val _memoryDataList = MutableLiveData<List<MemoryData>>()
    val memoryDataList: LiveData<List<MemoryData>> = _memoryDataList

    fun initTourItem(tourItemIntent: TourItem) {
        tourItem = tourItemIntent.apply {
            isAdded = ContentIdPrefUtil.isSavedContentId(this.getContentId())
        }
        _isTourItemAdded.value = tourItem?.isAdded

        initPlaceInfoList()
        initMemoryDataList()
    }

    fun addButtonClicked() {
        OnTourItemAddClickListener.onTourItemAddClick(tourItem!!)
        _isTourItemAdded.value = tourItem?.isAdded
    }

    private fun initPlaceInfoList(){
        _placeInfoList.value = tourItem!!.getDetailInfoWithLabel()
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