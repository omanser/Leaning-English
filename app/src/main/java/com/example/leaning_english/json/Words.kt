package com.example.leaning_english.json

import androidx.room.TypeConverters
import com.example.leaning_english.database.ChatItemConverter
import com.example.leaning_english.database.entity.Translation

@TypeConverters(ChatItemConverter::class)
data class Words(
    var id: Int,
    var word: String,
    var usPhonetic: String, // 美式音标
    var ukPhonetic: String, // 英式音标
    val translation: Translation // 中文释义
)
