package com.example.umc_7th_hackathon.review.api.responseData

data class ReviewWriteResponse(
    var isSuccess: Boolean,
    var code: String,
    var message: String,
    var result: ReviewWriteResult
)

data class ReviewWriteResult(
    var reviewId: Int,
    var createdAt: String
)
