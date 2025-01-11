package com.example.umc_7th_hackathon.review.api.reponseData

data class ReviewDetailResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: ReviewDetail
)

data class ReviewDetail(
    val title: String,
    val imageUrl: String,
    val address: String,
    val sunEvent: String,
    val isAdmin: Boolean,
    val sunriseTime: String,
    val sunsetTime: String,
    val createdAt: String
)
