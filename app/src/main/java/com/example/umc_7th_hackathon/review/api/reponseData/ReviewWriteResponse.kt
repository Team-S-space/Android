package com.example.umc_7th_hackathon.review.api.reponseData

data class ReviewWriteResponse(
    var isSuccess: Boolean,
    var code: String,
    var message: String,
    var result: ReviewWriteResult
)

data class ReviewWriteResult(
    var id: Int,
    var createdAt: String
)


