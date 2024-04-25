package com.example.leaning_english.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.leancloud.LCObject
import com.example.leaning_english.ConstantData
import com.example.leaning_english.R
import com.example.leaning_english.activity.SearchActivity
import com.example.leaning_english.activity.WordCardActivity
import com.example.leaning_english.database.DatabaseManager
import com.example.leaning_english.database.entity.HistorySearch
import com.example.leaning_english.database.entity.Word
import com.example.leaning_english.utils.DatabaseOperate
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchAdapter: RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    val TAG = "SearchAdapter"
    private var allWords = listOf<Word>()
    private var allLCObject = listOf<LCObject>()
    private lateinit var myContext: Context

    fun updateDataList(newList: List<Word>){
        allWords = newList
    }

    fun updateDataListLC(newList: List<LCObject>){
        allLCObject = newList
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var view: View = itemView
        var textWord: TextView = itemView.findViewById(R.id.text_search_word)
        var textMeanings: TextView = itemView.findViewById(R.id.text_means_search)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val cellView = layoutInflater.inflate(R.layout.cell_word_search, parent, false)
        myContext = parent.context
        return ViewHolder(cellView)
    }

    override fun getItemCount(): Int {
        return allLCObject.size
    }

//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val ob = allWords[position]
//        holder.textWord.text = ob.word
//        val meanings = ob.translation?.meanings
//
//        if (meanings != null) {
//            holder.textMeanings.text = meanings.first().toString()
//        }
//        holder.view.setOnClickListener {
//            Log.d(TAG, "position: $position")
//            val itemWord = allWords[position]
//            WordCardActivity.wordId = itemWord.id
//            WordCardActivity.currentWord = itemWord
//            val intent = Intent(myContext, WordCardActivity::class.java)
//            myContext.startActivity(intent)
//        }
//    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ob = allLCObject[position]
        val word = DatabaseOperate.lCObjectTOWord(ob, 0)
        holder.textWord.text = word.word
        val meanings = word.translation?.meanings

        if (meanings != null) {
            if (meanings.isNotEmpty()) {
                holder.textMeanings.text = meanings.first().toString()
            }
        }
        holder.view.setOnClickListener {
            Log.d(TAG, "position: $position")
            val itemWord = allLCObject[position]
            val wordClick = DatabaseOperate.lCObjectTOWord(itemWord, ConstantData.BOOKLEVEL8)
            DatabaseOperate.saveHistorySearch(wordClick)
            SearchActivity.dataListHistoryLive.value =
                DatabaseManager.db.historySearchDao.getAllHistoryWords()?.reversed()
            WordCardActivity.wordId = wordClick.id
            WordCardActivity.currentWord = wordClick
            val intent = Intent(myContext, WordCardActivity::class.java)
            myContext.startActivity(intent)
        }
    }
}