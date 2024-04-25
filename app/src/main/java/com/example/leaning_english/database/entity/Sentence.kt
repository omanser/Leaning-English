package com.example.leaning_english.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Sentence(
    var content: String?, // 中文例句
    var translation: String? // 例句翻译
)
