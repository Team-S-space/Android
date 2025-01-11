package com.example.umc_7th_hackathon.review

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_7th_hackathon.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class ReviewWrite : AppCompatActivity() {

    private lateinit var imageButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_review)

        // ImageView 초기화
        imageButton = findViewById(R.id.sun_iv)

        // 시간에 따라 이미지 업데이트
        updateImageBasedOnClosestSunTime()
    }

    private fun updateImageBasedOnClosestSunTime() {
        // 일출 및 일몰 시간 설정 (HH:mm 형식)
        val sunriseTime = "07:47"
        val sunsetTime = "17:34"

        // 현재 시간 가져오기 (현재 시간으로 자동 설정)
        val currentTimeCalendar = Calendar.getInstance()

        // 일출 및 일몰 시간을 Calendar로 변환
        val sunriseCalendar = parseTimeToCalendar(sunriseTime)
        val sunsetCalendar = parseTimeToCalendar(sunsetTime, isSunset = true)

        // 현재 시간과 일출/일몰 시간의 차이 계산 (밀리초 단위)
        val diffWithSunrise = abs(currentTimeCalendar.timeInMillis - sunriseCalendar.timeInMillis)
        val diffWithSunset = abs(currentTimeCalendar.timeInMillis - sunsetCalendar.timeInMillis)

        // 더 가까운 시간을 기준으로 이미지 변경
        if (diffWithSunrise <= diffWithSunset) {
            // 일출이 더 가까움
            imageButton.setImageResource(R.drawable.ic_sunrise_on)
        } else {
            // 일몰이 더 가까움
            imageButton.setImageResource(R.drawable.ic_sunset_on)
        }
    }


    private fun parseTimeToCalendar(time: String, isSunset: Boolean = false): Calendar {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val date = dateFormat.parse(time)

        if (date != null) {
            calendar.time = date
            calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR))
            calendar.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH))
            calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH))

            // 일몰 시간은 전날일 수 있으므로 처리
            if (isSunset && calendar.after(Calendar.getInstance())) {
                calendar.add(Calendar.DAY_OF_MONTH, -1) // 하루 빼기
            }
        }
        return calendar
    }

}
