package com.example.leaning_english.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.leaning_english.ConstantData
import com.example.leaning_english.R
import com.example.leaning_english.activity.WordCardActivity
import com.example.leaning_english.database.DatabaseManager
import com.example.leaning_english.database.entity.HistorySearch
import com.example.leaning_english.utils.DatabaseOperate

class HistoryWordAdapter: RecyclerView.Adapter<HistoryWordAdapter.ViewHolder>() {
    val TAG = "HistoryWordAdapter"
    private var allHistoryWord = listOf<HistorySearch>()
    private lateinit var myContext: Context

    fun updateDataListHistory(newList: List<HistorySearch>){
        allHistoryWord = newList
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
        return allHistoryWord.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ob = allHistoryWord[position]
        holder.textWord.text = ob.word
        val meanings = ob.meanings

        meanings?.let {
            holder.textMeanings.text = meanings.toString()
        }
        holder.view.setOnClickListener {
            Log.d(TAG, "position: $position")
            val itemWord = allHistoryWord[position]
            WordCardActivity.wordId = ob.wordId
            WordCardActivity.currentWord = DatabaseManager.db.wordDao.getWords(ob.wordId)!!
            val intent = Intent(myContext, WordCardActivity::class.java)
            myContext.startActivity(intent)
        }
    }
}