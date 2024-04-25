//package com.example.leaning_english.mysql
//
//import android.util.Log
//import com.example.leaning_english.database.entity.User
//import com.example.leaning_english.database.entity.Word
//
//object MySqlOperate {
//    const val TAG = "MySqlOperate"
//    fun getUser(userName: String, password: String): User? {
//        val conn = DBOpenHelper.getConn()
//        if (conn != null) {
//            // 获取数据库连接成功
//            Log.d(TAG, "数据库连接成功")
//            val statement = conn.createStatement()
//            val resultSet = statement.execute("SELECT * FROM USER WHERE USERNAME = '$userName' AND PASSWORD = '$password'")
//        }
//
//    fun updateWord(word: Word) {
//        val conn = DBOpenHelper.getConn()
//        if (conn != null) {
//            // 获取数据库连接成功
//            Log.d(TAG, "数据库连接成功")
//            val statement = conn.createStatement()
//            //val resultSet = statement.execute()
//        }
//    }
//}