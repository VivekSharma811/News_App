package com.doubtnut.news.model.network.datasource

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doubtnut.news.model.data.Article
import com.doubtnut.news.model.data.NewsResponse
import com.doubtnut.news.model.network.NewsApiService
import com.doubtnut.news.util.NoConnectivityException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class NewsNetworkDataSourceImpl(
    private val newsApiService: NewsApiService
) : NewsNetworkDataSource {

    private val disposable = CompositeDisposable()

    private val _downloadedNews = MutableLiveData<List<Article>>()
    override val downloadedNews: LiveData<List<Article>>
        get() = _downloadedNews

    private val _error = MutableLiveData<Boolean>()
    override val error: LiveData<Boolean>
        get() = _error

    override fun fetchNews(source: String) {
            disposable.add(
                newsApiService.getHeadlines("bbc-news")
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<NewsResponse>() {
                        override fun onSuccess(t: NewsResponse) {
                            if(t.status.equals("ok")) {
                                _downloadedNews.postValue(t.articles)
                            } else {
                                Log.e("error", "Error : ${t.status}")
                            }
                        }

                        override fun onError(e: Throwable) {
                            _error.postValue(true)
                            Log.e("error", e.toString())
                        }
                    })
            )
    }
}