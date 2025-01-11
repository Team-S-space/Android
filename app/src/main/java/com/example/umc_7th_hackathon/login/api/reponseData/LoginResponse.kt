package com.example.umc_7th_hackathon.login.api.reponseData

data class LoginResponse(
//    "isSuccess": true,
//    "code": 2000,
//    "message": "OK",
//    "result": "로그인이 성공적으로 완료되었습니다"

    var isSuccess: Boolean,
    var code: String,
    var message: String,
    var result: Result
)
data class Result(
    var createdAt: String,
    var updateAt: String,
    var id: Int,
    var userId: String,
    var password: String,
    var isAdmin: Boolean,
    var reviewList: ReviewList
)
data class ReviewList(
    var createdAt: String,
    var updateAt: String,
    var id: Int,
    var title: String,
    var imageUrl: String,
    var sunEvent: String,
    var user: String,
    var location: Location
)
data class Location(
    var createdAt: String,
    var updateAt: String,
    var id: Int,
    var latitude: String,
    var longitude: String,
    var address: String
)
