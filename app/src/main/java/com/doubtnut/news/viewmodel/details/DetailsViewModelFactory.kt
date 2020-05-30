package com.doubtnut.news.viewmodel.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doubtnut.news.model.repository.NewsRepository

class DetailsViewModelFactory(
    private val newsRepository: NewsRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailsViewModel(newsRepository) as T
    }
}