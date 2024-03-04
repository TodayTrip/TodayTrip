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
import com.twoday.todaytrip.databinding.FragmentPlaceListRestaurantRecyclerViewBinding
import com.twoday.todaytrip.place_list_adapter.RestaurantRecyclerViewAdapter
import com.twoday.todaytrip.viewModel.MainViewModel

class RestaurantRecyclerViewFragment : Fragment() {
    private var _binding: FragmentPlaceListRestaurantRecyclerViewBinding? = null
    private val binding get() = _binding!!

    private val mainModel: MainViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                MainViewModel() as T
        }
    }

    private lateinit var adapter:RestaurantRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPlaceListRestaurantRecyclerViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initModelObserver()
    }

    private fun initRecyclerView(){
        adapter = RestaurantRecyclerViewAdapter(listOf())
        binding.rvRestaurantRecyclerView.adapter = adapter
    }
    private fun initModelObserver(){
        mainModel.restaurantTabList.observe(viewLifecycleOwner, Observer {
            hideLoadingUI()
            adapter.changeTourItemList(it)
        })
    }
    private fun hideLoadingUI(){
        binding.layoutRestaurantRecyclerViewLoading.isVisible = false
        binding.rvRestaurantRecyclerView.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}