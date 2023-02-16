package com.leeseungyun1020.searcher.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leeseungyun1020.searcher.data.Image
import com.leeseungyun1020.searcher.databinding.ItemImageBinding

class ImageAdapter(private val list: List<Image>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    class ImageViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { view ->
                binding.data?.let { image ->
                    view.context.startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(image.url)))
                }
            }
        }

        fun bind(item: Image) {
            binding.apply {
                data = item
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}