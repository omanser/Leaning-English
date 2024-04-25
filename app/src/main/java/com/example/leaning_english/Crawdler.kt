//package com.example.leaning_english
//
//// 导入相关的库
//import android.util.Log
//import android.widget.Toast
//import cn.leancloud.LCObject
//import cn.leancloud.LCUser
//import com.google.gson.Gson
//import com.google.gson.annotations.SerializedName
//import io.reactivex.Observer
//import io.reactivex.disposables.Disposable
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import java.io.File
//import java.io.PrintWriter
//import com.google.gson.reflect.TypeToken
//import java.io.BufferedWriter
//import java.io.FileOutputStream
//import java.io.OutputStreamWriter
//import java.lang.Thread.sleep
//
//// 创建一个Word类，用于存储单词数据
//data class Word(
//    // 使用@SerializedName注解来指定每个属性在JSON数据中对应的键名
//    @SerializedName("id") val id: Int, // 单词编号
//    @SerializedName("word") val word: String, // 单词
//    @SerializedName("language") val language: String, // 语言
//    @SerializedName("explanations") val explanations: Explanations, // 释义
//    @SerializedName("synonyms") val synonyms: Array<String>?, // 同义词
//    @SerializedName("usPhonetic") val usPhonetic: String, // 美式音标
//    @SerializedName("ukPhonetic") val ukPhonetic: String, // 英式音标
//    @SerializedName("translations") val translations: Translations, // 翻译
//    @SerializedName("phrases") val phrases: Array<Phrase>, // 短语
//    @SerializedName("families") val families: Array<String>,
//    @SerializedName("memory") val memory: String,
//    @SerializedName("sentences") val sentences: Array<Sentence> // 例句
//
//
//)
//
//// 创建一个Explanations类，用于存储释义
//data class Explanations(
//    @SerializedName("meanings") val meanings: Meanings // 含义
//)
//
//// 创建一个Meanings类，用于存储含义
//data class Meanings(
//    @SerializedName("N") val n: Array<String>?, // 名词
//    @SerializedName("V") val v: Array<String>?,
//    @SerializedName("ADV") val adv: Array<String>?, // 副词
//    @SerializedName("ADJ") val adj: Array<String>?, // 形容词
//    @SerializedName("PREP") val prep: Array<String>?, // 介词
//    @SerializedName("ATTR") val attr: Array<String>?, // 缩略词
//    @SerializedName("PRON") val pron: Array<String>?, // 代词
//    @SerializedName("NUM") val num: Array<String>?, // 数词
//    @SerializedName("ART") val art: Array<String>?, //冠词
//    @SerializedName("CONj") val conj: Array<String>?, // 连词
//    @SerializedName("INT") val int: Array<String>?, //感叹词
//    @SerializedName("VT") val vt: Array<String>?, //及物动词
//    @SerializedName("VI") val vi:Array<String>?
//    // 如果有其他词性，可以继续添加
//)
//
//// 创建一个Translations类，用于存储翻译
//data class Translations(
//    @SerializedName("meanings") val meanings: Meanings // 含义
//)
//
//// 创建一个Phrase类，用于存储短语
//data class Phrase(
//    @SerializedName("content") val content: String, // 短语内容
//    @SerializedName("translation") val translation: String // 短语翻译
//)
//
//// 创建一个Sentence类，用于存储例句
//data class Sentence(
//    @SerializedName("content") val content: String, // 例句内容
//    @SerializedName("translation") val translation: String // 例句翻译
//)
//
//// 创建一个WordBook类，用于存储单词书数据
//data class WordBook(
//    // 使用@SerializedName注解来指定每个属性在JSON数据中对应的键名
//    @SerializedName("properties") val properties: Properties, // 单词书属性
//    @SerializedName("id") val id: String, // 单词书编号
//    @SerializedName("total") val total: Int, // 单词书总数
//    @SerializedName("name") val name: String, // 单词书名称
//    @SerializedName("type") val type: String, // 单词书类型
//    @SerializedName("words") val words: Array<Word> // 单词列表
//)
//
//// 创建一个Properties类，用于存储单词书属性
//data class Properties(
//    @SerializedName("random") val random: Boolean, // 是否随机
//    @SerializedName("description") val description: String, // 描述
//    @SerializedName("days") val days: Int, // 天数
//    @SerializedName("size") val size: Int, // 数量
//    @SerializedName("day") val day: Int // 日期
//)
//
//// 创建一个Crawler类，用于实现爬虫逻辑
//class Crawler {
//    // 创建一个OkHttpClient对象，用于发送网络请求
//    private val client = OkHttpClient()
//
//    // 创建一个Gson对象，用于转换JSON数据
//    private val gson = Gson()
//
//    // 定义一个方法，用于爬取指定的URL，并返回一个WordBook对象
//    fun crawl(url: String): String {
//        // 创建一个Request对象，设置请求的URL
//        val request = Request.Builder().url(url).build()
//        // 使用client对象发送请求，获取响应
//        val response = client.newCall(request).execute()
//        // 判断响应是否成功
//        if (response.isSuccessful) {
//            // 获取响应的JSON数据
//            val json = response.body.string()
//            // 使用gson对象将JSON数据转换为WordBook对象
//            //println(json)
//            // 返回WordBook对象
//            val jsonFile = gson.fromJson(json, WordBook::class.java)
//            val words = jsonFile.words
//            var wordsJson = Gson().toJson(words)
//            wordsJson = wordsJson.substring(1, wordsJson.length - 1) + ","
//            //println(wordsJson)
//            return wordsJson
//
//        } else {
//            // 抛出异常
//            throw Exception("请求失败：${response.code}")
//        }
//    }
//
//    // 定义一个方法，用于保存WordBook对象到指定的文件
//    fun save(wordBook: String, file: String) {
//        val outputStream = FileOutputStream(file, true) // 第二个参数为 true，表示追加模式
//        val writer = BufferedWriter(OutputStreamWriter(outputStream))
//
//        writer.write(wordBook)
//        writer.newLine()
//
//        writer.close()
//        outputStream.close()
////        // 创建一个File对象，设置文件的路径
////        val f = File(file)
////        // 创建一个PrintWriter对象，用于写入文件
////        val pw = PrintWriter(f)
////        // 写入单词书的名称，日期，数量
////        pw.println(wordBook)
////        // 关闭PrintWriter对象
////        pw.close()
//    }
//
//    fun addWord(wordBook: WordBook) {
//        for (word in wordBook.words){
//            val Tag = "myTag"
//            LCObject("Test").apply {
//                Log.e(Tag, "进来了")
//                put("id", word.id)
//                put("word", word.word)
//                put("usPhonetic", word.usPhonetic)
//                put("ukPhonetic", word.ukPhonetic)
//                put("translations", word.translations)
//                put("sentences", word.sentences)
//                //put("phrases", word.phrases)
//                saveInBackground().subscribe(object : Observer<LCObject> {
//                    override fun onSubscribe(d: Disposable) {
//                        Log.e(Tag, "来了")
//                    }
//
//                    override fun onError(e: Throwable) {
//                        Log.e(Tag, "${e.message}")
//                    }
//
//                    override fun onComplete() {
//                    }
//
//                    override fun onNext(t: LCObject) {
//                        Log.e(Tag, "添加成功")
//                    }
//
//                })
//            }
//        }
//    }
//}
//
//// 创建一个Main类，用于运行爬虫程序
//object Main {
//
//    // 定义一个主函数，用于启动程序
//    @JvmStatic
//    fun main(args: Array<String>) {
//        for (i in 121..239){
//            var day = "$i"
//            // 定义一个常量，表示要爬取的网站的URL
//            val URL = "https://wordforest.cn/api/book/en/zh/word/details?id=8&day=$day"
//
//            // 定义一个常量，表示要保存的文件的路径
////            val file = when(i){
////                in 0..9 -> 1
////                in 10..19 -> 2
////                in 20..29 -> 3
////                in 30..39 -> 4
////                in 40..49 -> 5
////                else -> 1
////            }
//            val FILE = "F://WordDatabase//wordbookGRE.txt"
//            // 创建一个Crawler对象，用于爬取数据
//            val crawler = Crawler()
//            // 调用crawler对象的crawl方法，传入URL，获取WordBook对象
//            val wordBook = crawler.crawl(URL)
//            // 打印爬取到的单词书的信息
//            //println("爬取成功：${wordBook.name}")
//            // 调用crawler对象的save方法，传入WordBook对象和FILE，将数据保存到文件
//            crawler.save(wordBook, FILE)
//
//            //调用addWord方法，将数据上传至leancloud
//            //crawler.addWord(wordBook)
//            // 打印保存成功的信息
//            //println("保存成功：$FILE")
//            sleep(200)
//            println("i == $i")
//        }
//    }
//}
