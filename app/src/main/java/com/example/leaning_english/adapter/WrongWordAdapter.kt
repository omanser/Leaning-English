package com.example.leaning_english.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.leaning_english.ConstantData
import com.example.leaning_english.R
import com.example.leaning_english.activity.WordCardActivity
import com.example.leaning_english.activity.dataclass.GameWord
import com.example.leaning_english.database.DatabaseManager
import com.example.leaning_english.utils.DatabaseOperate
import org.w3c.dom.Text

class WrongWordAdapter: RecyclerView.Adapter<WrongWordAdapter.ViewHolder>() {

    val TAG = "WrongWordAdapter"

    private var allWord = listOf<GameWord>()
    private lateinit var myContext: Context

    fun updateDataList(newList: List<GameWord>){
        allWord = newList
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        var view = itemView
        val textSequence: TextView = itemView.findViewById(R.id.textView_sequence)
        val textWord: TextView = itemView.findViewById(R.id.textView_word)
        val textMeanings: TextView = itemView.findViewById(R.id.textView_meanings)
        val imgStar: ImageView = itemView.findViewById(R.id.imageView_star)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellView = layoutInflater.inflate(R.layout.cell_finish_word, parent, false)
        myContext = parent.context
        return ViewHolder(cellView)
    }

    override fun getItemCount(): Int {
        return allWord.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ob = allWord[position]
        holder.textSequence.text = (position + 1).toString()
        holder.textWord.text = ob.word
        holder.textMeanings.text = ob.meanings.toString()
        val word = DatabaseManager.db.wordDao.getWordsByWord(ob.word)
        word?.let {
            if (it.isNewWord)
                holder.imgStar.setImageResource(R.drawable.icon_star_xmas)
            else
                holder.imgStar.setImageResource(R.drawable.icon_star_notify)
        }?:holder.imgStar.setImageResource(R.drawable.icon_star_notify)
        holder.view.setOnClickListener {
            Log.d(TAG, "position: $position")
            val itemWord = allWord[position]
            WordCardActivity.wordId = itemWord.wordID
            WordCardActivity.currentWord = word!!
            WordCardActivity.enterActivity = ConstantData.FinishActivity
            val intent = Intent(myContext, WordCardActivity::class.java)
            myContext.startActivity(intent)
        }
    }
}