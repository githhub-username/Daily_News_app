package com.devatrii.dailynews.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.devatrii.dailynews.Models.ArticleModel
import com.devatrii.dailynews.repository.APIResponses
import com.devatrii.dailynews.repository.MainRepository

class MainViewModel(private val repository: MainRepository) : ViewModel() {
    var currentPage = 0
    var isLoadingArticles = false

    fun loadArticles() {
        isLoadingArticles = true
        currentPage++
        repository.getArticles(currentPage.toString())
    }

    val articles: LiveData<APIResponses<ArrayList<ArticleModel>>>
        get() = repository.articleLiveData

}