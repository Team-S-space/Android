package com.example.umc_7th_hackathon.review

import androidx.room.Query
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.umc_7th_hackathon.review.api.clientData.ReviewWriteClient
import com.example.umc_7th_hackathon.review.api.reponseData.ReviewDetailResponse
import com.example.umc_7th_hackathon.review.api.reponseData.ReviewResponse
import com.example.umc_7th_hackathon.review.api.reponseData.ReviewWriteResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ReviewRetrofitltf {
    // 리뷰 작성
    @POST("/api/reviews")
    fun reviewWrite(@Body reviewWriteClient: ReviewWriteClient): Call<ReviewWriteResponse>

    // 리뷰 상세 정보 조회
    @GET("/api/reviews/{reviewId}")
    fun getReviewDetail(@Path("reviewId") reviewId: Int): Call<ReviewDetailResponse>

    // 특정 지역의 리뷰 목록을 받아오는 메서드
//    @GET("/api/reviews")
//    fun getReviewsByRegion(
//        @Query("region") region: String // 지역명을 쿼리 스트링으로 전달
//    ): Call<ReviewResponse>
}
