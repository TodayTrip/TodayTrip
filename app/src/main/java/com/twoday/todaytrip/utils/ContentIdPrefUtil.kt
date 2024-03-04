package com.twoday.todaytrip.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.twoday.todaytrip.MyApplication

object ContentIdPrefUtil {
    private val TAG = "ContentIdPrefUtil"

    fun loadContentIdList() = loadContentIdList(PrefConstants.ADDED_CONTENT_ID_LIST_KEY)
    fun saveContentIdList(contentIdList: List<String>) = saveContentIDList(
        contentIdList,
        PrefConstants.ADDED_CONTENT_ID_LIST_KEY
    )

    fun isSavedContentId(contentId: String): Boolean = loadContentIdList().contains(contentId)
    fun addContentId(contentId:String) = saveContentIdList(
        loadContentIdList().toMutableList().apply{
            add(contentId)
        }
    )
    fun removeContentId(contentId: String) = saveContentIdList(
        loadContentIdList().filter { it != contentId }
    )

    private fun getContentIdListPreferences(): SharedPreferences =
        MyApplication.appContext!!.getSharedPreferences(
            PrefConstants.PREFERENCE_CONTENT_ID_LIST_KEY,
            Context.MODE_PRIVATE
        )
    private fun saveContentIDList(contentIdList: List<String>, destinationKey: String) {
        val prefs = getContentIdListPreferences()
        val json = Gson().toJson(contentIdList)
        prefs.edit().putString(destinationKey, json).apply()
    }
    private fun loadContentIdList(destinationKey: String): List<String> {
        val prefs = getContentIdListPreferences()
        val json = prefs.getString(destinationKey, null)
        return if ((json != null) && (json.toString() != "[]")) {
            val type = object : TypeToken<List<String>>() {}.type
            Gson().fromJson(json, type)
        } else {
            emptyList()
        }
    }
}