package com.example.leaning_english.utils

import android.util.Log
import com.example.leaning_english.database.DatabaseManager
import com.example.leaning_english.database.entity.WordBook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object TempOperate {
    suspend fun insertWordBook(wordBook: WordBook) = withContext(Dispatchers.IO){
        try {
            DatabaseManager.db.wordBookDao.insertWordBooks(wordBook)
            Log.d("insertWordBook", "插入成功")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}