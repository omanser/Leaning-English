package com.example.leaning_english.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.leaning_english.ConstantData
import com.example.leaning_english.R
import com.example.leaning_english.activity.ui.WordSetPopupWindow
import com.example.leaning_english.adapter.MeaningAdapter
import com.example.leaning_english.adapter.PhrasesAdapter
import com.example.leaning_english.database.DatabaseManager
import com.example.leaning_english.database.entity.Meanings
import com.example.leaning_english.database.entity.Phrase
import com.example.leaning_english.database.entity.Word
import com.example.leaning_english.databinding.ActivityWordCardBinding
import com.example.leaning_english.utils.DatabaseOperate
import com.example.leaning_english.utils.MediaPlayerManager
import com.example.leaning_english.utils.UserUtils

class WordCardActivity : AppCompatActivity() {

    companion object {
        const val TAG = "WordCardActivity"
        var wordId: Int = 0
        lateinit var currentWord: Word
        var enterActivity = ConstantData.SearchActivity
    }

    private var itemMeanings = mutableListOf<Meanings>()
    private lateinit var meaningAdapter: MeaningAdapter
    private lateinit var binding: ActivityWordCardBinding

    private var itemPhrases = mutableListOf<Phrase>()
    private lateinit var phrasesAdapter: PhrasesAdapter

    private val _dataListLive = MutableLiveData<List<Meanings>?>()
    val dataListLive: MutableLiveData<List<Meanings>?> = _dataListLive

    private val _dataListPhraseLive = MutableLiveData<List<Phrase>?>()
    val dataListPhraseLive: MutableLiveData<List<Phrase>?> = _dataListPhraseLive

    private var _dataCurrentWordLive = MutableLiveData<Word?>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        DatabaseOperate.insertWordNotCoroutineScope(currentWord)

        currentWord = DatabaseManager.db.wordDao.getWordsByWord(currentWord.word)!!
        initData()

        viewEvent()

        binding.imageCardVoiceUs.setOnClickListener {
            MediaPlayerManager.playSound(UserUtils.USER_HABIT_PHONETIC, currentWord.word)
        }

        dataListLive.observe(this, Observer {
            if (it != null) {
                meaningAdapter.updateDataList(it)
            }
            meaningAdapter.notifyDataSetChanged()
        })

        dataListPhraseLive.observe(this, Observer {
            if (it != null) {
                phrasesAdapter.updateDataList(it)
            }
            phrasesAdapter.notifyDataSetChanged()
        })
        binding.imageLoadBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun viewEvent(){

        //
        when(enterActivity){
            ConstantData.SearchActivity -> {

            }
            ConstantData.FinishActivity -> {

            }
        }

        binding.imageCardStar.setOnClickListener {
            Log.d(TAG, "starClicked")
            currentWord.isNewWord = !currentWord.isNewWord
            DatabaseManager.db.wordDao.updateWords(currentWord)
            when(currentWord.isNewWord){
                false -> binding.imageCardStar.setImageResource(R.drawable.icon_star_notify)
                true -> binding.imageCardStar.setImageResource(R.drawable.icon_star_xmas)
            }
        }

        binding.imageViewWordSet.setOnClickListener {
            startActivity(Intent(this, WordSetPopupWindow::class.java))
        }
    }

    private fun initData(){
        binding.textCardWord.text = currentWord.word
        val usPhonetic = "/${currentWord.usPhonetic}/"
        val ukPhonetic = "/${currentWord.ukPhonetic}/"
        binding.textCardPhoneticUs.text = usPhonetic
        binding.textCardPhoneticUk.text = ukPhonetic
        //UserUtils.USER_HABIT_PHONETIC = ConstantData.UkPHONETIC
        when(UserUtils.USER_HABIT_PHONETIC){
            ConstantData.UsPHONETIC -> {
                binding.textCardPhoneticUs.visibility = View.VISIBLE
                binding.textCardPhoneticUk.visibility = View.GONE
            }
            ConstantData.UkPHONETIC -> {
                binding.textCardPhoneticUk.visibility = View.VISIBLE
                binding.textCardPhoneticUs.visibility = View.GONE
            }
        }
        Log.d(TAG, "USER_HABIT_PHONETIC:  ${UserUtils.USER_HABIT_PHONETIC}")

        // 设置收藏
        Log.d(TAG, "initData, wordIsNew: ${currentWord.isNewWord}")
        if (currentWord.isNewWord){
            binding.imageCardStar.setImageResource(R.drawable.icon_star_xmas)
        } else binding.imageCardStar.setImageResource(R.drawable.icon_star_notify)

        meaningAdapter = MeaningAdapter()
        phrasesAdapter = PhrasesAdapter()
        binding.recyclerMeanings.layoutManager = LinearLayoutManager(this)
        binding.recyclerMeanings.adapter = meaningAdapter
        binding.recyclerCardPhases.layoutManager = LinearLayoutManager(this)
        binding.recyclerCardPhases.adapter = phrasesAdapter

        _dataListLive.value = currentWord.translation?.meanings
        val phrases = currentWord.phrases
        phrases?.let {
            if (it.isNotEmpty()) {
                binding.cardPhrases.visibility = View.VISIBLE
                _dataListPhraseLive.value = it
            }
        }

        val memory = currentWord.memory
        memory?.let {
            if (it.isNotEmpty()){
                binding.cardMemory.visibility = View.VISIBLE
                binding.textCardMemory.text = it
            }
        }

        val families = currentWord.families
        families?.let {
            if (it.isNotEmpty()) {
                var text = ""
                for (index in it.indices - 1) {
                    text += it[index] + "\n"
                }
                text += it.last()
                binding.cardFamilies.visibility = View.VISIBLE
                binding.textCardFamilies.text = text
            }
        }

        val content = currentWord.sentence?.firstOrNull()
        content?.run {
            binding.textEnglishSentence.text = this.content
            binding.textChSentence.text = this.translation
            //Log.d(TAG, "content不为空")
        }
        if (content==null){
            Log.d(TAG, "content为空")
            binding.linearSentence.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }
}