package com.twoday.todaytrip.ui.place_detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twoday.todaytrip.tourApi.AreaBasedListItem
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.ui.place_list.adapter.OnTourItemAddClickListener
import com.twoday.todaytrip.utils.ContentIdPrefUtil
import com.twoday.todaytrip.viewModel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PlaceDetailViewModel(): ViewModel() {
    private var tourItem: TourItem? = null

    private val _isTourItemAdded = MutableLiveData<Boolean>()
    val isTourItemAdded: LiveData<Boolean> get() = _isTourItemAdded

    fun initTourItem(tourItemIntent: TourItem){
        tourItem = tourItemIntent.apply{
            isAdded = ContentIdPrefUtil.isSavedContentId(this.getContentId())
        }
        _isTourItemAdded.value = tourItem?.isAdded
    }

    fun addButtonClicked(){
        OnTourItemAddClickListener.onTourItemAddClick(tourItem!!)
        _isTourItemAdded.value = tourItem?.isAdded
    }
}