package com.leeseungyun1020.searcher.adapters

import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
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

class ImageDecoration(
    var spanCount: Int,
    var borderSpacing: Int,
    var gridSpacing: Int
) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        when (position % spanCount) {
            0 -> { // left side
                outRect.set(borderSpacing, gridSpacing, gridSpacing, gridSpacing)
            }
            spanCount - 1 -> { // right side
                outRect.set(gridSpacing, gridSpacing, borderSpacing, gridSpacing)
            }
            else -> { // center
                outRect.set(gridSpacing, gridSpacing, gridSpacing, gridSpacing)
            }
        }
        if (position < spanCount)
            outRect.top = borderSpacing
    }
}