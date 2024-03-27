package com.twoday.todaytrip.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.utils.DestinationPrefUtil

class RandomThemeViewModel : ViewModel() {

    private val _selectedTheme = MutableLiveData<String>()
    val selectedTheme: LiveData<String>
        get() = _selectedTheme

    private val _selectedButtonIndex = MutableLiveData<Int>()
    val selectedButtonIndex: LiveData<Int>
        get() = _selectedButtonIndex

    fun selectTheme(theme: String, selectedIndex: Int) {
        _selectedTheme.value = theme
        _selectedButtonIndex.value = selectedIndex
        selectRandomDestination(theme)
    }

    private fun selectRandomDestination(theme: String) {
        DestinationPrefUtil.saveTheme(theme)
    }
}