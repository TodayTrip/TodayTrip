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
import com.twoday.todaytrip.databinding.FragmentPlaceListCafeRecyclerViewBinding
import com.twoday.todaytrip.place_list_adapter.CafeRecyclerViewAdapter
import com.twoday.todaytrip.viewModel.MainViewModel

class CafeRecyclerViewFragment : Fragment() {
    private var _binding: FragmentPlaceListCafeRecyclerViewBinding? = null
    private val binding get() = _binding!!

    private val mainModel: MainViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                MainViewModel() as T
        }
    }

    private lateinit var adapter: CafeRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPlaceListCafeRecyclerViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initModelObserver()
    }

    private fun initRecyclerView(){
        adapter = CafeRecyclerViewAdapter(listOf())
        binding.rvCafeRecyclerView.adapter = adapter
    }
    private fun initModelObserver(){
        mainModel.cafeTabList.observe(viewLifecycleOwner, Observer {
            hideLoadingUI()
            adapter.changeTourItemList(it)
        })
    }
    private fun hideLoadingUI(){
        binding.layoutCafeRecyclerViewLoading.isVisible = false
        binding.rvCafeRecyclerView.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}