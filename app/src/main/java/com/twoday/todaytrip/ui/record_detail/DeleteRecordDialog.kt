package com.twoday.todaytrip.ui.record_detail

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.databinding.DialogAlertBinding

class DeleteRecordDialog(context: Context) {

    interface OnPositiveClickListener{
        fun onPositiveClick()
    }

    private lateinit var binding: DialogAlertBinding
    private val dialog = Dialog(MyApplication.appContext!!)
    private val context by lazy{
        MyApplication.appContext!!
    }

    var onPositiveClickListener:OnPositiveClickListener? = null

    fun show(){
        binding = DialogAlertBinding.inflate(LayoutInflater.from(context))

        dialog.setContentView(binding.root)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val closeBtn = binding.layoutAlertNegative
        val confirmBtn = binding.layoutAlertPositive

        closeBtn.setOnClickListener {
            dialog.dismiss()
        }
        confirmBtn.setOnClickListener {
            onPositiveClickListener?.onPositiveClick()
            dialog.dismiss()
        }

        dialog.show()
    }
}