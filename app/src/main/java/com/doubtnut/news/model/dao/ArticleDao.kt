package com.doubtnut.news.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.doubtnut.news.model.data.Article

@Dao
interface ArticleDao {

    @Insert
    suspend fun insertAll(vararg articles : Article) : List<Long>

    @Query("SELECT * FROM article_table")
    suspend fun getAllArticles() : List<Article>

    @Query("SELECT * FROM article_table WHERE uuid= :articleId")
    suspend fun getArticle(articleId : Int) : Article

    @Query("DELETE FROM article_table")
    suspend fun deleteAll()

}