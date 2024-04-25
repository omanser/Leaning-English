package com.example.leaning_english.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.leaning_english.database.entity.WordBook
import kotlinx.coroutines.flow.Flow

@Dao
interface WordBookDao {
    @Insert
    fun insertWordBooks(vararg wordBook: WordBook)

    @Update
    fun updateWordBooks(vararg wordBook: WordBook)

    @Delete
    fun deleteWordBooks(vararg wordBook: WordBook)

    @Query("SELECT * FROM WordBook WHERE id = :id")
    fun getWordBooks(id: Int): WordBook?

    @Query("SELECT * FROM WordBook")
    fun getAllWordBooks(): List<WordBook>?

    @Query("DELETE FROM WordBook")
    fun deleteAllWordBooks()
}