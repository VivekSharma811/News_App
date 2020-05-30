package com.doubtnut.news.viewmodel.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doubtnut.news.model.repository.NewsRepository

class ListViewModelFactory(
    private val newsRepository: NewsRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ListViewModel(newsRepository) as T
    }

}