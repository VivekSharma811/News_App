package com.doubtnut.news.model.repository

import androidx.lifecycle.LiveData
import com.doubtnut.news.model.data.Article

interface NewsRepository {

    suspend fun getNews() : LiveData<List<Article>>

    suspend fun getNewsBypassCache() : LiveData<List<Article>>

    suspend fun getCurrentArticle(uuid : Int) : LiveData<Article>

    suspend fun checkError() : LiveData<Boolean>
}