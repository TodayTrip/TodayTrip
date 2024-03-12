package com.twoday.todaytrip.ui.record_gallery

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twoday.todaytrip.R

class RecordGalleryFragment : Fragment() {

    companion object {
        fun newInstance() = RecordGalleryFragment()
    }

    private lateinit var viewModel: RecordGalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_record_gallery, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RecordGalleryViewModel::class.java)
        // TODO: Use the ViewModel
    }

}