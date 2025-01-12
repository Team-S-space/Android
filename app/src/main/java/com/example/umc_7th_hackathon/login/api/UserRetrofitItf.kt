package com.example.umc_7th_hackathon.login.api

import com.example.umc_7th_hackathon.login.api.clientData.LoginClient
import com.example.umc_7th_hackathon.login.api.clientData.SignUpClient
import com.example.umc_7th_hackathon.login.api.reponseData.LoginResponse
import com.example.umc_7th_hackathon.login.api.reponseData.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserRetrofitItf {
    // 회원가입
    @POST("/api/auth/signup")
    fun signUp(@Body signUpClient: SignUpClient): Call<SignUpResponse>

    // 로그인
    @POST("/api/auth/login")
    fun login(@Body loginClient: LoginClient): Call<LoginResponse>
}