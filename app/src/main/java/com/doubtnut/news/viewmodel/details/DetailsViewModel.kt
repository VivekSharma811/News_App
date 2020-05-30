package com.doubtnut.news.viewmodel.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.doubtnut.news.model.data.Article
import com.doubtnut.news.model.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DetailsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    suspend fun refresh(articleUuid : Int) : LiveData<Article> {
        return newsRepository.getCurrentArticle(articleUuid)
    }
}