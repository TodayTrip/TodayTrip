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
}