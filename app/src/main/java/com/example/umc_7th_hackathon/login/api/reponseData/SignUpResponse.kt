package com.example.umc_7th_hackathon.login.api.reponseData

data class SignUpResponse(
//    "isSuccess": true,
//    "code": 2000,
//"message": "OK",
//"result": {
//    "id": 1,
//    "createdAt": "2025-01-07T12:00:00",
//}
    var isSuccess: Boolean,
    var code: String,
    var message: String,
    var result: SignUpResult
)
data class SignUpResult(
    var createdAt: String,
    var updateAt: String,
    var id: Int,
    var userId: String,
    var password: String,
    var isAdmin: Boolean,
    var reviewList: SignUpReviewList
)
data class SignUpReviewList(
    var createdAt: String,
    var updateAt: String,
    var id: Int,
    var title: String,
    var imageUrl: String,
    var sunEvent: String,
    var user: String,
    var location: SignUpLocation
)
data class SignUpLocation(
    var createdAt: String,
    var updateAt: String,
    var id: Int,
    var latitude: String,
    var longitude: String,
    var address: String
)
