package com.example.umc_7th_hackathon.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_7th_hackathon.databinding.ItemRecyclerviewBinding

class RecyclerViewAdapter(private val list: List<Item>) : RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(private val binding: ItemRecyclerviewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.img.setImageResource(item.img)
            binding.title.text = item.title
            binding.address.text = item.address
            binding.sunriseTime.text = item.sunrise_time
            binding.sunsetTime.text = item.sunset_time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}
