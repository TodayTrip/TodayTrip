package com.twoday.todaytrip.pref_utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.twoday.todaytrip.MyApplication

object SelectRegionPrefUtil {
    private val TAG = "SelectRegionPrefUtil"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    fun loadSelectRegionList() = loadSelectRegionList(PrefConstants.SELECT_REGION_LIST_KEY)
    fun saveSelectRegionList(selectRegionList: MutableList<String>) = saveSelectRegionList(
        selectRegionList,
        PrefConstants.SELECT_REGION_LIST_KEY
    )

    fun resetSelectRegionListPref() {
        Log.d(TAG, "resetSelectRegionListPref) called")
        saveSelectRegionList(mutableListOf<String>())
    }

    private fun getSelectRegionListPreferences(): SharedPreferences =
        MyApplication.appContext!!.getSharedPreferences(
            PrefConstants.PREFERENCE_SELECT_REGION_LIST_KEY,
            Context.MODE_PRIVATE
        )

    private fun saveSelectRegionList(selectRegionList: List<String>, destinationKey: String) {
        val prefs = getSelectRegionListPreferences()
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        val json = moshi.adapter<List<String>>(type).toJson(selectRegionList)
        prefs.edit().putString(destinationKey, json).apply()
    }

    private fun loadSelectRegionList(destinationKey: String): MutableList<String> {
        val prefs = getSelectRegionListPreferences()
        val json = prefs.getString(destinationKey, null)
        return if ((json != null) && (json.toString() != "[]")) {
            val type = Types.newParameterizedType(List::class.java, String::class.java)
            moshi.adapter<List<String>>(type).fromJson(json)?.toMutableList() ?: mutableListOf()
        } else {
            mutableListOf()
        }
    }
}