package com.doubtnut.news.util

import android.app.Application
import com.doubtnut.news.model.database.ArticleDatabase
import com.doubtnut.news.model.network.NewsApiService
import com.doubtnut.news.model.network.datasource.NewsNetworkDataSource
import com.doubtnut.news.model.network.datasource.NewsNetworkDataSourceImpl
import com.doubtnut.news.model.network.interceptor.ConnectivityInterceptor
import com.doubtnut.news.model.network.interceptor.ConnectivityInterceptorImpl
import com.doubtnut.news.model.repository.NewsRepository
import com.doubtnut.news.model.repository.NewsRepositoryImpl
import com.doubtnut.news.viewmodel.details.DetailsViewModelFactory
import com.doubtnut.news.viewmodel.list.ListViewModelFactory
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class NewsApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@NewsApplication))

        bind() from singleton { ArticleDatabase(instance()) }
        bind() from singleton { instance<ArticleDatabase>().articleDao() }

        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { NewsApiService(instance()) }
        bind<NewsNetworkDataSource>() with singleton { NewsNetworkDataSourceImpl(instance()) }
        bind<NewsRepository>() with singleton { NewsRepositoryImpl(instance(), instance()) }

        bind() from provider { ListViewModelFactory(instance()) }
        bind() from provider { DetailsViewModelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}