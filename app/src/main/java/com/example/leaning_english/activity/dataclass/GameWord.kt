package com.example.leaning_english.activity.dataclass

import com.example.leaning_english.database.entity.Meanings

data class GameWord(
    var wordID: Int,
    var word: String,
    var usPhonetic: String,
    var ukPhonetic: String,
    var meanings: Meanings?,
    var isRight: Boolean = false
)
