package com.doubtnut.news.viewmodel.list

import androidx.lifecycle.ViewModel
import com.doubtnut.news.model.repository.NewsRepository
import com.doubtnut.news.util.lazyDeferred

class ListViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    val news by lazyDeferred {
        newsRepository.getNews()
    }

    val newsReload by lazyDeferred { newsRepository.getNewsBypassCache() }

    val error by lazyDeferred { newsRepository.checkError() }
}