package com.example.leaning_english.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.leaning_english.database.entity.HistorySearch

@Dao
interface HistorySearchDao {
    @Insert
    fun insertHistorySearches(vararg historySearch: HistorySearch)

    @Update
    fun updateHistorySearches(vararg historySearch: HistorySearch)

    @Delete
    fun deleteHistorySearches(vararg historySearch: HistorySearch)

    @Query("SELECT * FROM HistorySearch WHERE wordId = :id")
    fun getHistorySearch(id: Int): HistorySearch?

    @Query("SELECT * FROM HistorySearch")
    fun getAllHistoryWords(): List<HistorySearch>?

    @Query("DELETE FROM HistorySearch")
    fun deleteAllHistoryWords()
}