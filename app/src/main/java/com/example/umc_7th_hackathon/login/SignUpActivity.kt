package com.example.umc_7th_hackathon.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_7th_hackathon.MainActivity
import com.example.umc_7th_hackathon.databinding.ActivitySignUpBinding
import com.example.umc_7th_hackathon.login.api.clientData.SignUpClient
import com.example.umc_7th_hackathon.login.api.UserRetrofitItf
import com.example.umc_7th_hackathon.RetrofitObj
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

        // 가입 버튼 클릭 시
        binding.signUpBt.setOnClickListener {
            username = binding.signUpIdEt.text.toString()
            password = binding.signUpPwEt.text.toString()

            // 아이디와 비밀번호 조건 검사
            if (isInputValid(username, password)){
                Log.d("회원가입", "회원가입 완료")
                Toast.makeText(this, "회원가입이 완료되었습니다!", Toast.LENGTH_SHORT).show()

                // 명세서 완성 전 메인으로 넘어가게
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
//                checkSignup()

            } else {
                // 오류 메시지 표시
                binding.signUpErTv.visibility = View.VISIBLE
            }

        }

        // 아이디 입력 칸 클릭 시 오류 메시지 없애기
        binding.signUpIdEt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.signUpErTv.visibility = View.GONE
            }
        }

        // 비밀번호 입력 칸 클릭 시 오류 메시지 없애기
        binding.signUpPwEt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.signUpErTv.visibility = View.GONE
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
    }

    // 입력된 아이디와 비밀번호의 유효성 검사
    private fun isInputValid(id: String, password: String): Boolean {
        return id.length in 5..13 && password.length >=8
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
        binding.signUpErTv.visibility = View.VISIBLE

        // EditText 비워줌 (사용자가 입력한 닉네임 삭제)
        binding.signUpIdEt.text.clear()
        binding.signUpPwEt.text.clear()
    }

    private fun saveId(id: Int){
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()){
            putInt("UserId", id) // 아이디 값 전달
            apply()
        }
    }

    private fun moveLoginActivity(signUpResponse: SignUpResponse){

        Log.d("message", signUpResponse.message)
        Log.d("result", signUpResponse.result.id.toString())

        // 회원가입 성공 후 받은 아이디 저장
        var id: Int = signUpResponse.result.id
        Log.d("Nickname액티비티 사용자 아이디 값", id.toString())

        saveId(id)

        // 첫 번째 Toast 메시지 표시 (회원가입 완료)
        Toast.makeText(this, "회원가입이 완료되었습니다!", Toast.LENGTH_SHORT).show()

        // 로그인 화면으로 이동
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

        // 두 번째 Toast 메시지 표시 (로그인 안내)
        Toast.makeText(this, "로그인을 진행해 주세요 :)", Toast.LENGTH_SHORT).show()
    }
}