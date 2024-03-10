package com.twoday.todaytrip.ui.random

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentStartBinding
import com.twoday.todaytrip.utils.ContentIdPrefUtil
import com.twoday.todaytrip.utils.TourItemPrefUtil
import com.twoday.todaytrip.utils.showSnackBar
import com.twoday.todaytrip.viewModel.LoginResult
import com.twoday.todaytrip.viewModel.RandomViewModel

class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy {
        ViewModelProvider(this@StartFragment)[RandomViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
//        setGoogleLogin()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initModelObserver()
        setUpClickListener()
    }

    private fun initModelObserver() {
        viewModel.loginStatus.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                LoginResult.SUCCESS -> findNavController().navigate(R.id.action_navigation_start_to_navigation_random_option)
                LoginResult.FAILURE -> showSnackBar(R.string.login_fail)
            }
        })
    }

    private fun initView() {
        Glide.with(this)
            .load(R.drawable.gif_start_walk)
            .into(binding.ivStartWalkGif)
    }

    private fun setUpClickListener() {
        binding.btnStartTrip.setOnClickListener {
            viewModel.performAnonymousLogin()
            resetSharedPref()
        }
    }

    //TODO 이 함수도 RandomViewModel로 빼놓으면 어떨까요?
    private fun resetSharedPref() {
        TourItemPrefUtil.resetTourItemListPref()
        ContentIdPrefUtil.resetContentIdListPref()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// 구글 로그인 함수(필요할까?)
//    fun setGoogleLogin(){
//        // 요청 정보 옵션
//        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail().build()
//        client = GoogleSignIn.getClient(requireContext(), options)
//
//        binding.loginButton.setOnClickListener {
//            // 로그인 요청
//            startActivityForResult(client.signInIntent, 1)
//        }
//    }