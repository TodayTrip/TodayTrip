package com.twoday.todaytrip.ui.random

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentStartBinding
import com.twoday.todaytrip.utils.SharedPreferencesUtil

//TODO 여행지 초기화 작업 필요할 듯
class StartFragment : Fragment() {
    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!
    // FirebaseAuth 인스턴스를 변수로 선언
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // FirebaseAuth 객체의 공유 인스턴스를 초기화
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        setUpClickListener()
        initView()
        return binding.root
//        setGoogleLogin()
    }

    private fun initView() {
        Glide.with(this)
            .load(R.drawable.start_walk_gif)
            .into(binding.ivStartWalkGif)
    }

    private fun setUpClickListener() {
        binding.btnStartTrip.setOnClickListener {
//            findNavController().navigate(R.id.action_navigation_start_to_navigation_random_option)
            performAnonymousLogin()

            SharedPreferencesUtil.eraseTourItemList(MyApplication.appContext!!)
        }
    }

    //익명 로그인 함수
    private fun performAnonymousLogin() {
        auth.signInAnonymously()
            .addOnCompleteListener(activity as Activity) { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(R.id.action_navigation_start_to_navigation_random_option)
                } else {
                    showSnackBar(R.string.login_fail)
                }
            }
    }

    private fun showSnackBar(message: Int) {
        Snackbar.make(
            binding.root,
            getString(message),
            Snackbar.LENGTH_SHORT
        ).show()
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