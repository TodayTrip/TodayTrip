package com.twoday.todaytrip.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.twoday.todaytrip.adapter.FirstRecyclerViewAdapter
import com.twoday.todaytrip.databinding.FragmentFirstRecyclerViewBinding
import com.twoday.todaytrip.viewModel.MainViewModel


class FirstRecyclerViewFragment : Fragment() {

    private var _binding: FragmentFirstRecyclerViewBinding? = null
    private val binding = _binding!!

    private val mainModel: MainViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                MainViewModel() as T
        }
    }

    private lateinit var adapter:FirstRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFirstRecyclerViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initModelObserver()
    }

    private fun initRecyclerView(){
        adapter = FirstRecyclerViewAdapter(listOf())
        binding.rvFirstRecyclerView.adapter = adapter
    }
    private fun initModelObserver(){
        mainModel.tourInfoTabList.observe(viewLifecycleOwner, Observer {
            adapter.changeTourItemList(it)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}