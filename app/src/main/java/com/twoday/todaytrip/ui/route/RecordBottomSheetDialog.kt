package com.twoday.todaytrip.ui.route

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.twoday.todaytrip.R
import com.twoday.todaytrip.ui.MainActivity
import com.twoday.todaytrip.ui.record.Record
import com.twoday.todaytrip.ui.save_photo.SavePhotoActivity
import com.twoday.todaytrip.utils.ContentIdPrefUtil
import com.twoday.todaytrip.utils.RecordPrefUtil

class RecordBottomSheetDialog : BottomSheetDialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogFragment)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.item_bottomsheet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //여행 종료 버튼
        view?.findViewById<ConstraintLayout>(R.id.layout_bottom_sheet_button)?.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            val savePhotoDataList = (activity as SavePhotoActivity).savePhotoDataList
            RecordPrefUtil.addRecord(Record(savePhotoDataList = savePhotoDataList))
            ContentIdPrefUtil.resetContentIdListPref()
            dismiss()
            Toast.makeText(requireActivity(), R.string.record_finish, Toast.LENGTH_SHORT).show()
        }
    }
}