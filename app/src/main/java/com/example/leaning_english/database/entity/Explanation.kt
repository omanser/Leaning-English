package com.example.leaning_english.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//  英文释义
data class Explanation(
    var meanings: ArrayList<Meanings>? // 含义
)
