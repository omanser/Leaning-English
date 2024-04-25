package com.example.leaning_english.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import cn.leancloud.LCObject
import cn.leancloud.LCUser
import coil.load
import com.example.leaning_english.ConstantData
import com.example.leaning_english.MyApplication
import com.example.leaning_english.R
import com.example.leaning_english.activity.MainActivity
import com.example.leaning_english.activity.SignUpActivity
import com.example.leaning_english.activity.ui.word.HomeFragment
import com.example.leaning_english.database.DatabaseManager
import com.example.leaning_english.database.entity.User
import com.example.leaning_english.database.entity.WordBook
import com.example.leaning_english.utils.DatabaseOperate
import com.example.leaning_english.utils.TempOperate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class BookAdapter: RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    val TAG = "BookAdapter"
    private lateinit var myContext: Context;
    private var allWordBooks = listOf<LCObject>()

    fun updateDataList(newList: List<LCObject>){
        allWordBooks = newList
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var view: View = itemView
        var textViewBookName: TextView = itemView.findViewById(R.id.item_text_book_name)
        var textViewBookWordNum: TextView = itemView.findViewById(R.id.item_text_book_word_num)
        var bookImg: ImageView = itemView.findViewById(R.id.item_img_book)
        var wordsSource: TextView = itemView.findViewById(R.id.item_text_book_source)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val cellView = layoutInflater.inflate(R.layout.cell_word_book, parent, false)
        myContext = parent.context
        return ViewHolder(cellView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ob = allWordBooks[position]
        holder.bookImg.load(ob.get("bookImg").toString())
        val wordSum = "${ob.get("total")}词"
        holder.textViewBookWordNum.text = wordSum
        holder.textViewBookName.text = ob.get("name").toString()
        holder.wordsSource.text = "数据来源：单词森林"
        holder.view.setOnClickListener {
            Log.d(TAG, "position: $position")
            val itemWordBook = allWordBooks[position]
            val bookId = itemWordBook.getNumber("id").toInt()
            val total = itemWordBook.getNumber("total").toInt()
            val bookName = itemWordBook.get("name").toString()
            val bookImg = itemWordBook.get("bookImg").toString()
            val book = WordBook(bookId, total, bookName, bookImg, false, arrayListOf())
            CoroutineScope(Dispatchers.IO).launch {
                TempOperate.insertWordBook(book)
            }
            DatabaseManager.db.wordBookDao.getWordBooks(bookId)?.apply {
                words = arrayListOf()
                DatabaseManager.db.wordBookDao.updateWordBooks(this)
            }
            val user = DatabaseManager.db.userDao.getUser(LCUser.currentUser().objectId)
            val bookDao = DatabaseManager.db.wordBookDao
            user?.apply {
                val book = bookDao.getWordBooks(bookId)
                book!!.isLoad = false
                if(this.currentBookId == bookId && book.isLoad){
                    Toast.makeText(myContext, "当前正在学此书", Toast.LENGTH_SHORT).show()
                } else {
                    //if (bookId == 0){
                    this.currentBookId = bookId
                    Log.d(TAG, "有这个book")
                    if(book.isLoad){
                        Log.d(TAG, "下了这本书")
                    } else{
                        Log.d(TAG, "没下这本书")
                        DatabaseOperate.pullWordData(bookId)
                        bookDao.updateWordBooks(book.copy(isLoad = true))
                    }
                    DatabaseManager.db.userDao.updateUsers(this)
                    val intent = Intent(myContext, MainActivity::class.java)
                    myContext.startActivity(intent)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return allWordBooks.size
    }

}