package com.twoday.todaytrip.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.utils.SelectRegionPrefUtil

class SelectRegionViewModel: ViewModel() {

    private var _selectedRegionList = MutableLiveData<MutableSet<String>>()
    val selectedRegionList: LiveData<MutableSet<String>> = _selectedRegionList

    init {
        loadSelectedRegionList()
    }

    private fun loadSelectedRegionList() {
        _selectedRegionList.value = SelectRegionPrefUtil.loadSelectRegionList().toMutableSet()
    }

    fun updateSelectedRegion(region: String, isSelected: Boolean) {
        val currentList = _selectedRegionList.value ?: mutableSetOf()
        if (isSelected) {
            currentList.add(region)
        } else {
            currentList.remove(region)
        }
        _selectedRegionList.value = currentList
    }

    fun resetSharedPrefRegionList() {
        SelectRegionPrefUtil.resetSelectRegionListPref()
    }
}
//선택된 지역 목록 라이브데이터