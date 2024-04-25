package com.example.leaning_english.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.leaning_english.database.ChatItemConverter

@Entity
@TypeConverters(ChatItemConverter::class)
data class HistorySearch(
    @PrimaryKey
    var wordId: Int,

    var word: String,

    var meanings: Meanings?
)
