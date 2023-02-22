package com.leeseungyun1020.searcher.ui

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.leeseungyun1020.searcher.MainApplication
import com.leeseungyun1020.searcher.R
import com.leeseungyun1020.searcher.databinding.ActivitySearchBinding
import com.leeseungyun1020.searcher.network.NetworkManager
import com.leeseungyun1020.searcher.utilities.Category
import com.leeseungyun1020.searcher.viewmodels.SearchViewModel
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.utils.L
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private var initial = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val resultFragments = Category.values().associateWith {
            supportFragmentManager.findFragmentByTag(it.name) ?: ResultFragment.newInstance(it)
        }
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this))
        L.writeLogs(false)

        initUi()
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.result_container, resultFragments[Category.NEWS]!!, Category.NEWS.name)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.location.collect { location ->
                    if (initial) {
                        initial = false
                    } else if (location.move) {
                        supportFragmentManager.commit {
                            replace(
                                R.id.result_container,
                                resultFragments.getOrElse(location.category) {
                                    ResultFragment.newInstance(
                                        location.category
                                    )
                                },
                                location.category.name
                            )
                            setReorderingAllowed(true)
                            addToBackStack(location.category.name)
                        }
                    }
                }
            }
        }
    }

    private fun initUi() {
        binding.apply {
            searchBox.apply {
                setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        search()
                        true
                    } else {
                        false
                    }
                }
                setOnKeyListener { _, keyCode, keyEvent ->
                    if (keyEvent.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                        search()
                        true
                    } else {
                        false
                    }
                }
            }
            searchButton.setOnClickListener {
                search()
            }
            searchTab.apply {
                newsTabButton.setOnClickListener {
                    viewModel.onCategoryButtonClicked(Category.NEWS)
                }
                imageTabButton.setOnClickListener {
                    viewModel.onCategoryButtonClicked(Category.IMAGE)
                }
            }
        }
    }

    private fun search() {
        TypeSelectDialogFragment(
            types = (application as MainApplication).supportSearchTypes,
            titleId = R.string.search,
            emptyMessageId = R.string.search_unsupported_error,
            onSelect = { type ->
                NetworkManager.changeType(type)
                viewModel.search(binding.searchBox.text.toString())
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.searchBox.windowToken, 0)
            }
        ).show(supportFragmentManager, "search")
    }
}