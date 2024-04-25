package com.example.leaning_english.activity.ui.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import cn.leancloud.LCUser
import com.example.leaning_english.ConstantData
import com.example.leaning_english.R
import com.example.leaning_english.activity.dataclass.GameWord
import com.example.leaning_english.adapter.WrongWordAdapter
import com.example.leaning_english.database.DatabaseManager
import com.example.leaning_english.databinding.ActivityFinishBinding

class FinishActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFinishBinding

    private lateinit var wrongWordAdapter: WrongWordAdapter
    private var wordIdList = mutableListOf<Int>()

    companion object{
        // 错词
        var errorWordList = mutableListOf<GameWord>()
        // 得分
        var score = 0
    }
    var isSuccess = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        viewEvent()
    }

    private fun viewEvent() {
        binding.imageViewBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun init(){
        wrongWordAdapter = WrongWordAdapter()
        binding.recyclerMeanings.layoutManager = LinearLayoutManager(this)
        binding.recyclerMeanings.adapter = wrongWordAdapter

        binding.textViewScore.text = score.toString()

        wrongWordAdapter.updateDataList(errorWordList)
    }

    private fun setUserDatabase(){
        val user = DatabaseManager.db.userDao.getUser(LCUser.currentUser().objectId)
        if(user != null) {
            val bookName = ConstantData.getWordBookString(GameActivity.currentBookId)
            if (user.maxScores[bookName] != null) {
                if (user.maxScores[bookName]!! < score) user.maxScores[bookName] = score
            }
        } else {
            Toast.makeText(this, "数据库无此用户", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        finishAfterTransition()
    }
}