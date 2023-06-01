package com.example.roadtracking.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roadtracking.common.extensions.longToDateConvert
import com.example.roadtracking.data.model.RoadUI
import com.example.roadtracking.databinding.ItemRoadBinding

class RoadAdapter(private val road: List<RoadUI>) :
    RecyclerView.Adapter<RoadAdapter.RoadViewHolder>() {

    inner class RoadViewHolder(private val binding: ItemRoadBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RoadUI) {
            binding.textDate.text = item.dateInMillis.longToDateConvert()
            binding.textCompany.text = item.company

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoadViewHolder {
        val binding = ItemRoadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RoadViewHolder(binding)
    }

    override fun getItemCount(): Int = road.size

    override fun onBindViewHolder(holder: RoadViewHolder, position: Int) =
        holder.bind(road[position])
}