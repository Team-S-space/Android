package com.example.umc_7th_hackathon.review

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_7th_hackathon.databinding.BottomReviewBinding
import com.example.umc_7th_hackathon.review.api.clientData.AddReviewRequest
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class ReviewWriteActivity : AppCompatActivity() {

    private lateinit var binding: BottomReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Binding 객체 초기화
        binding = BottomReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomSheetHandle.setOnClickListener{
            Toast.makeText(this,"text", Toast.LENGTH_SHORT ).show()
        }

        // 바텀시트에서 URI와 제목을 받아서 서버로 전송
        binding.writingBt.setOnClickListener {
            val title = binding.titleEt.text.toString() // EditText에서 제목 가져오기
            val photoUriString = intent.getStringExtra("photoUri") // MainActivity에서 전달받은 photoUri
//            Log.d("photoUriString", "test : $photoUriString")
            if (photoUriString != null) {
                val photoUri = Uri.parse(photoUriString)
                sendReview(photoUri, title) // 서버로 전송
            }
        }
    }

    // 사진 URI를 파일로 변환하여 MultipartBody.Part로 만들기
    fun createImagePart(uri: Uri, key: String): MultipartBody.Part {
        val file = File(getRealPathFromURI(uri)) // 실제 파일 경로를 가져오는 함수 사용
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(key, file.name, requestFile)
    }

    // URI를 실제 파일 경로로 변환하는 함수
    fun getRealPathFromURI(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex("_data")
        val filePath = columnIndex?.let { cursor.getString(it) }
        cursor?.close()
        return filePath ?: uri.path ?: ""
    }

    // 서버로 리뷰 보내기
    fun sendReview(photoUri: Uri, title: String) {
        // 현재 위치를 받아오는 방법 (위도, 경도 값)
        val latitude = 37.5665 // 예시: 서울의 위도 값
        val longitude = 126.9780 // 예시: 서울의 경도 값
        val userId = 1 // 사용자 ID는 예시로 1로 설정, 실제로는 로그인한 사용자의 ID로 설정해야 함
        val sunEvent = 0 // 예시: 이벤트 값, 실제 값에 맞게 설정해야 함

        // Retrofit 초기화
        val retrofit = Retrofit.Builder()
            .baseUrl("http://3.38.222.221:8080") // 서버 주소
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        Log.d("sendReview", "test")

        val service = retrofit.create(ApiService::class.java)

        // 이미지와 텍스트 데이터 준비
        val imagePart = createImagePart(photoUri, "reviewPicture") // "reviewPicture"는 서버에서 요구하는 필드명
        val addReviewRequest = AddReviewRequest(
            userId = userId,
            latitude = latitude.toString(),
            longitude = longitude.toString(),
            title = title,
            sunEvent = sunEvent
        )

        // AddReviewRequest 객체를 RequestBody로 변환
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), Gson().toJson(addReviewRequest))

        // API 호출
        val call = service.addReview(
            request = MultipartBody.Part.createFormData("request", null, requestBody),
            reviewPicture = imagePart
        )

        call.enqueue(object : Callback<AddReviewRequest> {
            override fun onResponse(
                call: Call<AddReviewRequest>,
                response: Response<AddReviewRequest>
            ) {
                if (response.isSuccessful) {
                    val reviewResponse = response.body()
                    Log.d("ReviewWrite", "리뷰 작성 성공: $reviewResponse")
                } else {
                    Log.d("ReviewWrite", "서버 오류: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<AddReviewRequest>, t: Throwable) {
                Log.d("ReviewWrite", "네트워크 실패: ${t.message}")
            }
        })
    }
}
