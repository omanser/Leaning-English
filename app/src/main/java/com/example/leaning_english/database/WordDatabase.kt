package com.example.leaning_english.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.leaning_english.database.dao.HistorySearchDao
import com.example.leaning_english.database.dao.UserDao
import com.example.leaning_english.database.dao.WordBookDao
import com.example.leaning_english.database.dao.WordDao
import com.example.leaning_english.database.entity.HistorySearch
import com.example.leaning_english.database.entity.Meanings
import com.example.leaning_english.database.entity.Phrase
import com.example.leaning_english.database.entity.Sentence
import com.example.leaning_english.database.entity.User
import com.example.leaning_english.database.entity.Word
import com.example.leaning_english.database.entity.WordBook

@Database(
    entities = [User::class, Word::class, WordBook::class, HistorySearch::class],
    version = 1,
    exportSchema = false)
abstract class WordDatabase: RoomDatabase() {
//    @JvmField
//    var INSTANCE: WordDatabase? = null
//
//    @JvmStatic
//    fun getDatabase(): WordDatabase {
//        if (INSTANCE == null) {
//            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), WordDatabase::class.java, "word_database")
//                .allowMainThreadQueries()
//                .build()
//        }
//        return INSTANCE
//    }

    var getData: WordDatabase? = null

    val wordDao: WordDao by lazy { createWordDao() }
    abstract fun createWordDao(): WordDao

    val wordBookDao: WordBookDao by lazy { createWordBookDao() }
    abstract fun createWordBookDao() : WordBookDao

    val userDao: UserDao by lazy { userDao() }
    abstract fun userDao() : UserDao

    val historySearchDao: HistorySearchDao by lazy { historySearchDao() }
    abstract fun historySearchDao(): HistorySearchDao

//    companion object {
//        @Volatile
//        private var Instance: WordDatabase? = null
//
//        fun getDatabase(context: Context) : WordDatabase {
//
//            return Instance ?: synchronized(this){
//                Room.databaseBuilder(context, WordDatabase::class.java, "WordData.db")
//                    .build()
//                    .also { Instance = it }
//            }
//        }
//    }

}