package com.example.leaning_english.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//  中文释义
data class Translation(
    var meanings: ArrayList<Meanings>? // 含义
)
