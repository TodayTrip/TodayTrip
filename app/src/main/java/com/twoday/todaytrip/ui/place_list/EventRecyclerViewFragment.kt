package com.twoday.todaytrip.ui.place_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.twoday.todaytrip.databinding.FragmentPlaceListEventRecyclerViewBinding
import com.twoday.todaytrip.place_list_adapter.EventRecyclerViewAdapter
import com.twoday.todaytrip.viewModel.MainViewModel


class EventRecyclerViewFragment : Fragment(){
    private val TAG = "EventRecyclerViewFragment"

    private var _binding: FragmentPlaceListEventRecyclerViewBinding? = null
    private val binding get() = _binding!!

    private val mainModel: MainViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                MainViewModel() as T
        }
    }

    private lateinit var adapter: EventRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPlaceListEventRecyclerViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initNoResultOnClickListener()
        initModelObserver()
    }

    private fun initRecyclerView(){
        adapter = EventRecyclerViewAdapter()
        binding.rvEventRecyclerView.adapter = adapter
    }

    private fun initNoResultOnClickListener(){
        binding.layoutEventRecyclerViewNoResult.setOnClickListener {
            setLoadingUI(true)
            mainModel.fetchAndSaveEventList()
        }
    }
    private fun initModelObserver(){
        mainModel.eventList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it.toMutableList())
            if(it.isEmpty())
                setNoResultUI()
            else
                setLoadingUI(false)
        })
    }
    private fun setNoResultUI(){
        binding.layoutEventRecyclerViewNoResult.isVisible = true
        binding.layoutEventRecyclerViewLoading.isVisible = false
    }
    private fun setLoadingUI(isLoading:Boolean){
        Log.d(TAG, "setLoadingUI) isLoading: $isLoading")
        binding.layoutEventRecyclerViewNoResult.isVisible = false

        binding.layoutEventRecyclerViewLoading.isVisible = isLoading
        binding.rvEventRecyclerView.isVisible = !isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}