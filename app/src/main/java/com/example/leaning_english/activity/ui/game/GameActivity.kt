package com.example.leaning_english.activity.ui.game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.leaning_english.activity.MainActivity
import com.example.leaning_english.activity.dataclass.GameWord
import com.example.leaning_english.adapter.GameAdapter
import com.example.leaning_english.database.DatabaseManager
import com.example.leaning_english.database.entity.Word
import com.example.leaning_english.databinding.ActivityGameBinding
import com.example.leaning_english.utils.MediaPlayerManager
import com.example.leaning_english.utils.RandomNumber
import com.example.leaning_english.utils.UserUtils
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding

    private val baseHP = 3

    private var baseTime: Long = 0
    private val progressBarMax = 5000
    private val progressBarInterval = 10 // 每 100 毫秒更新一次进度
    private val totalLoadingTime = 5000 // 总共加载时间为 5 秒

    private var timeOut: Boolean = false

    private var errorWordList = mutableListOf<GameWord>()

    private lateinit var gameAdapter: GameAdapter

    var wordList: List<Word> = listOf()

    private var gameWordList = mutableListOf<GameWord>()

    private var _dataListLive =  MutableLiveData<List<GameWord>>()
    val dataListLive: MutableLiveData<List<GameWord>> = _dataListLive

    private var randomList = mutableListOf<Int>()

    companion object {
        const val TAG = "GameActivity"
        // 随机列表下标
        var currentWordIndex = 0

        // 当前单词列表下标
        var gameWordIndex: Int = 0
        var currentHP = 3

        var currentBookId = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()

        gameAdapter = GameAdapter()

        binding.recyclerMeanings.layoutManager = LinearLayoutManager(this)
        binding.recyclerMeanings.adapter = gameAdapter

        _dataListLive.observe(this, Observer {
            gameAdapter.updateDataList(it)
            gameAdapter.notifyDataSetChanged()
        })

        viewEvent()

        gameAdapter.isRight.observe(this, Observer {
            if (currentWordIndex < gameWordList.size - 1) {
                if (it)
                    Log.d(TAG, "选择正确！correct word: ${gameWordList[gameWordIndex].word}")
                else {
                    errorWordList.add(gameWordList[gameWordIndex])
                    Log.d(TAG, "选择错误, correct word: ${gameWordList[gameWordIndex].word}")
                }
                updateView()
            } else {
                Toast.makeText(this, "恭喜你，打通了本单词书！", Toast.LENGTH_SHORT).show()
                toFinish()
            }
        })

        generateRandomSequence()

        updateView()
    }

    private fun toFinish(){
        Log.d(TAG, errorWordList.toString())
        FinishActivity.errorWordList = errorWordList
        FinishActivity.score = gameAdapter.currentScore
        val intent = Intent(this, FinishActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun viewEvent(){
        binding.imageViewGoHome.setOnClickListener {
            val intent = Intent(this@GameActivity, MainActivity::class.java)
            this.startActivity(intent)
            finish()
        }
    }

    private fun initData(){
        currentWordIndex = 0
        currentHP = 3
        gameWordIndex = 0
        DatabaseManager.db.wordDao.getAllWordsLive().let {
            Log.d(TAG, "wordList = $it")
            if (it != null) {
                for (word in it){
                    gameWordList.add(GameWord(
                        wordID = word.id,
                        word = word.word,
                        meanings = word.translation?.meanings?.first(),
                        usPhonetic = word.usPhonetic,
                        ukPhonetic = word.ukPhonetic
                    ))
                }
            }
//            for (word in gameWordList){
//                Log.d(TAG, "$word")
//            }
            Log.d(TAG, "gameWordList.size = ${gameWordList.size}")
        }
    }

    private fun generateRandomSequence(){

        randomList = RandomNumber.getRandomList(gameWordList.size)

//        for (item in randomList){
//            Log.d(TAG, "random: $item")
//        }

        Log.d(TAG, "randomList.size: ${randomList.size}")

    }

    private fun progress(){
        baseTime = System.currentTimeMillis()
        // 初始化进度条
        binding.progressBar.max = progressBarMax
        binding.progressBar.progress = progressBarMax - (System.currentTimeMillis() - baseTime).toInt()

        // 开始加载
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            private var progress = 0

            override fun run() {
                if (progress >= 0) {
                    progress = progressBarMax - (System.currentTimeMillis() - baseTime).toInt()
//                    if(progress < progressBarMax / 3) binding.progressBar.progressTintList.
                    binding.progressBar.progress = progress
                    if(!gameAdapter.isClicked)
                        handler.postDelayed(this, progressBarInterval.toLong())
                } else {
                    if(!gameAdapter.isClicked) {
                        timeOut = true
                        currentHP--
                        errorWordList.add(gameWordList[gameWordIndex])
                        Log.d(TAG, "errorWordList添加：${gameWordList[gameWordIndex]}")
                        Log.d(TAG, "HP减少, currentWord: ${gameWordList[gameWordIndex].word}")
                        Log.d(TAG, "$currentHP")
                        if (currentHP >= 0) {
                            if (currentWordIndex < gameWordList.size - 1)
                                updateView()
                            else {
                                toFinish()
                            }
                        }
                        else gameAdapter.isClicked = true
                    }
                }
            }
        }

        handler.postDelayed(runnable, progressBarInterval.toLong())

//        // 模拟加载完成
//        handler.postDelayed({
//            binding.progressBar.progress = 0
//        }, totalLoadingTime.toLong())
    }

    private fun updateView(){
        currentWordIndex++
        //binding.textViewGameHP.text = "血量：$currentHP"
        binding.textViewGameScore.text = "得分：${gameAdapter.currentScore}"
        when(currentHP){
            2 -> binding.imageViewLove3.visibility = View.INVISIBLE
            1 -> binding.imageViewLove2.visibility = View.INVISIBLE
            0 -> binding.imageViewLove1.visibility = View.INVISIBLE
            else -> Log.d(TAG, "currentHP: $currentHP")
        }
        if (currentHP <= 0){
            Toast.makeText(this@GameActivity, "血量耗尽，挑战结束！", Toast.LENGTH_SHORT).show()
            toFinish()
        } else if (gameWordList.size > 0) {
            gameAdapter.isClicked = false
            if(timeOut) timeOut = false
            gameWordIndex = randomList[currentWordIndex]
            MediaPlayerManager.playSound(UserUtils.USER_HABIT_PHONETIC, gameWordList[gameWordIndex].word)
            binding.textViewGameWord.text = gameWordList[gameWordIndex].word
            val phonetic = "/${gameWordList[gameWordIndex].usPhonetic}/"
            binding.textViewGamePhonetic.text = phonetic

            val meanList = mutableListOf<GameWord>()
            val correctOption = Random.nextInt(if(4<gameWordList.size) 4 else gameWordList.size)

            val randomMeanIndexList = RandomNumber.getRandomList(gameWordList.size).subList(0, if(5<gameWordList.size) 5 else gameWordList.size)

            var flag = false
            for ((count, i) in randomMeanIndexList.withIndex()) {
                if(i == gameWordIndex) {
                    flag = true
                    continue
                }
                if (count == 4 && !flag) break
                if (count == correctOption) {
                    val word = gameWordList[gameWordIndex]
                    Log.d(TAG, "count = $count, correctOption = $correctOption, word: ${word.word}, ${word.isRight}")
                    word.isRight = true
                    meanList.add(word)
                } else {
                    val word = gameWordList[i]
                    word.isRight = false
                    meanList.add(word)
                }
            }
            Log.d(TAG, "correct option: $correctOption, meanList: $meanList")
            _dataListLive.value = meanList
            progress()
        }
    }
}