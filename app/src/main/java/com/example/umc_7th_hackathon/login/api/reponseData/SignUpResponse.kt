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
    var id: Int,
    var createAt: String
)
