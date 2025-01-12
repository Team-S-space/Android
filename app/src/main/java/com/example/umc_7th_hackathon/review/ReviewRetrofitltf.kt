package com.example.umc_7th_hackathon.review

import com.example.umc_7th_hackathon.review.api.clientData.AddReviewRequest
import retrofit2.Call
import retrofit2.http.POST
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("/api/reviews")
    fun addReview(
        @Part request: MultipartBody.Part,
        @Part reviewPicture: MultipartBody.Part // 파일
    ): Call<AddReviewRequest>
}

