package com.example.leaning_english.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.leaning_english.database.ChatItemConverter

@Entity
@TypeConverters(ChatItemConverter::class)
data class Word(
    @PrimaryKey
    var id: Int, // 单词ID
    var word: String, // 单词
    var usPhonetic: String, // 美式音标
    var ukPhonetic: String, // 英式音标
    var families: ArrayList<String>?,
    var memory: String?,
    var belongTest: Boolean = false,
    var belongCET4:Boolean = false,
    var belongCET6:Boolean = false,
    var belongKaoYan: Boolean = false,
    var belongChuZhong: Boolean = false,
    var belongGaoZhong: Boolean = false,
    var belongGRE: Boolean = false,
    var belongLevel8: Boolean = false,
    var belongIELTS: Boolean = false,
    val explanation: Explanation?, // 英文释义
    val translation: Translation?, //中文释义
    val phrases: ArrayList<Phrase>?,
    val sentence: ArrayList<Sentence>?,

    // 是否错词
    var isWrongWord: Boolean = false,

    // 是否是生词
    var isNewWord: Boolean = false,

    // 是否是简单词
    var isEasy: Boolean = false,

    // 检测次数
    var testNum: Int = 0,

    // 正确次数
    var testRightNum: Int = 0,

    // 是否已学习
    var isLearned: Boolean = false,
) {

}
