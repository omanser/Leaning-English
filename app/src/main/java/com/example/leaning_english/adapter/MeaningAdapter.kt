package com.example.leaning_english.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.leaning_english.R
import com.example.leaning_english.database.entity.Meanings
import com.example.leaning_english.database.entity.Word

class MeaningAdapter: RecyclerView.Adapter<MeaningAdapter.ViewHolder>() {
    val TAG = "MeaningAdapter"
    private var allMeanings = listOf<Meanings>()
    private lateinit var myContext: Context

    fun updateDataList(newList: List<Meanings>){
        allMeanings = newList
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var view: View = itemView
        var textType: TextView = itemView.findViewById(R.id.text_meaning_type)
        var textMeaning: TextView = itemView.findViewById(R.id.text_meaning_mean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellView = layoutInflater.inflate(R.layout.cell_card_meaning,parent, false)
        myContext = parent.context
        return ViewHolder(cellView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ob = allMeanings[position]
        val type = ob.wordType + ".\t"
        holder.textType.text = type
        holder.textMeaning.text = ob.toString()
        Log.d(TAG,"type = $type,   meaning = $ob")
    }

    override fun getItemCount(): Int {
        return allMeanings.size
    }
}