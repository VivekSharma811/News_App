package com.doubtnut.news.model.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity(tableName = "article_table")
data class Article(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    @Embedded(prefix = "source_")
    val source: Source,
    val title: String?,
    val url: String?,
    val urlToImage: String?
) {
    @PrimaryKey(autoGenerate = true)
    var uuid : Int = 0
}