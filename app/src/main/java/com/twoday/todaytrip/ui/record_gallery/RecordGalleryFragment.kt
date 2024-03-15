package com.twoday.todaytrip.ui.record_gallery

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentRecordBinding
import com.twoday.todaytrip.databinding.FragmentRecordGalleryBinding

class RecordGalleryFragment : Fragment() {
    private lateinit var binding: FragmentRecordGalleryBinding
    private val viewModel: RecordGalleryViewModel by activityViewModels()

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
        observeViewModel()
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
    }

    private fun observeViewModel() {
        val imageUriList = arguments?.getStringArrayList("IMAGE_URI_LIST")
        viewModel.setImageUriList(imageUriList ?: emptyList())

        viewModel.imageUriListLiveData.observe(viewLifecycleOwner, fun(imageUriList: List<String>) {
            val adapter = RecordGalleryAdapter(imageUriList)
            binding.rvRecordGallery.adapter = adapter
        })
    }

}