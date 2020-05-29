package com.doubtnut.news.viewmodel.list

import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doubtnut.news.model.data.Article
import com.doubtnut.news.model.data.NewsResponse
import com.doubtnut.news.model.data.Source
import com.doubtnut.news.model.database.ArticleDatabase
import com.doubtnut.news.model.network.NewsApiService
import com.doubtnut.news.viewmodel.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ListViewModel(application: Application) : BaseViewModel(application) {

    private val disposable = CompositeDisposable()
    private val newsApiService = NewsApiService()

    val articles = MutableLiveData<List<Article>>()
    val loadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        fetchFromRemote()
    }

    fun fetchFromDatabase() {
        loading.value = true
        launch {
            val articles = ArticleDatabase(getApplication()).articleDao().getAllArticles()
            articlesRetrieved(articles)
            Toast.makeText(getApplication(), "DB", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchFromRemote() {
        loading.value = true
        disposable.add(
            newsApiService.getHeadlines("bbc-news")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<NewsResponse>() {
                    override fun onSuccess(t: NewsResponse) {
                        storeArticlesLocally(t.articles!!)
                        Toast.makeText(getApplication(), "Endpoint", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(e: Throwable) {
                        loadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun articlesRetrieved(articlesList : List<Article>) {
        articles.value = articlesList
        loadError.value = false
        loading.value = false
    }

    private fun storeArticlesLocally(articlesList: List<Article>) {
        launch {
            val articleDao = ArticleDatabase(getApplication()).articleDao()
            articleDao.deleteAll()
            var result = articleDao.insertAll(*articlesList!!.toTypedArray())
            var i =0
            while(i < articlesList.size) {
                articlesList[i].uuid = result[i].toInt()
                ++i
            }
            articlesRetrieved(articlesList)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}