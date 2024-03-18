package com.twoday.todaytrip.ui.record_gallery

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.twoday.todaytrip.databinding.FragmentRecordGalleryBinding
import com.twoday.todaytrip.viewModel.RecordDetailViewModel

class RecordGalleryFragment : Fragment() {
    private lateinit var binding: FragmentRecordGalleryBinding
    private val viewModel: RecordDetailViewModel by activityViewModels()
    private lateinit var uriList: List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecordGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(binding.rvRecordGallery)
        initModelObserver()
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        val columnWidthDp = 400
        val layoutManager = GridLayoutManager(requireContext(), calColumns(columnWidthDp))
        recyclerView.layoutManager = layoutManager
        Log.d("asd", calColumns(columnWidthDp).toString())
    }

    private fun initModelObserver() {
        viewModel.imageUriList.observe(viewLifecycleOwner, Observer { uriList ->
            uriList?.let {
                this.uriList = it
                val adapter = RecordGalleryAdapter(it)
                binding.rvRecordGallery.adapter = adapter
            }
        })
    }

    private fun getDisplayWidth(): Int {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        Log.d("qqq",displayMetrics.widthPixels.toString())
        return displayMetrics.widthPixels
    }

    private fun calColumns(columnWidthDp: Int): Int {
        val displayWidth = getDisplayWidth()
        val numColumns = displayWidth / columnWidthDp
        return if (numColumns <= 1) 2 else numColumns
    }
}

//    private fun observeViewModel() {
//        val imageUriList = arguments?.getStringArrayList("IMAGE_URI_LIST")
//        viewModel.setImageUriList(imageUriList ?: emptyList())
//
//        viewModel.imageUriListLiveData.observe(viewLifecycleOwner, fun(imageUriList: List<String>) {
//            val adapter = RecordGalleryAdapter(imageUriList)
//            binding.rvRecordGallery.adapter = adapter
//        })
//    }

