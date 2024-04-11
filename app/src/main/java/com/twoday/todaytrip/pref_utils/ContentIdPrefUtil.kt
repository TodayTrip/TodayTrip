package com.twoday.todaytrip.pref_utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.twoday.todaytrip.MyApplication
import java.util.Collections

object ContentIdPrefUtil {
    private val TAG = "ContentIdPrefUtil"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    fun loadContentIdList() = loadContentIdList(PrefConstants.ADDED_CONTENT_ID_LIST_KEY)
    private fun saveContentIdList(contentIdList: List<String>) = saveContentIDList(
        contentIdList,
        PrefConstants.ADDED_CONTENT_ID_LIST_KEY
    )

    fun isSavedContentId(contentId: String): Boolean = loadContentIdList().contains(contentId)

    fun addContentId(contentId: String) = saveContentIdList(
        loadContentIdList().toMutableList().apply {
            remove(contentId)
            add(contentId)
        }
    )

    fun removeContentId(contentId: String) = saveContentIdList(
        loadContentIdList().filter { it != contentId }
    )

    fun swapContentId(selectedPosition: Int, targetPosition: Int) {
        val contentIdList = loadContentIdList().toMutableList()
        Collections.swap(
            contentIdList,
            selectedPosition,
            targetPosition
        )
        saveContentIdList(contentIdList)
    }

    fun resetContentIdListPref() {
        Log.d(TAG, "resetContentIdListPref) called")
        getContentIdListPreferences().edit().clear().commit()
    }

    private fun getContentIdListPreferences(): SharedPreferences =
        MyApplication.appContext!!.getSharedPreferences(
            PrefConstants.PREFERENCE_CONTENT_ID_LIST_KEY,
            Context.MODE_PRIVATE
        )

    private fun saveContentIDList(contentIdList: List<String>, destinationKey: String) {
        val prefs = getContentIdListPreferences()
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        val json = moshi.adapter<List<String>>(type).toJson(contentIdList)
        prefs.edit().putString(destinationKey, json).apply()
    }

    private fun loadContentIdList(destinationKey: String): List<String> {
        val prefs = getContentIdListPreferences()
        val json = prefs.getString(destinationKey, null)
        return if ((json != null) && (json.toString() != "[]")) {
            val type = Types.newParameterizedType(List::class.java, String::class.java)
            moshi.adapter<List<String>>(type).fromJson(json) ?: emptyList()
        } else {
            emptyList()
        }
    }
}