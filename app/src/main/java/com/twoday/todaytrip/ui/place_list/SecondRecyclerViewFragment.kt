package com.twoday.todaytrip.ui.place_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentFirstRecyclerViewBinding
import com.twoday.todaytrip.databinding.FragmentSecondRecyclerViewBinding
import com.twoday.todaytrip.place_list_adapter.FirstRecyclerViewAdapter
import com.twoday.todaytrip.place_list_adapter.SecondRecyclerViewAdapter
import com.twoday.todaytrip.viewModel.MainViewModel

class SecondRecyclerViewFragment : Fragment() {
    private var _binding: FragmentSecondRecyclerViewBinding? = null
    private val binding get() = _binding!!

    private val mainModel: MainViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                MainViewModel() as T
        }
    }

    private lateinit var adapter:SecondRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSecondRecyclerViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initModelObserver()
    }

    private fun initRecyclerView(){
        adapter = SecondRecyclerViewAdapter(listOf())
        binding.rvSecondRecyclerView.adapter = adapter
    }
    private fun initModelObserver(){
        mainModel.restaurantTabList.observe(viewLifecycleOwner, Observer {
            hideLoadingUI()
            adapter.changeTourItemList(it)
        })
    }
    private fun hideLoadingUI(){
        binding.layoutSecondRecyclerViewLoading.isVisible = false
        binding.rvSecondRecyclerView.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}