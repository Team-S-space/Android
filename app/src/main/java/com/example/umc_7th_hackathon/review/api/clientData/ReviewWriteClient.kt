package com.example.umc_7th_hackathon.review.api.clientData

data class ReviewWriteClient(
    var userId: String,
    var latitude : Int,
    var longitude : Int,
    var title : String,
    var image_url : String,
    var sun_event : Int
)