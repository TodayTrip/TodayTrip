package com.twoday.todaytrip.utils

fun Pair<String, String>.convertExtraInfo(): String {
    return this.first + "  " + this.second
}