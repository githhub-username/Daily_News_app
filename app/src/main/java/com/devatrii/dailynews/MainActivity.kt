package com.devatrii.dailynews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.devatrii.dailynews.Adapters.LAYOUT_CARD
import com.devatrii.dailynews.Adapters.LAYOUT_LIST
import com.devatrii.dailynews.Adapters.MainAdapter
import com.devatrii.dailynews.Models.ArticleModel
import com.devatrii.dailynews.ViewModels.MainViewModel
import com.devatrii.dailynews.ViewModels.MainViewModelFactory
import com.devatrii.dailynews.databinding.ActivityMainBinding
import com.devatrii.dailynews.repository.APIResponses
import com.devatrii.dailynews.repository.MainRepository
import com.devatrii.dailynews.utils.PrefUtils
import com.devatrii.dailynews.utils.errorDialog
import com.devatrii.dailynews.utils.logInfo
import com.devatrii.dailynews.utils.removeView
import com.devatrii.dailynews.utils.showView


private const val TAG = "Main_Activity"
var LAYOUT_TYPE_SETTING = LAYOUT_LIST

class MainActivity : AppCompatActivity() {
    val activity = this
    lateinit var binding: ActivityMainBinding
    lateinit var mainViewModel: MainViewModel
    private val list: ArrayList<ArticleModel> = ArrayList()
    private val adapter = MainAdapter(list, activity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Setup Shimmer Layout base on view
        layoutChanger()
        setupShimmerLayout()
        loadArticlesFromBackend()
        recyclerViewSetup()
        binding.apply {
            pullToRefresh.setOnRefreshListener {
                pullToRefresh.isRefreshing = false
                list.clear()
                mainViewModel.currentPage = 0
                mainViewModel.loadArticles()
            }
        }

    }

    private fun layoutChanger() {
        // getting layout setting
        PrefUtils.init(activity)
        LAYOUT_TYPE_SETTING = PrefUtils.getPrefInt("layout_Type", LAYOUT_CARD)
        binding.mActionBar.apply {
            // Updating Layout Icon
            updateLayoutTypeIcon()
            // Updating App Layout
            mLayoutChanger.setOnClickListener {
                updateLayoutChangerIcon()
            }
        }
    }

    private fun updateLayoutTypeIcon() {
        binding.mActionBar.apply {
            if (LAYOUT_TYPE_SETTING == LAYOUT_LIST) {
                mLayoutChanger.setImageResource(R.drawable.layout_card)
            } else {
                mLayoutChanger.setImageResource(R.drawable.layout_list)
            }
        }
    }

    private fun updateLayoutChangerIcon() {
        updateLayoutTypeIcon()
        binding.mActionBar.apply {
            if (LAYOUT_TYPE_SETTING == LAYOUT_LIST) {
                PrefUtils.putPrefInt("layout_Type", LAYOUT_CARD)
                LAYOUT_TYPE_SETTING = PrefUtils.getPrefInt("layout_Type", LAYOUT_CARD)
            } else {
                PrefUtils.putPrefInt("layout_Type", LAYOUT_LIST)
                LAYOUT_TYPE_SETTING = PrefUtils.getPrefInt("layout_Type", LAYOUT_CARD)
            }
            updateUiBaseOnLayout()
        }
    }

    private fun updateUiBaseOnLayout() {
        if (list.isNotEmpty()) {
            val tempList: ArrayList<ArticleModel> = ArrayList()
            list.forEach {
                tempList.add(it)
            }
            list.clear()
            tempList.forEach {
                it.LAYOUT_TYPE = LAYOUT_TYPE_SETTING
                list.add(it)
            }
            logInfo(TAG, "ListSize ${list.size}")
            adapter.notifyDataSetChanged()
        }
    }

    private fun hideAllShimmer() {
        binding.apply {
            listShimmer.removeView()
            cardShimmer.removeView()
        }
    }

    private fun setupShimmerLayout() {
        binding.apply {
            if (LAYOUT_TYPE_SETTING == LAYOUT_CARD) cardShimmer.showView()
            else listShimmer.showView()
        }
    }

    private fun recyclerViewSetup() {
        binding.apply {
            mNewsRecyclerView.adapter = adapter
            mNewsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1) && !mainViewModel.isLoadingArticles) mainViewModel.loadArticles()
                }
            })
        }
    }

    private fun loadArticlesFromBackend() {
        val repository = MainRepository(activity)
        mainViewModel =
            ViewModelProvider(activity, MainViewModelFactory(repository))[MainViewModel::class.java]
        mainViewModel.loadArticles()
        mainViewModel.articles.observe(this) {
            when (it) {
                is APIResponses.Error -> {
                    binding.pullToRefresh.isRefreshing = false
                    errorDialog(
                        "Failed to load articles\nError: ${it.errorMessage}",
                        { dialogInterface, i ->
                            if (mainViewModel.currentPage == 1) {
                                list.clear()
                                mainViewModel.currentPage = 0
                                mainViewModel.loadArticles()
                            } else {
                                mainViewModel.loadArticles()
                            }
                        },
                        activity
                    )
                }

                is APIResponses.Loading -> {
                    // show shimmer
                    setupShimmerLayout()
                    binding.pullToRefresh.isRefreshing = true
                    logInfo(TAG, "Loading")
                }

                is APIResponses.Success -> {
                    // hide shimmer
                    hideAllShimmer()
                    binding.pullToRefresh.isRefreshing = false
                    it.data!!.forEach { model ->
                        if (model.excerpt.isNotEmpty()) {
                            list.add(model)
                        }
                    }
                    adapter.notifyDataSetChanged()
                    mainViewModel.isLoadingArticles = false
                }

            }
        }

    }
}