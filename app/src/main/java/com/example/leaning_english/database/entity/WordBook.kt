package com.example.leaning_english.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.leaning_english.database.ChatItemConverter

@Entity
@TypeConverters(ChatItemConverter::class)
data class WordBook(
    @PrimaryKey
    var id: Int, // 单词书编号
    var total: Int, // 单词书总数
    var name: String, // 单词书名称
    var bookImg: String?,
    var isLoad: Boolean, // 是否已经下载
    var words: ArrayList<Int>?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WordBook

        if (id != other.id) return false
        if (total != other.total) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + total
        result = 31 * result + name.hashCode()
        return result
    }

}
