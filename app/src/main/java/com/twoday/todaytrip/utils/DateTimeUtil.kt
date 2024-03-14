package com.twoday.todaytrip.utils

import java.text.SimpleDateFormat
import java.util.Calendar

object DateTimeUtil {
    fun getCurrentDate():String{
        val currentDateTime = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.format(currentDateTime)
    }
    fun getCurrentDateWithNoLine():String{
        val currentDateTime = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyyMMdd")
        return formatter.format(currentDateTime)
    }
    fun getCurrentTime():String{
        val currentTime = Calendar.getInstance().time
        val formatter = SimpleDateFormat("HHmm")
        return formatter.format(currentTime)
    }
    fun getCurrentDay():String{
        val currentDay = Calendar.getInstance().time
        val formatter = SimpleDateFormat("EEE")
        return formatter.format(currentDay)
    }
}