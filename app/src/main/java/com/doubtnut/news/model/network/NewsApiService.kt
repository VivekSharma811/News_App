package com.doubtnut.news.model.network

import com.doubtnut.news.model.data.NewsResponse
import com.doubtnut.news.model.network.interceptor.ConnectivityInterceptor
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "c3e2eaa8beb64712bb4dddfe9b034e27"

const val BASE_URL = "https://newsapi.org/v2/"

interface NewsApiService {

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ) : NewsApiService {
            val requestInterceptor = Interceptor {chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("apiKey", API_KEY)
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NewsApiService::class.java)
        }
    }

    @GET("top-headlines")
    fun getHeadlines(
        @Query("sources") source : String
    ) : Single<NewsResponse>

}