package com.twoday.todaytrip.ui.random

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.twoday.todaytrip.R
import androidx.transition.TransitionInflater
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentRandomResultBinding
import com.twoday.todaytrip.ui.MainActivity
import com.twoday.todaytrip.viewModel.RandomResultViewModel

class RandomResultFragment : Fragment() {
    private val TAG = "RandomResultFragment"

    private var _binding: FragmentRandomResultBinding? = null
    private val binding get() = _binding!!

    private val model: RandomResultViewModel by lazy {
        ViewModelProvider(this@RandomResultFragment)[RandomResultViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRandomResultBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initModelObserver()
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.fade)
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
    }

    private fun initModelObserver(){
        model.isTouristAttractionListReady.observe(viewLifecycleOwner, Observer { isReady ->
            if(isReady) {
                Log.d(TAG, "tourist attraction list ready! start main activity")
                val intent = Intent(activity, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        })
    }

    private fun initView() {
        Glide.with(this)
            .load(R.drawable.gif_map)
            .into(binding.ivRandomMapGif)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}