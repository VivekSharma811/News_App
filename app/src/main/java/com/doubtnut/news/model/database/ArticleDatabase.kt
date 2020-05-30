package com.doubtnut.news.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.doubtnut.news.model.dao.ArticleDao
import com.doubtnut.news.model.data.Article

@Database(
    entities = arrayOf(Article::class),
    version = 1,
    exportSchema = false
)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun articleDao() : ArticleDao

    companion object {
        @Volatile private var instance : ArticleDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ArticleDatabase::class.java,
            "articledatabase"
        ).build()
    }

}