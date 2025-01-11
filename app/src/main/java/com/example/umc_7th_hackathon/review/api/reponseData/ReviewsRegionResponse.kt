package com.example.umc_7th_hackathon.review.api.reponseData

data class ReviewResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: List<Review>
)

data class Review(
    val title: String
)
