package com.example.umc_7th_hackathon.review

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_7th_hackathon.R
import java.text.SimpleDateFormat
import java.util.*

class ReviewWriteActivity : AppCompatActivity() {

    private lateinit var sun: ImageView

    // 일출 및 일몰 시간 (예: 06:30, 18:00)
    private val sunriseTime = "06:30"
    private val sunsetTime = "18:00"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_review)

        // ImageView 초기화
        sun = findViewById(R.id.sun_iv)

        // 시간에 따라 이미지 업데이트
        updateImageBasedOnClosestSunTime()
    }

    // 현재 시간을 "HH:mm" 형식으로 반환
    fun getCurrentTime(): String {
//        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val sdf = "16:00"
        return sdf.format(Date()) // 현재 시간을 "HH:mm" 형식으로 반환
    }

    private fun updateImageBasedOnClosestSunTime() {
        val currentTime = getCurrentTime()

        // 일출과 일몰 상태에 따라 이미지 변경
        when {
            currentTime < sunriseTime -> {
                setSunImg(isSunrise = true) // 일출 전
            }
            currentTime > sunsetTime -> {
                setSunImg(isSunset = true) // 일몰 후
            }
            else -> {
                setSunImg(isSunrise = false, isSunset = false) // 일출과 일몰 사이
            }
        }
    }

    fun setSunImg(isSunrise: Boolean = false, isSunset: Boolean = false) {
        // 일출만 활성화
        if (isSunrise) {
            sun.setImageResource(R.drawable.ic_sunrise_on)
        }
        // 일몰만 활성화
        else if (isSunset) {
            sun.setImageResource(R.drawable.ic_sunset_on)
        }
        // 일출과 일몰 사이일 때는 이미지 변경 안 함
    }
}
