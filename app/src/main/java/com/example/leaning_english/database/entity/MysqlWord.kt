package com.example.leaning_english.database.entity

import androidx.room.TypeConverters
import com.example.leaning_english.database.ChatItemConverter

@TypeConverters(ChatItemConverter::class)
data class MysqlWord(
    var id: Int, // 单词ID
    var word: String, // 单词
    var usPhonetic: String, // 美式音标
    var ukPhonetic: String, // 英式音标
    val translation: Translation? //中文释义
)
