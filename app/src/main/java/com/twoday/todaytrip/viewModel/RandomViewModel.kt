package com.twoday.todaytrip.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.twoday.todaytrip.utils.DestinationData
import com.twoday.todaytrip.utils.DestinationPrefUtil

class RandomViewModel: ViewModel() {

    private val _loginStatus = MutableLiveData<LoginResult>()
    val loginStatus: LiveData<LoginResult> = _loginStatus

    // FirebaseAuth 인스턴스를 변수로 선언
    private var auth: FirebaseAuth = Firebase.auth

    //익명 로그인 함수
    fun performAnonymousLogin() {
        auth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _loginStatus.value = LoginResult.SUCCESS
            } else {
                _loginStatus.value = LoginResult.FAILURE
            }
        }
    }

    // 전체 랜덤 시 여행지 랜덤 선택하는 함수, 테마 Sharf에는 null로 저장
    fun selectRandomDestination() {
        val randomDestination = DestinationData.allRandomDestination.random()
        DestinationPrefUtil.saveDestination(randomDestination)
        DestinationPrefUtil.saveTheme("")
    }
}

enum class LoginResult {
    SUCCESS,
    FAILURE
}