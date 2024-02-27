package com.twoday.todaytrip.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecordDetailViewModel : ViewModel() {
    private val _isOptionMap: MutableLiveData<Boolean> = MutableLiveData<Boolean>( )
    val isOptionMap: LiveData<Boolean>
        get() = _isOptionMap

    init{
        _isOptionMap.value = true
    }

    fun toggleIsOptionMap(){
        _isOptionMap.value = !(_isOptionMap.value!!)
    }
}