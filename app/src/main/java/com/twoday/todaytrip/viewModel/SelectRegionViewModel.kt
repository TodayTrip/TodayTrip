package com.twoday.todaytrip.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.utils.DestinationPrefUtil
import com.twoday.todaytrip.utils.SelectRegionPrefUtil

class SelectRegionViewModel : ViewModel() {

    private var _selectedRegionList = MutableLiveData<MutableSet<String>>()
    val selectedRegionList: LiveData<MutableSet<String>> = _selectedRegionList

    init {
        loadSelectedRegionList()
    }

    private fun loadSelectedRegionList() {
        _selectedRegionList.value = SelectRegionPrefUtil.loadSelectRegionList().toMutableSet()
    }

    fun saveSelectedRegionList() {
        SelectRegionPrefUtil.resetSelectRegionListPref()
        SelectRegionPrefUtil.saveSelectRegionList(_selectedRegionList.value!!.toMutableList())
    }

    fun selectAndSaveDestination() {
        val selectedDestination = _selectedRegionList.value!!.random()
        DestinationPrefUtil.saveDestination(selectedDestination)
    }

    fun clearSelectedRegionList() {
        _selectedRegionList.value = emptySet()
    }

    fun addSelectedRegion(selectedRegion: String) {
        val newSelectedRegionList = mutableSetOf<String>().apply {
            _selectedRegionList.value?.let {
                addAll(it.toSet())
            }
            add(selectedRegion)
        }
        _selectedRegionList.value = newSelectedRegionList
    }

    fun removeSelectedRegion(selectedRegion: String) {
        val newSelectedRegionList = mutableSetOf<String>().apply {
            _selectedRegionList.value?.let {
                addAll(
                    it.filter { region -> region != selectedRegion }.toSet()
                )
            }
        }
        _selectedRegionList.value = newSelectedRegionList
    }
}