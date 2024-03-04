package com.twoday.todaytrip.ui.place_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentFirstRecyclerViewBinding
import com.twoday.todaytrip.databinding.FragmentFourthRecyclerViewBinding
import com.twoday.todaytrip.place_list_adapter.FourthRecyclerViewAdapter
import com.twoday.todaytrip.place_list_adapter.SecondRecyclerViewAdapter
import com.twoday.todaytrip.place_list_adapter.ThirdRecyclerViewAdapter
import com.twoday.todaytrip.viewModel.MainViewModel

class FourthRecyclerViewFragment : Fragment(){
    private var _binding: FragmentFourthRecyclerViewBinding? = null
    private val binding get() = _binding!!

    private val mainModel: MainViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                MainViewModel() as T
        }
    }

    private lateinit var adapter: FourthRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFourthRecyclerViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initModelObserver()
    }

    private fun initRecyclerView(){
        adapter = FourthRecyclerViewAdapter(listOf())
        binding.rvFourthRecyclerView.adapter = adapter
    }
    private fun initModelObserver(){
        mainModel.eventTabList.observe(viewLifecycleOwner, Observer {
            hideLoadingUI()
            adapter.changeTourItemList(it)
        })
    }
    private fun hideLoadingUI(){
        binding.layoutFourthRecyclerViewLoading.isVisible = false
        binding.rvFourthRecyclerView.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}