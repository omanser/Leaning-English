package com.example.leaning_english.utils

import android.provider.ContactsContract.Data
import android.util.Log
import cn.leancloud.LCObject
import cn.leancloud.LCQuery
import com.example.leaning_english.ConstantData
import com.example.leaning_english.R
import com.example.leaning_english.database.DatabaseManager
import com.example.leaning_english.database.entity.Explanation
import com.example.leaning_english.database.entity.Meanings
import com.example.leaning_english.database.entity.Phrase
import com.example.leaning_english.database.entity.Sentence
import com.example.leaning_english.database.entity.Translation
import com.example.leaning_english.database.entity.User
import com.example.leaning_english.database.entity.Word
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import androidx.lifecycle.lifecycleScope
import cn.leancloud.LCFile
import cn.leancloud.LCUser
import com.example.leaning_english.database.entity.HistorySearch
import com.example.leaning_english.database.entity.WordBook
import com.example.leaning_english.mysql.DBHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Locale

object DatabaseOperate {

    val TAG = "DatabaseOperate"

    fun lCObjectTOWord(word: LCObject, bookID: Int): Word{
        val id = word.getInt("id")
        val word1 = word.getString("word")
        //Log.d("word", "id = $id: word = $word1")
        val meaningsEn = word.getJSONObject("explanations")
            .getJSONObject("meanings")
        val meanings = word.getJSONObject("translations")
            .getJSONObject("meanings")
//        val vEN = meaningsEn.getJSONArray("V")?.run {
//            Log.d(TAG, "vEN: ${this.size}")
//            jsonArrayToArrayString(this)
//        }
//        val nEN = meaningsEn.getJSONArray("N")?.run {
//            Log.d(TAG, "nEN: ${this.size}")
//            jsonArrayToArrayString(this)
//        }
//        val adjEN = meaningsEn.getJSONArray("ADJ")?.run {
//            Log.d(TAG, "adjEN: ${this.size}")
//            jsonArrayToArrayString(this)
//        }
//        val advEN = meaningsEn.getJSONArray("ADV")?.run {
//            Log.d(TAG, "advEN: ${this.size}")
//            jsonArrayToArrayString(this)
//        }
//        val prepEN = meaningsEn.getJSONArray("PREP")?.run {
//            Log.d(TAG, "prepEN: ${this.size}")
//            jsonArrayToArrayString(this)
//        }
//        val v = meanings.getJSONArray("V")?.run {
//            Log.d(TAG, "v: ${this.size}")
//            jsonArrayToArrayString(this)
//        }
//        val n = meanings.getJSONArray("N")?.run {
//            Log.d(TAG, "n: ${this.size}")
//            jsonArrayToArrayString(this)
//        }
//        val adj = meanings.getJSONArray("ADJ")?.run {
//            Log.d(TAG, "agj: ${this.size}")
//            jsonArrayToArrayString(this)
//        }
//        val adv = meanings.getJSONArray("ADV")?.run {
//            Log.d("TAG", "adv: ${this.size}")
//            jsonArrayToArrayString(this)
//        }
//        val prep = meanings.getJSONArray("PREP")?.run {
//            Log.d("TAG", "prep: ${this.size}")
//            jsonArrayToArrayString(this)
//        }
        val meaningList = arrayListOf<Meanings>()
        val meaningEnList = arrayListOf<Meanings>()
        for (wType in ConstantData.wordTypeList){
            meanings.getJSONArray(wType.uppercase(Locale.ROOT))?.let {
                //Log.d("TAG", "$wType: ${it.size}")
                meaningList.add(Meanings(wType, jsonArrayToArrayString(it)))
            }
            meaningsEn.getJSONArray(wType.uppercase(Locale.ROOT))?.let {
                //Log.d("TAG", "$wType: ${it.size}")
                meaningEnList.add(Meanings(wType, jsonArrayToArrayString(it)))
            }
        }
        val memory: String? = word.getString("memory")
        val families = word.getJSONArray("families")?.run {
            jsonArrayToArrayString(this)
        }

        val phrases = word.getJSONArray("phrases")?.run {
            //Log.d("TAG", "phrases: ${this.size}")

            val arrayList = ArrayList<Phrase>(this.size)
            for(i in this.indices){
                arrayList.add(
                    Phrase(
                        content = this.getJSONObject(i).getString("content"),
                        translation = this.getJSONObject(i).getString("translation")
                    )
                )
            }
            arrayList
        }
        val sentences = word.getJSONArray("sentences")?.run {
            val arrayList = ArrayList<Sentence>(this.size)
            for (i in this.indices){
                arrayList.add(
                    Sentence(
                        content = this.getJSONObject(i).getString("content"),
                        translation = this.getJSONObject(i).getString("translation")
                    )
                )
            }
            arrayList
        }
        val ukPhonetic = word.getString("ukPhonetic")?:""
        val usPhonetic = word.getString("usPhonetic")?:""
        val translation: Translation = Translation(arrayListOf())
        val explanation: Explanation = Explanation(arrayListOf())
        translation.meanings = meaningList
        explanation.meanings = meaningEnList
        val wordIn = Word(id, word1, usPhonetic, ukPhonetic, families, memory, explanation = explanation,
            translation = translation, phrases = phrases, sentence = sentences).apply {
            when(bookID){
                ConstantData.BOOKCET4 -> this.belongCET4 = true
                ConstantData.BOOKCET6 -> this.belongCET6 = true
                ConstantData.BOOKKAOY -> this.belongKaoYan = true
                ConstantData.BOOKTEST -> this.belongTest = true
                ConstantData.BOOKGRE -> this.belongGRE = true
                ConstantData.BOOKCHUZHONG -> this.belongChuZhong = true
                ConstantData.BOOKGAOZHONG -> this.belongGaoZhong = true
                ConstantData.BOOKIELTS -> this.belongIELTS = true
                ConstantData.BOOKLEVEL8 -> this.belongLevel8 = true
            }
        }
        return wordIn
    }

    fun saveWord(words: List<LCObject>, bookID: Int){
        CoroutineScope(Dispatchers.IO).launch {
            //Log.d(TAG, "saveWord: Leancloud 数据量：${words.size}---------------------------------------------------------------------")
            for (word in words) {
                val wordIn = lCObjectTOWord(word, bookID)
                //Log.d(TAG, wordIn.toString())
                insertWordNotCoroutineScope(wordIn)
                //updateWordList(wordIn, bookID)
            }
        }
    }

    fun insertWordBook(){

    }

    @OptIn(DelicateCoroutinesApi::class)
    fun saveUserData(userId: String, portraitUrl: String){
        GlobalScope.launch {
            try {
                val userDao = DatabaseManager.db.userDao
                if(userDao.getUser(userId) == null){
                    userDao.insertUsers(
                        User(
                            userId,
                            "defaultName",
                            portraitUrl,
                            maxScores = mutableMapOf()
                        )
                    )
                    Log.d(TAG, "插入一条User数据")
                }
            } catch (exception: Exception){
                println("user save error = ${exception.message}")
                exception.printStackTrace()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun updateWordIsWrongWord(word: String, isWrongWord: Boolean){
        GlobalScope.launch {
            try {
                val wordDao = DatabaseManager.db.wordDao
                val wordDb = wordDao.getWordsByWord(word)
                wordDb?.let {
                    it.isWrongWord = isWrongWord
                    wordDao.updateWords(it)
                    Log.d(TAG, "更新一条Word数据isWrongWord，$it")
                }
            } catch (exception: Exception){
                println("user save error = ${exception.message}")
                exception.printStackTrace()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun updateWordIsNewWord(word: String, isNewWord: Boolean){
        GlobalScope.launch {
            try {
                val wordDao = DatabaseManager.db.wordDao
                val wordDb = wordDao.getWordsByWord(word)
                wordDb?.let {
                    it.isWrongWord = isNewWord
                    wordDao.updateWords(it)
                    Log.d(TAG, "更新一条Word数据isNewWord，$it")
                }
            } catch (exception: Exception){
                println("user save error = ${exception.message}")
                exception.printStackTrace()
            }
        }
    }

//    @OptIn(DelicateCoroutinesApi::class)
//    fun insertPhrase(phrases: List<Phrase>?){
//        GlobalScope.launch {
//            try {
//                if (phrases != null) {
//                    for(i in phrases){
//                        DatabaseManager.db.phraseDao.insertPhrases(i)
//                    }
//                }
//            } catch (exception: Exception) {
//                println("Phrase insert error = ${exception.message}")
//                exception.printStackTrace()
//            }
//        }
//    }
//
//    @OptIn(DelicateCoroutinesApi::class)
//    fun insertMeaning(meanings: Meanings?){
//        GlobalScope.launch {
//            try {
//                if (meanings != null) {
//                    DatabaseManager.db.meaningDao.insertMeanings(meanings)
//                }
//            } catch (exception: Exception) {
//                println("Meaning insert error = ${exception.message}")
//                exception.printStackTrace()
//            }
//        }
//    }
//
//    @OptIn(DelicateCoroutinesApi::class)
//    fun insertSentences(sentences: List<Sentence>?){
//        GlobalScope.launch {
//            try {
//                if (sentences != null) {
//                    for(i in sentences){
//                        DatabaseManager.db.sentenceDao.insertSentences(i)
//                    }
//                }
//            } catch (exception: Exception) {
//                println("Sentences insert error = ${exception.message}")
//                exception.printStackTrace()
//            }
//        }
//    }

    fun saveHistorySearch(wordClick: Word){
        val historyWord = HistorySearch(
            wordId = wordClick.id,
            word = wordClick.word,
            meanings = wordClick.translation?.meanings?.first()
        )
        try {
            DatabaseManager.db.historySearchDao.insertHistorySearches(historyWord)
        } catch (exception: Exception){
            println("HistorySearch insert error = ${exception.message}")
            exception.printStackTrace()
        }
    }

    fun insertWord(word: Word?){
        CoroutineScope(Dispatchers.IO).launch {
            try{
                if(word!=null){
                    val wordDao = DatabaseManager.db.wordDao
                    val wordDb = wordDao.getWords(word.id)

                    if (wordDb != null) {
                        Log.d(TAG, "insertWord: wordDb不为空 $wordDb")
                        DatabaseManager.db.wordDao.updateWords(updateWordBelong(word, wordDb))
                    } else {
                        Log.d(TAG, "insertWord: wordDb为空 $word")
                        DatabaseManager.db.wordDao.insertWords(word)
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }

    fun insertWordNotCoroutineScope(word: Word?){
        try{
            if(word!=null){
                val wordDao = DatabaseManager.db.wordDao
                val wordDb = wordDao.getWords(word.id)
                if (wordDb != null) {
                    Log.d(TAG, "insertWord: wordDb不为空 $wordDb")
                    DatabaseManager.db.wordDao.updateWords(updateWordBelong(word, wordDb))
                } else {
                    Log.d(TAG, "insertWord: wordDb为空 $word")
                    DatabaseManager.db.wordDao.insertWords(word)
                }
            }
        } catch (exception: Exception) {
            println("Word insert error = ${exception.message}")
            //exception.printStackTrace()
        }
    }

    fun updateWordList(word: Word, bookID: Int){
        try {
            val book = DatabaseManager.db.wordBookDao.getWordBooks(bookID)
            if (book != null) {
                if (book.words == null) {
                    book.words = arrayListOf()
                }
                if (book.words?.contains(word.id) == false) book.words?.add(word.id)
                book.total = book.words?.size!!
                DatabaseManager.db.wordBookDao.updateWordBooks(book)
                Log.d(TAG, "wordID = ${word.id}, insertWord: 更新book $book")
            }
        } catch (exception: Exception) {
            println("Word insert error = ${exception.message}")
            exception.printStackTrace()
        }
    }

    private fun updateWordBelong(word: Word, wordDb: Word): Word{
        if(word.belongCET4) wordDb.belongCET4 = true
        if(word.belongCET6) wordDb.belongCET6 = true
        if(word.belongKaoYan) wordDb.belongKaoYan = true
        if(word.belongTest) wordDb.belongTest = true
        if(word.belongGRE) wordDb.belongGRE = true
        if(word.belongChuZhong) wordDb.belongChuZhong = true
        if(word.belongGaoZhong) wordDb.belongGaoZhong = true
        if(word.belongIELTS) wordDb.belongIELTS = true
        if(word.belongLevel8) wordDb.belongLevel8 = true
        return wordDb
    }

    fun deleteAllData(){

    }

    fun pullWordData(bookID: Int){
        val query = LCQuery<LCObject>(ConstantData.getWordBookString(bookID))
        for (i in 0 until 0){
            query.limit(500)
            query.skip(i * 500)
            runBlocking{
                query.findInBackground().subscribe(object : Observer<List<LCObject>> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onError(e: Throwable) {
                        Log.e(TAG, "${e.message}")
                    }

                    override fun onComplete() {}

                    override fun onNext(t: List<LCObject>) {
                        saveWord(t, bookID)
                        DBHelper.databaseHandle(t, bookID)
                    }

                })
            }
        }
    }

    private fun jsonArrayToArrayString(jArray: cn.leancloud.json.JSONArray): ArrayList<String>{
        val arrayString = ArrayList<String>(jArray.size)
        for (i in 0 until jArray.size){
            //Log.d("TAG", "总长：${jArray.size.toString()}")
            arrayString.add(jArray.getString(i))
        }
        return arrayString
    }

    fun arrayListToString(array: ArrayList<String>?): String{
        var str: String = ""
        if (array != null) {
            for (i in array.indices - 1){
                str += array[i] + ","
            }
        }
        str+= array?.last() ?: ""
        return str
    }

}