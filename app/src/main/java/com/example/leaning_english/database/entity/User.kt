package com.example.leaning_english.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.leaning_english.ConstantData
import com.example.leaning_english.database.ChatItemConverter

@Entity
@TypeConverters(ChatItemConverter::class)
data class User(
    @PrimaryKey
    var id: String,
    var userName: String,
    var userPortrait: String, // 头像链接
    var currentBookId: Int = -1,
    // 挑战分数
    var maxScores: MutableMap<String, Int> = mutableMapOf(),
    // 单词发音
    var habitPhonetic: Int = ConstantData.UsPHONETIC
)
