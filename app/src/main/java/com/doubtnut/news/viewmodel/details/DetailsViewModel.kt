package com.doubtnut.news.viewmodel.details

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.doubtnut.news.model.data.Article
import com.doubtnut.news.model.database.ArticleDatabase
import com.doubtnut.news.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application) : BaseViewModel(application) {

    val article = MutableLiveData<Article>()

    fun refresh(articleUuid : Int) {
        fetchFromDatabase(articleUuid)
    }

    private fun fetchFromDatabase(articleUuid: Int) {
        launch {
            val articleValue = ArticleDatabase(getApplication()).articleDao().getArticle(articleUuid)
            article.value = articleValue
        }
    }

}