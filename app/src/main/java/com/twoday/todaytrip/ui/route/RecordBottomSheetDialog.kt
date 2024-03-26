package com.twoday.todaytrip.ui.route

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.twoday.todaytrip.R
import com.twoday.todaytrip.ui.MainActivity
import com.twoday.todaytrip.ui.record.Record
import com.twoday.todaytrip.utils.ContentIdPrefUtil
import com.twoday.todaytrip.utils.RecordPrefUtil
import com.twoday.todaytrip.viewModel.SavePhotoViewModel

class RecordBottomSheetDialog : BottomSheetDialogFragment() {

    private val savePhotoViewModel: SavePhotoViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                SavePhotoViewModel() as T
        }
    }
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
//            val savePhotoDataList = (activity as SavePhotoActivity).savePhotoDataList
            val savePhotoDataList = savePhotoViewModel.savePhotoDataList
//            RecordPrefUtil.addRecord(Record(savePhotoDataList = savePhotoDataList))
            RecordPrefUtil.addRecord(Record(savePhotoDataList = savePhotoDataList.value?.toList() ?: listOf()))
//            Log.d("sdc","바텀시트 ${savePhotoDataList[0].imageUriList.toString()}")
            Log.d("sdc","바텀시트 ${savePhotoDataList.value.toString()}")
            Log.d("sdc","바텀시트 ${savePhotoDataList.value?.get(0)?.imageUriList.toString()}")
            ContentIdPrefUtil.resetContentIdListPref()
            dismiss()
            Toast.makeText(requireActivity(), R.string.record_finish, Toast.LENGTH_SHORT).show()
        }
    }
}