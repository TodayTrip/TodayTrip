package com.twoday.todaytrip.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.twoday.todaytrip.R

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

fun ImageView.glideWithPlaceholder(
    @DrawableRes
    resId: Int
){
    Glide.with(this.context)
        .load(resId)
        .placeholder(R.drawable.img_default)
        .into(this)
}

fun ImageView.glideWithPlaceholder(
    uri: String
){
    Glide.with(this.context)
        .load(uri)
        .placeholder(R.drawable.img_default)
        .into(this)
}