package com.example.umc_7th_hackathon.login

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.umc_7th_hackathon.MainActivity
import com.example.umc_7th_hackathon.R
import com.example.umc_7th_hackathon.RetrofitObj
import com.example.umc_7th_hackathon.databinding.ActivityLoginBinding
import com.example.umc_7th_hackathon.login.api.clientData.LoginClient
import com.example.umc_7th_hackathon.login.api.UserRetrofitItf
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

        // EditText 입력 감지
        binding.loginIdEt.addTextChangedListener(inputWatcher)
        binding.loginPwEt.addTextChangedListener(inputWatcher)

        // 로그인 버튼 클릭 시
        binding.loginBt.setOnClickListener {

            username = binding.loginIdEt.text.toString()
            password = binding.loginPwEt.text.toString()

            CheckLogin()
        }

        // 회원가입 버튼
        binding.signupBtnTv.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
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

    private fun CheckLogin() {
        // 입력하지 않은 경우 (빈칸)
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "아이디와 비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // 로그인 API 연동
        val authService = RetrofitObj.getRetrofit().create(UserRetrofitItf::class.java)
        authService.login(LoginClient(username, password)).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("LOGIN/SUCCESS", response.toString())

                if (response.isSuccessful && response.body() != null) {
                    val resp = response.body()!!

                    if (resp.isSuccess) {
                        // 로그인 성공: 메인 화면으로 이동
                        Log.d("아이디 값", resp.result.id.toString())
                        moveMainActivity(resp)
                    } else {
                        // 로그인 실패: 오류 메시지 표시
                        binding.loginErTv.visibility = View.VISIBLE
                        binding.loginErTv.text = resp.message // 서버에서 제공한 오류 메시지
                        Log.e("LOGIN/FAILURE", "응답 코드: ${resp.code}, 메시지: ${resp.message}")
                    }
                } else {
                    // 서버 응답 실패: HTTP 상태 코드 확인
                    binding.loginErTv.visibility = View.VISIBLE
                    binding.loginErTv.text = "로그인에 실패했습니다. 다시 시도해주세요."
                    Log.e("LOGIN/ERROR", "HTTP 코드: ${response.code()}, 메시지: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // 네트워크 요청 실패 처리
                binding.loginErTv.visibility = View.VISIBLE
                binding.loginErTv.text = "서버와의 연결에 실패했습니다. 인터넷 연결을 확인해주세요."
                Log.e("RETROFIT/FAILURE", t.message.toString())
            }
        })
    }


    private fun errormessage(){
        // "이미 사용 중인 닉네임" 텍스트
        binding.loginErTv.visibility = View.VISIBLE

        // EditText 비워줌 (사용자가 입력한 닉네임 삭제)
        binding.loginIdEt.text.clear()
        binding.loginIdEt.text.clear()
    }

    private val inputWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // 아이디와 비밀번호 모두 입력되었는지 확인
            val isUsernameEntered = binding.loginIdEt.text.toString().isNotEmpty()
            val isPasswordEntered = binding.loginPwEt.text.toString().isNotEmpty()

            // 로그인 버튼 상태 업데이트
            if (isUsernameEntered && isPasswordEntered) {
                binding.loginBt.isEnabled = true
                binding.loginBt.backgroundTintList = ContextCompat.getColorStateList(this@LoginActivity, R.color.chip_text_check) // 활성화 색상
            } else {
                binding.loginBt.isEnabled = false
                binding.loginBt.backgroundTintList = ContextCompat.getColorStateList(this@LoginActivity, R.color.chip_text_uncheck) // 비활성화 색상
            }
        }

        override fun afterTextChanged(s: Editable?) {}
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
    private fun saveId(id: Int){
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()){
            putInt("UserId", id) // 아이디 값 전달
            apply()
        }
    }

    private fun moveMainActivity(loginResponse: LoginResponse){
        // 로그인 연동 후 받은 아이디 저장
        var id: Int = loginResponse.result.id
        Log.d("Nickname액티비티 사용자 아이디 값", id.toString())
        saveId(id)

        // 메인 화면으로 이동
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}