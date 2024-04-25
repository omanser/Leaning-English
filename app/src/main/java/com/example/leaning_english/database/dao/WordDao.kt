package com.example.leaning_english.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.leaning_english.database.entity.Word
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Insert
    fun insertWords(vararg word: Word)

    @Update
    fun updateWords(vararg word: Word)

    @Delete
    fun deleteWords(vararg word: Word)

    @Query("SELECT * FROM WORD WHERE id = :id")
    fun getWords(id: Int): Word?

    @Query("SELECT * FROM WORD WHERE word = :word")
    fun getWordsByWord(word: String): Word?

    @Query("SELECT * FROM Word Where belongCET4 = 1")
    fun getWordsBelongCET4(): List<Word>?

    @Query("SELECT * FROM Word Where belongTest = 1")
    fun getWordsBelongTest(): List<Word>

    @Query("SELECT * FROM WORD ORDER BY id Desc")
    fun getAllWordsLive(): List<Word>?

    @Query("DELETE FROM WORD")
    fun deleteAllWords()

    @Query("SELECT * FROM WORD WHERE word LIKE '%' || :searchWord || '%' limit 15")
    fun queryWord(searchWord: String): List<Word>?

    @Query("SELECT id FROM WORD")
    fun getAllWordId(): List<Int>?
}