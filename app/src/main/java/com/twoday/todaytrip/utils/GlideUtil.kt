package com.twoday.todaytrip.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide

fun ImageView.glide(
    url: String
){
    Glide.with(this.context)
        .load(url)
        .into(this)
}

fun ImageView.glide(
    @DrawableRes
    resId: Int
){
    Glide.with(this.context)
        .load(resId)
        .into(this)
}