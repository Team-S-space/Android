package com.example.umc_7th_hackathon.review

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_7th_hackathon.R
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
        //val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val sdf = "14:00"
        return sdf.format(Date()) // 현재 시간을 "HH:mm" 형식으로 반환
    }

    private fun updateImageBasedOnClosestSunTime() {
        val currentTime = getCurrentTime()
        val isSunrise = currentTime!! < sunriseTime && currentTime!! > sunsetTime // 일출
        val isSunset = currentTime!! < sunsetTime && currentTime!! > sunriseTime // 일몰

        // 일출과 일몰 상태에 따라 이미지 변경
        when {
            isSunrise -> {
                setSunImg(sunriseActive = true, sunsetActive = false)
            }
            isSunset -> {
                setSunImg(sunriseActive = false, sunsetActive = true)
            }
        }
    }

    fun setSunImg(sunriseActive: Boolean, sunsetActive: Boolean) {
        sun.tag = if (sunriseActive) "active" else "inactive"
        sun.tag = if (sunsetActive) "active" else "inactive"

        sun.setImageResource(
            if (sunriseActive) R.drawable.ic_sunrise_on else R.drawable.ic_sunrise_off
        )
        sun.setImageResource(
            if (sunsetActive) R.drawable.ic_sunset_on else R.drawable.ic_sunset_off
        )
    }
}
