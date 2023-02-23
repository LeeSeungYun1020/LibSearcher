package com.leeseungyun1020.searcher.ui

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.fragment.app.Fragment
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
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var resultFragments: Map<Category, Fragment>
    private val viewModel: SearchViewModel by viewModels()
    private var initial = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultFragments = Category.values().associateWith {
            supportFragmentManager.findFragmentByTag(it.name) ?: ResultFragment.newInstance(it)
        }
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.result_container, resultFragments[Category.NEWS]!!, Category.NEWS.name)
            }
        }
        observeChanges()
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
        val types = (application as MainApplication).supportSearchTypes
        if (types.size == 1) {
            val keyword = binding.searchBox.text.toString()
            if (viewModel.keyword.value != keyword) {
                viewModel.search(keyword)
            }
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.searchBox.windowToken, 0)
        } else {
            TypeSelectDialogFragment(
                types = types,
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

    private fun observeChanges() {
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

                    when (location.category) {
                        Category.NEWS -> {
                            binding.searchTab.apply {
                                selectTabItem(newsTabButton, newsTabButtonIndicator)
                                deselectTabItem(imageTabButton, imageTabButtonIndicator)
                            }
                        }
                        Category.IMAGE -> {
                            binding.searchTab.apply {
                                deselectTabItem(newsTabButton, newsTabButtonIndicator)
                                selectTabItem(imageTabButton, imageTabButtonIndicator)
                            }
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tabVisibility.collect { isVisible ->
//                    binding.searchTab.tabGroup?.visibility = if (isVisible) {
//                        View.VISIBLE
//                    } else {
//                        View.GONE
//                    }
                }
            }
        }
    }

    private fun selectTabItem(tabButton: View, tabButtonIndicator: View?) {
        val tabIndicatorColor = ContextCompat.getColor(this, R.color.tab_indicator)
        tabButtonIndicator?.visibility = View.VISIBLE
        (tabButton as? Button)?.setColor(tabIndicatorColor)
        (tabButton as? ImageButton)?.setColorFilter(tabIndicatorColor)
    }

    private fun deselectTabItem(tabButton: View, tabButtonIndicator: View?) {
        val typedValue = TypedValue()
        theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true)
        val colorRes = typedValue.run { if (resourceId != 0) resourceId else data }
        val defaultTextColor = ContextCompat.getColor(this@SearchActivity, colorRes)
        tabButtonIndicator?.visibility = View.INVISIBLE
        (tabButton as? Button)?.setColor(defaultTextColor)
        (tabButton as? ImageButton)?.setColorFilter(defaultTextColor)
    }

    private fun Button.setColor(@ColorInt color: Int) {
        compoundDrawables.getOrNull(1)?.colorFilter =
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                color,
                BlendModeCompat.SRC_ATOP
            )
        setTextColor(color)
    }
}