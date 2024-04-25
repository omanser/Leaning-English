package com.example.leaning_english.activity

import android.app.AlertDialog
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.leancloud.LCObject
import cn.leancloud.LCQuery
import com.example.leaning_english.R
import com.example.leaning_english.adapter.BookAdapter
import com.example.leaning_english.database.DatabaseManager
import com.example.leaning_english.database.entity.WordBook
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import io.reactivex.Observer as LCObserver

class ChooseWordBookActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bookAdapter: BookAdapter
    private lateinit var imgBack: ImageView
    private lateinit var textViewDeleteAllWords: TextView
    private val _dataListLive = MutableLiveData<List<LCObject>>()
    val dataListLive: MutableLiveData<List<LCObject>> = _dataListLive

    init {
        val query = LCQuery<LCObject>("WordBook")
        query.findInBackground().subscribe(object : LCObserver<List<LCObject>>{
            override fun onSubscribe(d: Disposable) {}

            override fun onError(e: Throwable) {
                Toast.makeText(application, "${e.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onComplete() {}

            override fun onNext(t: List<LCObject>) {
                _dataListLive.value = t
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_word_book)

        init()

        dataListLive.observe(this, Observer {
            bookAdapter.updateDataList(it)
            bookAdapter.notifyDataSetChanged()
        })

        imgBack.setOnClickListener {
            onBackPressed()
        }

        textViewDeleteAllWords.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("提示")
                .setMessage("是否删除所有单词")
                .setPositiveButton("确定") { dialog, which ->
                    DatabaseManager.db.wordDao.deleteAllWords()
                    DatabaseManager.db.wordBookDao.deleteAllWordBooks()
                    //updateWordBook()
                }
                .setNegativeButton("取消") { dialog, which ->
                    dialog.dismiss()
                }
        }
    }


    override fun onBackPressed() {
        supportFinishAfterTransition()
    }
    private fun init(){
        recyclerView = findViewById(R.id.recycler_word_book_list)
        imgBack = findViewById(R.id.img_book_back)
        textViewDeleteAllWords = findViewById(R.id.textView_deleteAllWords)
        bookAdapter = BookAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = bookAdapter
    }

    private fun updateWordBook(){
//        DatabaseManager.db.wordDao.getAllWordId()?.onEach {wordIds ->
//            val wordBook = WordBook(0, 0, "单词书", "", true, wordIds)
//            DatabaseManager.db.wordBookDao.updateWordBooks(wordBook)
//        }?.launchIn(this)
    }

}