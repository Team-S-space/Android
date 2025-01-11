package com.example.umc_7th_hackathon.review.api.clientData

import com.google.gson.annotations.SerializedName

data class ReviewWriteClient(
    var userId: Long,
    var latitude: String,
    var longitude: String,
    var title: String,
    var sun_event: Int
)

data class AddReviewRequest(
    @SerializedName("userId") val userId: Int,
    @SerializedName("latitude") val latitude: String,
    @SerializedName("longitude") val longitude: String,
    @SerializedName("title") val title: String,
    @SerializedName("sunEvent") val sunEvent: Int
)