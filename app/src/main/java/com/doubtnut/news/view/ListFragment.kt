package com.doubtnut.news.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.doubtnut.news.R
import com.doubtnut.news.model.data.NewsResponse
import com.doubtnut.news.model.network.NewsApiService
import com.doubtnut.news.viewmodel.list.ListViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

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

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()

        newsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsListAdapter
        }

        refreshLayout.setOnRefreshListener {
            newsList.visibility = View.GONE
            loadingError.visibility = View.GONE
            loadingView.visibility = View.VISIBLE
            //viewModel.refresh()
            viewModel.fetchFromDatabase()
            refreshLayout.isRefreshing = false
        }

        observeViewModel()
    }

    fun observeViewModel() {
        viewModel.articles.observe(viewLifecycleOwner, Observer {articles ->
            articles?.let {
                newsList.visibility = View.VISIBLE
                newsListAdapter.updateNewsList(articles)
            }
        })
        viewModel.loadError.observe(viewLifecycleOwner, Observer { isError ->
            isError?.let {
                loadingError.visibility = if(it) View.VISIBLE else View.GONE
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            it?.let {
                loadingView.visibility = if(it) View.VISIBLE else View.GONE
                if(it) {
                    loadingError.visibility = View.GONE
                    newsList.visibility = View.GONE
                }
            }
        })
    }
}
