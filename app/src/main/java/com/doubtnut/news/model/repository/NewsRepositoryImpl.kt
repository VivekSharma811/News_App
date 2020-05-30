package com.doubtnut.news.model.repository

import androidx.lifecycle.LiveData
import com.doubtnut.news.model.dao.ArticleDao
import com.doubtnut.news.model.data.Article
import com.doubtnut.news.model.network.datasource.NewsNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class NewsRepositoryImpl(
    private val articleDao: ArticleDao,
    private val newsNetworkDataSource: NewsNetworkDataSource
) : NewsRepository {

    init {
        newsNetworkDataSource.downloadedNews.observeForever {
            storeNewsLocally(it)
        }
    }

    override suspend fun getNewsBypassCache(): LiveData<List<Article>> {
        return withContext(Dispatchers.IO) {
            fetchLatestNews()
            return@withContext articleDao.getAllArticles()
        }
    }

    override suspend fun getCurrentArticle(articleUuid : Int): LiveData<Article> {
        return withContext(Dispatchers.IO) {
            return@withContext articleDao.getArticle(articleUuid)
        }
    }

    override suspend fun checkError(): LiveData<Boolean> {
        return withContext(Dispatchers.IO) {
            return@withContext newsNetworkDataSource.error
        }
    }

    override suspend fun getNews(): LiveData<List<Article>> {
        return withContext(Dispatchers.IO) {
            initNewsData()
            return@withContext articleDao.getAllArticles()
        }
    }

    private fun storeNewsLocally(articles : List<Article>) {
        GlobalScope.launch {
            articleDao.deleteAll()
            articleDao.insertAll(*articles.toTypedArray())
        }
    }

    private fun initNewsData() {
        if(isFetchNeeded(ZonedDateTime.now()))
            fetchLatestNews()
    }

    private fun fetchLatestNews() {
        newsNetworkDataSource.fetchNews("bbc-news")
    }

    private fun isFetchNeeded(lastFetchedTime: ZonedDateTime) : Boolean {
        val fiveMinutesAgo = ZonedDateTime.now().minusMinutes(5)
        return lastFetchedTime.isBefore(fiveMinutesAgo)
    }
}