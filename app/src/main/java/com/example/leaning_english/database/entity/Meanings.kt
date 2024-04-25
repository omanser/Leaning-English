package com.example.leaning_english.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.leaning_english.database.ChatItemConverter

data class Meanings(
    val wordType: String,
    var meaning: ArrayList<String>
//    var n: ArrayList<String>?, // 名词
//    var adv: ArrayList<String>?, // 副词
//    var prep: ArrayList<String>?, // 介词
//    var v: ArrayList<String>?, // 动词
//    var adj: ArrayList<String>? // 形容词
) {
    override fun toString(): String {
        var str = ""
        for (i in meaning.indices){
            str += "${meaning[i]}; "
        }
        return str
    }
}
