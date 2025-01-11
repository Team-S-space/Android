package com.example.umc_7th_hackathon.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_7th_hackathon.databinding.ActivityLoginBinding
import com.example.umc_7th_hackathon.login.api.clientData.LoginClient
import com.example.umc_7th_hackathon.login.api.UserRetrofitItf
import com.example.umc_7th_hackathon.RetrofitObj
import com.example.umc_7th_hackathon.login.api.reponseData.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private var username: String = ""
    private var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 로그인 버튼 클릭 시
        binding.loginBt.setOnClickListener {

            username = binding.loginIdEt.text.toString()
            password = binding.loginPwEt.text.toString()

            CheckLogin()
        }

        // 아이디 입력 칸 클릭 시 오류 메시지 없애기
        binding.loginIdEt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.loginErTv.visibility = View.GONE
            }
        }

        // 비밀번호 입력 칸 클릭 시 오류 메시지 없애기
        binding.loginPwEt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.loginErTv.visibility = View.GONE
            }
        }

        // 배경화면 클릭 시 키보드 숨기기
        binding.loginActivity.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN){
                currentFocus?.let { view ->
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                    view.clearFocus()
                }
            }
            true
        }
    }

    private fun CheckLogin(){

        // 입력하지 않은 경우 (빈칸)
        if (binding.loginIdEt.text.toString().isEmpty() || binding.loginPwEt.text.toString().isEmpty()){
            Toast.makeText(this, "아이디와 비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // 로그인 API 연동
        val authService = RetrofitObj.getRetrofit().create(UserRetrofitItf::class.java)
        authService.login(LoginClient(username, password)).enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("LOGIN/SUCCESS", response.toString())

                when (response.code()){
                    200 -> {
                        val resp: LoginResponse = response.body()!!
                        if (resp != null){
                            if (resp.isSuccess){
//                                Log.d("아이디 값", resp.result.id.toString())
                                moveMainActivity(resp) // 로그인 진행
                            } else {
                                Log.e("LOGIN/FAILURE",
                                    "응답 코드: ${resp.code}, 응답메시지: ${resp.message}")

                                // code가 MEMBER400일 때 오류 메시지 출력
                                if (resp.code == "MEMBER401" || response.code() == 500 || response.code() == 400) {
                                    binding.loginErTv.visibility = View.VISIBLE
                                }
                            }

                        } else {
                            Log.e("실패", response.message())
                            Log.e("LOGIN/FAILURE", "응답 코드: ${resp.code}, 응답메시지: ${resp.message}")

                        }
                    }
                    else -> {
                        binding.loginErTv.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("RETROFIT/FAILURE", t.message.toString())
            }

        })


    }

//    // 토큰을 SharedPreferences에 저장
//    private fun saveToken(token: String){
//        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//        with(sharedPref.edit()){
//            putString("token", token)
//            apply()
//        }
//    }
//
//    private fun saveId(id: Int){
//        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//        with(sharedPref.edit()){
//            putInt("UserId", id) // 아이디 값 전달
//            apply()
//        }
//    }

    private fun moveMainActivity(loginResponse: LoginResponse){

        Log.d("message", loginResponse.message)

        // 메인 화면으로 이동
//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
//        Toast.makeText(this, "요리어터에 오신 걸 환영합니다!", Toast.LENGTH_SHORT).show()
    }
}