package com.leeseungyun1020.searcher.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.leeseungyun1020.searcher.adapters.NewsAdapter
import com.leeseungyun1020.searcher.data.News
import com.leeseungyun1020.searcher.databinding.FragmentResultBinding

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
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = NewsAdapter(
                listOf(
                    News(
                        title = "Title",
                        date = "2020/20/20",
                        imageUrl = "https://rs.nxfs.nexon.com/gnb/images/logo_nexon.png",
                        contents = "contents",
                        url = "https://www.nexon.com"
                    ),
                    News(
                        title = "Title",
                        date = "2020/20/20",
                        imageUrl = "https://rs.nxfs.nexon.com/gnb/images/logo_nexon.png",
                        contents = "contents",
                        url = "https://www.nexon.com"
                    )
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}