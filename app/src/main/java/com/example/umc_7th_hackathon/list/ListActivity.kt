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
        itemList.add(Item("전동진", "강원 강릉시 강동면 정동진리 64-3", "07:39 AM", "17:02 PM", 0))
        itemList.add(Item("왕왕", "부산", "06:30 AM", "06:30 PM", 0))
        itemList.add(Item("전동진에서 유명한 스팟이냐구", "대구", "07:00 AM", "07:00 PM",0))
        itemList.add(Item("전동진", "강원 강릉시 강동면 정동진리 64-3", "07:39 AM", "17:02 PM", 0))
        itemList.add(Item("왕왕", "부산", "06:30 AM", "06:30 PM", 0))
        itemList.add(Item("전동진에서 유명한 스팟이냐구", "대구", "07:00 AM", "07:00 PM",0))
        itemList.add(Item("전동진", "강원 강릉시 강동면 정동진리 64-3", "07:39 AM", "17:02 PM", 0))
        itemList.add(Item("왕왕", "부산", "06:30 AM", "06:30 PM", 0))
        itemList.add(Item("전동진에서 유명한 스팟이냐구", "대구", "07:00 AM", "07:00 PM",0))
        itemList.add(Item("전동진", "강원 강릉시 강동면 정동진리 64-3", "07:39 AM", "17:02 PM", 0))
        itemList.add(Item("왕왕", "부산", "06:30 AM", "06:30 PM", 0))
        itemList.add(Item("전동진에서 유명한 스팟이냐구", "대구", "07:00 AM", "07:00 PM",0))
        itemList.add(Item("전동진", "강원 강릉시 강동면 정동진리 64-3", "07:39 AM", "17:02 PM", 0))
        itemList.add(Item("왕왕", "부산", "06:30 AM", "06:30 PM", 0))
        itemList.add(Item("전동진에서 유명한 스팟이냐구", "대구", "07:00 AM", "07:00 PM",0))
        itemList.add(Item("전동진", "강원 강릉시 강동면 정동진리 64-3", "07:39 AM", "17:02 PM", 0))
        itemList.add(Item("왕왕", "부산", "06:30 AM", "06:30 PM", 0))
        itemList.add(Item("전동진에서 유명한 스팟이냐구", "대구", "07:00 AM", "07:00 PM",0))


        recyclerViewAdapter = RecyclerViewAdapter(itemList)

        binding.mainRecyclerview.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = recyclerViewAdapter
        }
    }
}
