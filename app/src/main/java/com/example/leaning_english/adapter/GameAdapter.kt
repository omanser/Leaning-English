package com.example.leaning_english.adapter

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.leaning_english.R
import com.example.leaning_english.activity.ui.game.GameActivity
import com.example.leaning_english.activity.dataclass.GameWord

class GameAdapter: RecyclerView.Adapter<GameAdapter.ViewHolder>(){
    val TAG = "GameAdapter"

   var currentScore = 0

    private var allMeanings = listOf<GameWord>()
    private lateinit var myContext: Context

    // 是否已经被点击
    var isClicked: Boolean = false

    var isRight = MutableLiveData<Boolean>()

    fun updateDataList(newList: List<GameWord>){
        allMeanings = newList
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var view = itemView
        val textWordMeaning: TextView = itemView.findViewById(R.id.textView_cell_meaning)
        val card: CardView = itemView.findViewById(R.id.cell_game_card)
        val imgWrong: ImageView = itemView.findViewById(R.id.imageView_wrong)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellView = layoutInflater.inflate(R.layout.cell_game_meanings,parent, false)
        myContext = parent.context
        return ViewHolder(cellView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ob = allMeanings[position]
        holder.textWordMeaning.text = ob.meanings.toString()
        //Log.d(TAG,"type = $type,   meaning = $ob")
        Log.d(TAG, "${ob.word}, ${ob.isRight}")
        holder.view.setOnClickListener {
            if(!isClicked) {
                val itemWord = allMeanings[position]
                if (itemWord.isRight) {
                    //Log.d(TAG, "选择正确！correct word: ${itemWord.word}")
                    isClicked = true
                    currentScore++
                    var color = ContextCompat.getColor(myContext, R.color.colorLittleBlue)
                    holder.card.setCardBackgroundColor(color)
                    Handler().postDelayed({
                        color = ContextCompat.getColor(myContext, R.color.colorBgWhite)
                        holder.card.setCardBackgroundColor(color)
                        isRight.postValue(true)
                    }, 400)
                } else {
                    //Log.d(TAG, "选择错误, wrong word: ${itemWord.word}")
                    isClicked = true
                    var color = ContextCompat.getColor(myContext, R.color.colorLittleRed)
                    holder.card.setCardBackgroundColor(color)
                    holder.imgWrong.visibility = View.VISIBLE
                    GameActivity.currentHP--
                    Log.d(TAG, "HP减少")
                    Handler().postDelayed({
                        color = ContextCompat.getColor(myContext, R.color.colorBgWhite)
                        holder.card.setCardBackgroundColor(color)
                        isRight.postValue(false)
                        holder.imgWrong.visibility = View.GONE
                    }, 400)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return allMeanings.size
    }

}