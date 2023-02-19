package com.leeseungyun1020.searcher.ui

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leeseungyun1020.searcher.adapters.ImageAdapter
import com.leeseungyun1020.searcher.adapters.NewsAdapter
import com.leeseungyun1020.searcher.data.Image
import com.leeseungyun1020.searcher.data.News
import com.leeseungyun1020.searcher.databinding.FragmentResultBinding
import com.leeseungyun1020.searcher.network.NetworkManager
import com.leeseungyun1020.searcher.utilities.Mode
import com.leeseungyun1020.searcher.utilities.ResultCategory
import com.leeseungyun1020.searcher.utilities.TAG
import com.leeseungyun1020.searcher.viewmodels.SearchViewModel
import kotlinx.coroutines.launch

private const val CATEGORY = "category"

class ResultFragment : Fragment() {

    companion object {
        fun newInstance(category: ResultCategory) = ResultFragment().apply {
            arguments = Bundle().apply {
                putSerializable(CATEGORY, category)
            }
        }
    }

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private var category: ResultCategory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(CATEGORY, ResultCategory::class.java)
            } else {
                it.getSerializable(CATEGORY) as ResultCategory
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewModel: SearchViewModel by activityViewModels()
        super.onViewCreated(view, savedInstanceState)
        when (category) {
            ResultCategory.NEWS -> {
                val list = mutableListOf<News>()
                val newsAdapter = NewsAdapter(list)
                binding.recyclerView.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = newsAdapter
                    addOnScrollListener(object: RecyclerView.OnScrollListener(){
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)

                            val lastVisibleItemPosition = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                            val lastItemPosition = newsAdapter.itemCount - 1

                            if (lastVisibleItemPosition == lastItemPosition) {
                                viewModel.loadMoreNews()
                            }
                        }
                    })
                }
                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.newsResult.collect {
                            when (it.mode) {
                                Mode.ADD -> {
                                    val start = list.size
                                    val add = it.news.subList(start, it.news.size)
                                    list += add
                                    newsAdapter.notifyItemRangeInserted(start, add.size)
                                }
                                Mode.REPLACE -> {
                                    list.clear()
                                    list += it.news
                                    newsAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }
            }
            ResultCategory.IMAGE -> {
                val list = mutableListOf<Image>()
                val imageAdapter = ImageAdapter(list)
                binding.recyclerView.apply {
                    layoutManager = GridLayoutManager(
                        context, when (resources.configuration.orientation) {
                            Configuration.ORIENTATION_PORTRAIT -> 3
                            Configuration.ORIENTATION_LANDSCAPE -> 5
                            else -> 3
                        }
                    )
                    adapter = imageAdapter
                    addOnScrollListener(object: RecyclerView.OnScrollListener(){
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)

                            val lastVisibleItemPosition = (layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                            val lastItemPosition = imageAdapter.itemCount - 1

                            if (lastVisibleItemPosition == lastItemPosition) {
                                viewModel.loadMoreImage()
                            }
                        }
                    })
                }
                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.imageResult.collect {
                            when (it.mode) {
                                Mode.ADD -> {
                                    val start = list.size
                                    val add = it.images.subList(start, it.images.size)
                                    list += add
                                    imageAdapter.notifyItemRangeInserted(start, add.size)
                                }
                                Mode.REPLACE -> {
                                    list.clear()
                                    list += it.images
                                    imageAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }
            }
            null -> {
                Log.e(TAG, "ResultFragment onViewCreated: Not initialized")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}