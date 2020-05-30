package com.doubtnut.news.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.doubtnut.news.R
import com.doubtnut.news.model.data.Article
import com.doubtnut.news.viewmodel.list.ListViewModel
import com.doubtnut.news.viewmodel.list.ListViewModelFactory
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class ListFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory : ListViewModelFactory by instance()

    private lateinit var viewModel : ListViewModel
    private val newsListAdapter = NewsListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ListViewModel::class.java)

        newsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsListAdapter
        }

        refreshLayout.setOnRefreshListener {
            newsList.visibility = View.GONE
            loadingError.visibility = View.GONE
            loadingView.visibility = View.VISIBLE
            bypassCache()
            refreshLayout.isRefreshing = false
        }
        getNews()
        checkForError()
    }

    private fun checkForError()  = launch {
        viewModel.error.await().observe(viewLifecycleOwner, Observer {
            if(it) {
                loadingView.visibility = View.GONE
                newsList.visibility = View.GONE
                loadingError.visibility = View.VISIBLE
            }
        })
    }

    private fun getNews() = launch {
        val news = viewModel.news.await()
        bindUi(news)
    }

    private fun bypassCache() = launch {
        val news = viewModel.newsReload.await()
        bindUi(news)
    }

    private fun bindUi(news : LiveData<List<Article>>) = launch {
        news.observe(viewLifecycleOwner, Observer {
            if(it == null) {
                return@Observer
            } else {
                loadingError.visibility = View.GONE
                loadingView.visibility = View.GONE
                newsList.visibility = View.VISIBLE
                newsListAdapter.updateNewsList(it)
            }
        })
    }
}
