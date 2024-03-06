package com.twoday.todaytrip.ui.route

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.twoday.todaytrip.R
import com.twoday.todaytrip.ui.MainActivity
import com.twoday.todaytrip.ui.record.Record
import com.twoday.todaytrip.ui.save_photo.SavePhotoActivity
import com.twoday.todaytrip.utils.ContentIdPrefUtil
import com.twoday.todaytrip.utils.RecordPrefUtil

class BottomSheetDialog : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.item_bottomsheet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //여행종료 버튼
        view?.findViewById<ConstraintLayout>(R.id.layout_bottom_sheet_button)?.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            val savePhotoDataList = (activity as SavePhotoActivity).savePhotoDataList
            RecordPrefUtil.addRecord(Record(savePhotoDataList))
            ContentIdPrefUtil.resetContentIdListPref()
            dismiss()
        }

        //닫기버튼
        view?.findViewById<ImageView>(R.id.iv_bottom_sheet_cancel)?.setOnClickListener {
            dismiss()
        }
    }
}