package com.twoday.todaytrip.pref_utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.ui.record.Record
import com.twoday.todaytrip.ui.record.RecordJsonConverter

object RecordPrefUtil {
    private val TAG = "RecordPrefUtil"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    fun loadRecordList() = loadRecordList(PrefConstants.RECORD_LIST_KEY)
    fun addRecord(record: Record) = saveRecordList(
        loadRecordList().toMutableList().apply {
            add(record)
        },
        PrefConstants.RECORD_LIST_KEY
    )

    fun deleteRecord(record: Record) {
        Log.d(TAG, "deleteRecord) recordId: ${record.recordId}")
        saveRecordList(
            loadRecordList().filter { it.recordId != record.recordId },
            PrefConstants.RECORD_LIST_KEY
        )
    }

    private fun getRecordListPreferences(): SharedPreferences =
        MyApplication.appContext!!.getSharedPreferences(
            PrefConstants.PREFERENCE_RECORD_LIST_KEY,
            Context.MODE_PRIVATE
        )

    private fun saveRecordList(recordList: List<Record>, destinationKey: String) {
        Log.d(TAG, "saveTourItemList) recordList size = ${recordList.size}")
        val prefs = getRecordListPreferences()

        val serializedRecordList: MutableList<String> = mutableListOf()
        recordList.forEach { record ->
            RecordJsonConverter.toJson(record)?.let{
                serializedRecordList.add(it)
            }
        }
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        val json = moshi.adapter<List<String>>(type).toJson(serializedRecordList)
        prefs.edit().putString(destinationKey, json).apply()
    }

    private fun loadRecordList(destinationKey: String): List<Record> {
        Log.d(TAG, "loadRecordList) destination key: ${destinationKey}")

        val prefs = getRecordListPreferences()
        val json = prefs.getString(destinationKey, null)
        if ((json == null) || (json.toString() == "[]"))
            return emptyList()

        val type = Types.newParameterizedType(List::class.java, String::class.java)
        val serializedRecordList = moshi.adapter<List<String>>(type).fromJson(json)

        val recordList = mutableListOf<Record>()
        serializedRecordList?.forEach { serializedRecord ->
            RecordJsonConverter.fromJson(serializedRecord)?.let {
                recordList.add(it)
            }
        }
        return recordList
    }
}
