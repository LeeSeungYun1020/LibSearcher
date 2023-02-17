package com.leeseungyun1020.searcher.ui

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
import com.leeseungyun1020.searcher.adapters.ImageAdapter
import com.leeseungyun1020.searcher.data.Image
import com.leeseungyun1020.searcher.databinding.FragmentResultBinding
import com.leeseungyun1020.searcher.utilities.TAG
import com.leeseungyun1020.searcher.viewmodels.SearchViewModel
import kotlinx.coroutines.launch

class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewModel: SearchViewModel by activityViewModels()
        super.onViewCreated(view, savedInstanceState)
        val list = mutableListOf<Image>(
            Image(
                title = "title4",
                imageUrl = "https://rs.nxfs.nexon.com/gnb/images/logo_nexon.png",
                url = "https://www.nexon.com"
            )
        )
        val imageAdapter = ImageAdapter(list)
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 5)
            adapter = imageAdapter
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.imageResult.collect {
                    val end = list.size
                    list += it
                    imageAdapter.notifyItemRangeInserted(end, list.size - end)
                    Log.d(
                        TAG,
                        "onViewCreated: 또 호출??? ${binding.recyclerView.adapter != null} ${it}"
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}