package com.doubtnut.news.viewmodel.list

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doubtnut.news.model.data.Article
import com.doubtnut.news.model.data.NewsResponse
import com.doubtnut.news.model.data.Source
import com.doubtnut.news.model.network.NewsApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list.*

class ListViewModel : ViewModel() {

    private val disposable = CompositeDisposable()
    private val newsApiService = NewsApiService()

    val articles = MutableLiveData<List<Article>>()
    val loadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        fetchFromRemote()
    }

    private fun fetchFromRemote() {
        loading.value = true
        disposable.add(
            newsApiService.getHeadlines("bbc-news")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<NewsResponse>() {
                    override fun onSuccess(t: NewsResponse) {
                        t?.let {
                            articles.value = t.articles
                            loadError.value = false
                            loading.value = false
                        }
                    }

                    override fun onError(e: Throwable) {
                        loadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }

                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}