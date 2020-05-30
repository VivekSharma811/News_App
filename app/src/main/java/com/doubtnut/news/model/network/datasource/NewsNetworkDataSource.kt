package com.doubtnut.news.model.network.datasource

import androidx.lifecycle.LiveData
import com.doubtnut.news.model.data.Article

interface NewsNetworkDataSource {

    val downloadedNews : LiveData<List<Article>>
    val error : LiveData<Boolean>

    fun fetchNews(
        source : String
    )

}