package com.doubtnut.news.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.doubtnut.news.model.data.Article

@Dao
interface ArticleDao {

    @Insert
    fun insertAll(vararg articles : Article)

    @Query("SELECT * FROM article_table")
    fun getAllArticles() : LiveData<List<Article>>

    @Query("SELECT * FROM article_table WHERE uuid= :articleId")
    fun getArticle(articleId : Int) : LiveData<Article>

    @Query("DELETE FROM article_table")
    fun deleteAll()

}