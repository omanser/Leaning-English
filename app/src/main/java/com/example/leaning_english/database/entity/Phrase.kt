package com.example.leaning_english.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Phrase(
    var content: String?, // 短语内容
    var translation: String? // 短语翻译
)
