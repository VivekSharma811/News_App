package com.doubtnut.news.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doubtnut.news.R
import com.doubtnut.news.model.data.Article
import com.doubtnut.news.util.getProgressDrawable
import com.doubtnut.news.util.loadImage
import kotlinx.android.synthetic.main.item_news.view.*

class NewsListAdapter(val articlesList : ArrayList<Article>) : RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {

    class NewsViewHolder(var view : View) : RecyclerView.ViewHolder(view)

    fun updateNewsList(newArticleList : List<Article>) {
        articlesList.clear()
        articlesList.addAll(newArticleList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount() = articlesList.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.view.newsAuthor.text = articlesList[position].author
        holder.view.newsTitle.text = articlesList[position].title
        holder.view.newsPublishedAt.text = articlesList[position].publishedAt
        holder.view.newsImage.loadImage(articlesList[position].urlToImage, getProgressDrawable(holder.view.newsImage.context))
    }

}