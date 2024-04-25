//package com.example.leaning_english.adapter
//
//import android.content.Context
//import android.content.Intent
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import android.widget.Toast
//import androidx.recyclerview.widget.RecyclerView
//import cn.leancloud.LCObject
//import cn.leancloud.LCUser
//import coil.load
//import com.example.leaning_english.R
//import com.example.leaning_english.activity.MainActivity
//import com.example.leaning_english.database.DatabaseManager
//import com.example.leaning_english.database.entity.WordBook
//import com.example.leaning_english.utils.DatabaseOperate
//import com.example.leaning_english.utils.TempOperate
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//class BookBattleAdapter: RecyclerView.Adapter<BookBattleAdapter.ViewHolder>()  {
//    val TAG = "BookBattleAdapter"
//    private lateinit var myContext: Context;
//    private var allWordBooks = listOf<WordBook>()
//
//    fun updateDataList(newList: List<WordBook>){
//        allWordBooks = newList
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
//        var view: View = itemView
//        var textViewBookName: TextView = itemView.findViewById(R.id.item_book_name)
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
//        val cellView = layoutInflater.inflate(R.layout.cell_book_choose, parent, false)
//        myContext = parent.context
//        return ViewHolder(cellView)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val ob = allWordBooks[position]
//        val wordSum = "${ob.get("total")}词"
//        holder.textViewBookName.text = ob.get("name").toString()
//        holder.view.setOnClickListener {
//
//
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return allWordBooks.size
//    }
//}