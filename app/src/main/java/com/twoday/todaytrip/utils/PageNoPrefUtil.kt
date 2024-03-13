package com.twoday.todaytrip.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.twoday.todaytrip.MyApplication

object PageNoPrefUtil {
    private val TAG = "PageNoPrefUtil"

    fun saveTouristAttractionPageNo(pageNo:Int) =
        savePageNo(pageNo, PrefConstants.TOURIST_ATTRACTION_PAGE_NO_KEY)
    fun loadTouristAttractionPageNo() =
        loadPageNo(PrefConstants.TOURIST_ATTRACTION_PAGE_NO_KEY)

    fun saveRestaurantPageNo(pageNo:Int) =
        savePageNo(pageNo, PrefConstants.RESTAURANT_PAGE_NO_KEY)
    fun loadRestaurantPageNo() =
        loadPageNo(PrefConstants.RESTAURANT_PAGE_NO_KEY)

    fun saveCafePageNo(pageNo:Int) =
        savePageNo(pageNo, PrefConstants.CAFE_PAGE_NO_KEY)
    fun loadCafePageNo() =
        loadPageNo(PrefConstants.CAFE_PAGE_NO_KEY)

    fun saveEventPageNo(pageNo:Int) =
        savePageNo(pageNo, PrefConstants.EVENT_PAGE_NO_KEY)
    fun loadEventPageNo() =
        loadPageNo(PrefConstants.EVENT_PAGE_NO_KEY)

    fun resetPageNoPref(){
        Log.d(TAG, "resetPageNoPref called")
        saveTouristAttractionPageNo(0)
        saveRestaurantPageNo(0)
        saveCafePageNo(0)
        saveEventPageNo(0)
    }


    private fun getPageNoPreferences(): SharedPreferences =
        MyApplication.appContext!!.getSharedPreferences(
            PrefConstants.PREFERENCE_PAGE_NO_KEY,
            Context.MODE_PRIVATE
        )
    private fun savePageNo(pageNo: Int, pageNoKey: String) {
        val editor = getPageNoPreferences().edit()
        editor.putInt(pageNoKey, pageNo)
        editor.apply()
    }
    private fun loadPageNo(pageNoKey: String): Int {
        return getPageNoPreferences().getInt(pageNoKey, 0)
    }
}