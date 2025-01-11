package com.example.umc_7th_hackathon.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_7th_hackathon.databinding.ActivityListBinding

class ListActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityListBinding.inflate(layoutInflater)
    }
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 데이터 생성
        val itemList = ArrayList<Item>()
        itemList.add(Item("전동진에서 유명한 스팟이냐구", "서울", "06:00 AM", "06:00 PM", 0))
        itemList.add(Item("전동진에서 유명한 스팟이냐구", "부산", "06:30 AM", "06:30 PM", 0))
        itemList.add(Item("전동진에서 유명한 스팟이냐구", "대구", "07:00 AM", "07:00 PM",0))

        recyclerViewAdapter = RecyclerViewAdapter(itemList)

        binding.mainRecyclerview.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = recyclerViewAdapter
        }
    }
}
