package com.example.leaning_english.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.leaning_english.R
import com.example.leaning_english.database.entity.Meanings
import com.example.leaning_english.database.entity.Phrase

class PhrasesAdapter: RecyclerView.Adapter<PhrasesAdapter.ViewHolder>() {
    val TAG = "PhrasesAdapter"
    private var allPhrases = listOf<Phrase>()
    private lateinit var myContext: Context

    fun updateDataList(newList: List<Phrase>){
        allPhrases = newList
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var view = itemView
        val textContent: TextView = itemView.findViewById(R.id.text_item_phrase_en)
        val textTranslation: TextView = itemView.findViewById(R.id.text_item_phrase_ch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellView = layoutInflater.inflate(R.layout.cell_card_phrase,parent, false)
        myContext = parent.context
        return ViewHolder(cellView)
    }

    override fun getItemCount(): Int {
        return allPhrases.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ob = allPhrases[position]
        holder.textContent.text = ob.content
        holder.textTranslation.text = ob.translation
    }
}