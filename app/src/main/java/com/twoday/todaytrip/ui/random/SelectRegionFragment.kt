package com.twoday.todaytrip.ui.random

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twoday.todaytrip.R

class SelectRegionFragment : Fragment() {

    companion object {
        fun newInstance() = SelectRegionFragment()
    }

    private lateinit var viewModel: SelectRegionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_region, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SelectRegionViewModel::class.java)
        // TODO: Use the ViewModel

    }

}