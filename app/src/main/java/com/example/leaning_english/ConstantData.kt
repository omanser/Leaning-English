package com.example.leaning_english

import com.example.leaning_english.activity.ui.game.FinishActivity

object ConstantData {
    const val BOOKTEST = 0
    const val BOOKCET4 = 1
    const val BOOKCET6 = 2
    const val BOOKKAOY = 3
    const val BOOKIELTS = 4
    const val BOOKLEVEL8 = 5
    const val BOOKGAOZHONG = 6
    const val BOOKCHUZHONG = 7
    const val BOOKGRE = 8

    const val UsPHONETIC = 0
    const val UkPHONETIC = 1

    const val SearchActivity = 0
    const val FinishActivity = 1

    val wordTypeList = listOf(
        "n", "v", "adv", "adj", "prep", "attr", "pron", "num", "art", "conj", "int", "vt", "vi"
    )

    const val SERVER_HOST = "172.17.157.73:8080"
    const val SERVER_URL = "ws://$SERVER_HOST/api/websocket"

    fun getWordBookString(bookId: Int): String{
        return when(bookId){
            BOOKTEST -> "WordTest"
            BOOKCET4 -> "BookCET4"
            BOOKCET6 -> "BookCET6"
            BOOKKAOY -> "BookKaoYan"
            BOOKIELTS -> "BookIELTS"
            BOOKLEVEL8 -> "BookLevel8"
            BOOKGAOZHONG -> "BookGaoZhong"
            BOOKCHUZHONG -> "BookChuZhong"
            BOOKGRE -> "BookGRE"
            else -> "None"
        }
    }
}