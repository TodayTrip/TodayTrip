package com.twoday.todaytrip.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RandomOptionViewModel : ViewModel() {

    private val _isSelectedAllRandomBtn = MutableLiveData<Boolean>(false)
    val isSelectedAllRandomBtn: LiveData<Boolean> get() = _isSelectedAllRandomBtn

    private var _isSelectedThemeRandomBtn = MutableLiveData<Boolean>(false)
    val isSelectedThemeRandomBtn: LiveData<Boolean> get() = _isSelectedThemeRandomBtn

    private var _isButtonClickable = MutableLiveData<Boolean>(false)
    val isButtonClickable: LiveData<Boolean> get() = _isButtonClickable

    init{
        _isButtonClickable.value = false
    }

    fun selectAllRandomBtn(){
        _isSelectedAllRandomBtn.value = true
        _isSelectedThemeRandomBtn.value = false

        _isButtonClickable.value = true
    }
    fun selectThemeRandomBtn(){
        _isSelectedAllRandomBtn.value = false
        _isSelectedThemeRandomBtn.value = true

        _isButtonClickable.value = true
    }
}