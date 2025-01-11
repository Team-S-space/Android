package com.example.umc_7th_hackathon.review

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_7th_hackathon.R
import com.example.umc_7th_hackathon.databinding.BottomReviewBinding
import java.text.SimpleDateFormat
import java.util.*

class ReviewWriteActivity : AppCompatActivity() {

    private lateinit var binding: BottomReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Binding 객체 초기화
        binding = BottomReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}
