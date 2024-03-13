package com.twoday.todaytrip.utils

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.showSnackBar(message: Int, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(
        requireView(),
        message,
        length
    ).show()
}