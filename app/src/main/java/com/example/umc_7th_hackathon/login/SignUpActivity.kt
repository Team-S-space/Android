package com.example.umc_7th_hackathon.login

import android.content.Context
import android.content.Intent
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
import com.example.umc_7th_hackathon.databinding.ActivitySignUpBinding
import com.example.umc_7th_hackathon.login.api.clientData.SignUpClient
import com.example.umc_7th_hackathon.login.api.UserRetrofitItf
import com.example.umc_7th_hackathon.login.api.reponseData.SignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    private var username: String = ""
    private var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // EditText 입력 감지
        binding.signupIdEt.addTextChangedListener(inputWatcher)
        binding.signupPwEt.addTextChangedListener(inputWatcher)

        // 가입 버튼 클릭 시
        binding.signupBt.setOnClickListener {
            username = binding.signupIdEt.text.toString()
            password = binding.signupPwEt.text.toString()

            when {
                !isUsernameValid(username) -> {
                    // 아이디가 유효하지 않을 경우
                    showErrorMessage("아이디는 5~13자의 길이여야 합니다.")
                }
                !isPasswordValid(password) -> {
                    // 비밀번호가 유효하지 않을 경우
                    showErrorMessage("비밀번호는 8자 이상이어야 합니다.")
                }
                else -> {
                    // 모든 조건이 만족되었을 경우
                    Log.d("회원가입", "회원가입 완료")
                    Toast.makeText(this, "회원가입이 완료되었습니다!", Toast.LENGTH_SHORT).show()
                    checkSignup()
                }
            }

        }

        // 아이디 입력 칸 클릭 시 오류 메시지 없애기
        binding.signupIdEt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.signupErTv.visibility = View.GONE
            }
        }

        // 비밀번호 입력 칸 클릭 시 오류 메시지 없애기
        binding.signupPwEt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.signupErTv.visibility = View.GONE
            }
        }

        // 배경화면 클릭 시 키보드 숨기기
        binding.signUpActivity.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN){
                currentFocus?.let { view ->
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                    view.clearFocus()
                }
            }
            true
        }

        binding.signupBtnTv.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private val inputWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // 아이디와 비밀번호 모두 입력되었는지 확인
            val isUsernameEntered = binding.signupIdEt.text.toString().isNotEmpty()
            val isPasswordEntered = binding.signupPwEt.text.toString().isNotEmpty()

            // 로그인 버튼 상태 업데이트
            if (isUsernameEntered && isPasswordEntered) {
                binding.signupBt.isEnabled = true
                binding.signupBt.backgroundTintList = ContextCompat.getColorStateList(this@SignUpActivity, R.color.chip_text_check) // 활성화 색상
            } else {
                binding.signupBt.isEnabled = false
                binding.signupBt.backgroundTintList = ContextCompat.getColorStateList(this@SignUpActivity, R.color.chip_text_uncheck) // 비활성화 색상
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    // 아이디 유효성 검사
    private fun isUsernameValid(username: String): Boolean {
        return username.length in 5..13
    }

    // 비밀번호 유효성 검사
    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }

    // 오류 메시지 표시
    private fun showErrorMessage(message: String) {
        binding.signupErTv.visibility = View.VISIBLE
        binding.signupErTv.text = message
    }

    // 오류 메시지 숨기기
    private fun hideErrorMessage() {
        binding.signupErTv.visibility = View.GONE
    }

    private fun checkSignup() {
        // 회원가입 API 연동
        val authService = RetrofitObj.getRetrofit().create(UserRetrofitItf::class.java)
        authService.signUp(SignUpClient(username, password)).enqueue(object:
            Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                Log.d("SIGNUP/SUCCESS", response.toString())

                when (response.code()){
                    200 -> {
                        val resp: SignUpResponse = response.body()!!
                        if (resp != null){
                            if (resp.isSuccess) {
                                val id = resp.result.id // 사용자 아이디 저장 (memberId)
                                Log.d("USERID/SUCCESS", id.toString())
                                moveLoginActivity(resp) // 회원가입 성공 시 로그인 화면으로 이동
                            } else {
                                Log.e("SIGNUP/FAILURE", "응답 코드: ${resp.code}, 응답 메시지: ${resp.message}")

                                // code가 MEMBER400일 때 오류 메시지 출력
                                if (resp.code == "MEMBER400") {
                                    errormessage() // 오류 메시지 출력
                                }
                            }

                        } else {
                            Log.d("SIGNUP/FAILURE", "Response body is null")
                            Log.e("SIGNUP/FAILURE", "응답 코드: ${resp.code}, 응답메시지: ${resp.message}")

                        }
                    }
                    else -> {
                        errormessage()
                    }
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Log.d("RETROFIT/FAILURE", t.message.toString())
            }

        })
    }

    private fun errormessage(){
        // "이미 사용 중인 닉네임" 텍스트
        binding.signupErTv.visibility = View.VISIBLE

        // EditText 비워줌 (사용자가 입력한 닉네임 삭제)
        binding.signupIdEt.text.clear()
        binding.signupIdEt.text.clear()
    }

    private fun moveLoginActivity(signUpResponse: SignUpResponse){

        Log.d("message", signUpResponse.message)
        Log.d("result", signUpResponse.result.id.toString())

        // 회원가입 성공 후 받은 아이디 저장
        var id: Int = signUpResponse.result.id
        Log.d("Nickname액티비티 사용자 아이디 값", id.toString())

        // 첫 번째 Toast 메시지 표시 (회원가입 완료)
        Toast.makeText(this, "회원가입이 완료되었습니다!", Toast.LENGTH_SHORT).show()

        // 로그인 화면으로 이동
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}