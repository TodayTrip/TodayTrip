package com.twoday.todaytrip.ui.record_detail

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.DialogAlertBinding

class DeleteRecordDialog(val context: Context) {
    private val TAG = "DeleteRecordDialog"

    interface OnPositiveClickListener{
        fun onPositiveClick()
    }

    private lateinit var binding: DialogAlertBinding
    private val dialog = Dialog(context)

    var onPositiveClickListener:OnPositiveClickListener? = null

    fun show(){
        binding = DialogAlertBinding.inflate(LayoutInflater.from(context))

        dialog.setContentView(binding.root)
        dialog.setCancelable(false)

        initDialogUI()
        initListener()

        dialog.show()
    }

    private fun initDialogUI(){
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.tvAlertDialogTitle.text =
            context.resources.getText(R.string.alert_dialog_delete_title)
        binding.tvAlertDialogDescription.text =
            context.resources.getText(R.string.alert_dialog_delete_description)
    }
    private fun initListener(){
        binding.layoutAlertNegative.setOnClickListener {
            Log.d(TAG, "closeBtn clicked")
            dialog.dismiss()
        }
        binding.layoutAlertPositive.setOnClickListener {
            Log.d(TAG, "deleteBtn clicked")
            onPositiveClickListener?.onPositiveClick()
            dialog.dismiss()
        }
    }
}