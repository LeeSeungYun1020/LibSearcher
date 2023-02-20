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
import com.leeseungyun1020.searcher.data.ItemResult
import com.leeseungyun1020.searcher.data.News
import com.leeseungyun1020.searcher.databinding.FragmentResultBinding
import com.leeseungyun1020.searcher.utilities.Category
import com.leeseungyun1020.searcher.utilities.Mode
import com.leeseungyun1020.searcher.utilities.TAG
import com.leeseungyun1020.searcher.viewmodels.SearchViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val CATEGORY = "category"

class ResultFragment : Fragment() {

    companion object {
        fun newInstance(category: Category) = ResultFragment().apply {
            arguments = Bundle().apply {
                putSerializable(CATEGORY, category)
            }
        }
    }

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by activityViewModels()
    private var category: Category? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(CATEGORY, Category::class.java)
            } else {
                it.getSerializable(CATEGORY) as Category
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
        super.onViewCreated(view, savedInstanceState)
        when (category) {
            Category.NEWS -> {
                val list = mutableListOf<News>()
                initPagingRecyclerView(
                    list = list,
                    itemAdapter = NewsAdapter(list),
                    itemLayoutManager = LinearLayoutManager(context),
                    itemResult = viewModel.newsResult,
                    loadMoreItem = { viewModel.loadMore(Category.NEWS) }
                )
            }
            Category.IMAGE -> {
                val list = mutableListOf<Image>()
                initPagingRecyclerView(
                    list = list,
                    itemAdapter = ImageAdapter(list),
                    itemLayoutManager = GridLayoutManager(
                        context, when (resources.configuration.orientation) {
                            Configuration.ORIENTATION_PORTRAIT -> 3
                            Configuration.ORIENTATION_LANDSCAPE -> 5
                            else -> 3
                        }
                    ),
                    itemResult = viewModel.imageResult,
                    loadMoreItem = { viewModel.loadMore(Category.IMAGE) }
                )
            }
            null -> {
                Log.e(TAG, "ResultFragment onViewCreated: Not initialized")
            }
        }
    }

    private fun <T> initPagingRecyclerView(
        list: MutableList<T>,
        itemAdapter: RecyclerView.Adapter<*>,
        itemLayoutManager: RecyclerView.LayoutManager,
        itemResult: StateFlow<ItemResult<T>>,
        loadMoreItem: () -> Unit,
    ) {
        binding.recyclerView.apply {
            layoutManager = itemLayoutManager
            adapter = itemAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val lastVisibleItemPosition =
                        (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val lastItemPosition = itemAdapter.itemCount - 1

                    if (lastVisibleItemPosition == lastItemPosition) {
                        loadMoreItem()
                    }
                }
            })
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                itemResult.collect {
                    when (it.mode) {
                        Mode.ADD -> {
                            val start = list.size
                            val add = it.items.subList(start, it.items.size)
                            list += add
                            itemAdapter.notifyItemRangeInserted(start, add.size)
                            category?.let { cat -> viewModel.loadComplete(cat) }
                        }
                        Mode.REPLACE -> {
                            list.clear()
                            list += it.items
                            itemAdapter.notifyDataSetChanged()
                            binding.recyclerView.scrollToPosition(0)
                            category?.let { cat -> viewModel.loadComplete(cat) }
                        }
                        Mode.COMPLETE -> {
                            if (list.isEmpty()) {
                                list += it.items
                                itemAdapter.notifyItemRangeInserted(0, list.size)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}