package com.doubtnut.news.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.doubtnut.news.R
import com.doubtnut.news.databinding.ItemNewsBinding
import com.doubtnut.news.model.data.Article
import com.doubtnut.news.util.NewsClickListener
import com.doubtnut.news.util.getProgressDrawable
import com.doubtnut.news.util.loadImage
import kotlinx.android.synthetic.main.item_news.view.*

class NewsListAdapter(val articlesList : ArrayList<Article>) : RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>(), NewsClickListener {

    class NewsViewHolder(var view : ItemNewsBinding) : RecyclerView.ViewHolder(view.root)

    fun updateNewsList(newArticleList : List<Article>) {
        articlesList.clear()
        articlesList.addAll(newArticleList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemNewsBinding>(inflater, R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount() = articlesList.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.view.article = articlesList[position]
        holder.view.listener = this
    }

    override fun onNewsClicked(v: View) {
        val uuid = v.articleId.text.toString().toInt()
        val action = ListFragmentDirections.actionListDetails()
        action.newsUuid = uuid
        Navigation.findNavController(v).navigate(action)
    }
}