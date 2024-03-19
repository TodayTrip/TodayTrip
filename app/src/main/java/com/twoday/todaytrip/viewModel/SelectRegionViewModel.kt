package com.twoday.todaytrip.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.utils.SelectRegionPrefUtil

class SelectRegionViewModel: ViewModel() {

    private var _selectedRegionList = MutableLiveData<MutableSet<String>>()
    private val selectedRegionList: LiveData<MutableSet<String>> = _selectedRegionList

    init {

    }

    fun loadSelectedRegionList() {
        _selectedRegionList.value = SelectRegionPrefUtil.loadSelectRegionList().toMutableSet()
    }

    fun updateSelectedRegion() {
        val currentList = _selectedRegionList.value ?: mutableSetOf()
//        if ()
    }

    fun resetSharedPrefRegionList() {
        SelectRegionPrefUtil
    }
}