package com.example.leaning_english.mysql

import android.util.Log
import cn.leancloud.LCObject
import com.example.leaning_english.database.DatabaseManager
import com.example.leaning_english.database.GsonInstance
import com.example.leaning_english.utils.DatabaseOperate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object DBHelper {
    private const val TAG = "Coroutine"
    private const val driver = "com.mysql.jdbc.Driver"
    //加入utf-8是为了后面往表中输入中文，表中不会出现乱码的情况
    private const val url = "jdbc:mysql://8.134.162.247:3306/word_battle?useSSL=false&serverTimezone=Asia/Shanghai"
    private const val user = "Harisy"//用户名
    private const val password = "123456"//密码
    /*
    * 连接数据库
    * */
    suspend fun getConn(): Connection? = withContext(Dispatchers.IO){
        var conn: Connection? = null;
        try {
            Class.forName(driver)
            conn = DriverManager.getConnection(url,user,password)//获取连接
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return@withContext conn
    }

    fun databaseHandle(words: List<LCObject>, bookID: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val conn = getConn()
            if (conn != null) {
                // 获取数据库连接成功
                Log.d(TAG, "数据库连接成功")
                insertWord(conn, words, bookID)
                conn.close()
            } else {
                // 获取数据库连接失败
                Log.d(TAG, "数据库连接失败")
            }
        }
    }

    fun insertWord(conn: Connection, wordList: List<LCObject>, bookID: Int){
        // 将本地room数据库中的word数据导入到mysql数据库中的word表中
        val wordBook = DatabaseManager.db.wordBookDao.getWordBooks(bookID)
        if (wordList.isNotEmpty()) {
            for (lcObject in wordList) {
                val word = DatabaseOperate.lCObjectTOWord(lcObject, bookID)
                val id = word.id
                val wor = word.word
                val translation = word.translation
                Log.d(TAG, "id: $id, word: $wor, meaning: $translation")
                try {
                    val sqlWord = "INSERT INTO words(id, word, usPhonetic, ukPhonetic, translation) VALUES($id, ${word.word}, ${word.usPhonetic}, ${word.ukPhonetic}, ${GsonInstance().INSTANCE?.gson?.toJson(translation)})"
                    val pstmt = conn.prepareStatement(sqlWord)
                    pstmt.executeUpdate()
                    Log.d(TAG, "word插入成功")
                    pstmt.close()
                } catch (e: Exception) {
                    //e.printStackTrace()
                }
                try {
                    val sqlWordBookConn = "INSERT INTO wordbook_words(wordbook_id, word_id) VALUES($bookID, $id)"
                    conn.prepareStatement(sqlWordBookConn).executeUpdate()
                    Log.d(TAG, "wordBookConnection插入成功")
                } catch (e: Exception) {
                    //e.printStackTrace()
                }
            }
        }
    }

    suspend fun queryWordID(bookID: Int, str: String): MutableList<Int> = withContext(Dispatchers.IO){
        val conn = getConn()
        val wordIDList = mutableListOf<Int>()
        if (conn != null) {
            // 获取数据库连接成功
            Log.d(TAG, "数据库连接成功")
            val sql = "SELECT * FROM wordbook_words WHERE wordbook_id=$bookID"
            val pstmt = conn.prepareStatement(sql)
            val rs = pstmt.executeQuery()
            while (rs.next()){
                val wordID = rs.getInt("word_id")
                wordIDList.add(wordID)
            }
            conn.close()
        } else {
            // 获取数据库连接失败
            Log.d(TAG, "数据库连接失败")
        }
        return@withContext wordIDList
    }

}