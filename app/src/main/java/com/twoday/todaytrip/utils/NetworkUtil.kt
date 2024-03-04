package com.twoday.todaytrip.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import com.twoday.todaytrip.MyApplication


object NetworkUtil {
    fun isNetworkAvailable(): Boolean {
        val connectivity = MyApplication.appContext!!.getSystemService<ConnectivityManager>()
        if (connectivity == null) {
            return false
        } else {
            val info = connectivity.allNetworkInfo
            if (info != null) {
                for (i in info.indices) {
                    if (info[i].state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
                }
            }
        }
        return false
    }
}