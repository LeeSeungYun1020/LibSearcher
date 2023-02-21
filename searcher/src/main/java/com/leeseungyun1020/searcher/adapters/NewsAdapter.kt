package com.leeseungyun1020.searcher.adapters

import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leeseungyun1020.searcher.data.News
import com.leeseungyun1020.searcher.databinding.ItemNewsBinding

class NewsAdapter(private val list: List<News>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    class NewsViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { view ->
                binding.news?.let { news ->
                    view.context.startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(news.url)))
                }
            }
        }

        fun bind(item: News) {
            binding.apply {
                news = item
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            ItemNewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class NewsDecoration(private val verticalSpacing: Int, private val horizontalSpacing: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        outRect.top = verticalSpacing
        outRect.left = horizontalSpacing
        outRect.right = horizontalSpacing
        outRect.bottom = verticalSpacing
        if (position == 0) {
            outRect.top = verticalSpacing * 2
        }
    }
}