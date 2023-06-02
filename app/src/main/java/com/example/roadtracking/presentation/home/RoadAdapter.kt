package com.example.roadtracking.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.roadtracking.common.extensions.longToDateConvert
import com.example.roadtracking.data.model.RoadUI
import com.example.roadtracking.databinding.ItemRoadBinding

class RoadAdapter : ListAdapter<RoadUI, RoadAdapter.RoadViewHolder>(RoadItemCallBack()) {

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

    override fun onBindViewHolder(holder: RoadViewHolder, position: Int) =
        holder.bind(getItem(position))

    class RoadItemCallBack : DiffUtil.ItemCallback<RoadUI>() {
        override fun areItemsTheSame(
            oldItem: RoadUI, newItem: RoadUI
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: RoadUI, newItem: RoadUI
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }
}