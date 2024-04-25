package com.example.leaning_english.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.leancloud.LCObject
import cn.leancloud.LCQuery
import com.example.leaning_english.ConstantData
import com.example.leaning_english.R
import com.example.leaning_english.adapter.HistoryWordAdapter
import com.example.leaning_english.adapter.SearchAdapter
import com.example.leaning_english.database.DatabaseManager
import com.example.leaning_english.database.entity.HistorySearch
import com.example.leaning_english.database.entity.Word
import com.example.leaning_english.databinding.ActivitySearchBinding
import com.example.leaning_english.mysql.DBHelper
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchActivity : AppCompatActivity() {
    val TAG = "SearchActivity"
    private lateinit var recyclerWordSearch: RecyclerView
    private lateinit var textWordInput: EditText
    private lateinit var layoutNothing: RelativeLayout
    private lateinit var textBack: TextView
    private lateinit var binding: ActivitySearchBinding
    private var tempQuery = listOf<LCObject>()
    private var itemSearch = mutableListOf<Word>()
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var historyWordAdapter: HistoryWordAdapter

    companion object{
        val dataListHistoryLive = MutableLiveData<List<HistorySearch>?>()
    }

    private val _dataListLive = MutableLiveData<List<Word>?>()
    val dataListLive: MutableLiveData<List<Word>?> = _dataListLive

    private val _dataListLiveQuery = MutableLiveData<List<LCObject>?>()
    val dataListLiveQuery: MutableLiveData<List<LCObject>?> = _dataListLiveQuery

    private val _dataListID = MutableLiveData<List<Int>?>()
    val dataListID: MutableLiveData<List<Int>?> = _dataListID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        layoutNothing.visibility = View.GONE

        dataListLive.observe(this, Observer {
            if (it != null) {
                searchAdapter.updateDataList(it)
            }
            searchAdapter.notifyDataSetChanged()
        })

        dataListLiveQuery.observe(this, Observer {
            if (it != null) {
                searchAdapter.updateDataListLC(it)
            }
            searchAdapter.notifyDataSetChanged()
        })

        dataListHistoryLive.observe(this, Observer {
            it?.let {
                historyWordAdapter.updateDataListHistory(it)
            }
            historyWordAdapter.notifyDataSetChanged()
        })

        textWordInput.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Log.d(TAG, "s: $s, start: $start, before: $before, count: $count")
                if(start == 0 && count != 1){
                    layoutNothing.visibility = View.GONE
                    recyclerWordSearch.visibility = View.GONE
                    binding.imageViewDelete.visibility = View.GONE
                    binding.layoutHistory.visibility = View.VISIBLE
                } else {
                    binding.imageViewDelete.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if(TextUtils.isEmpty(s.toString().trim())){
                    layoutNothing.visibility = View.GONE
                    recyclerWordSearch.visibility = View.GONE
                    binding.layoutHistory.visibility = View.VISIBLE
                } else {
                    //Log.d(TAG, s.toString().trim())
                    //setData(s.toString().trim())
                    queryWord(ConstantData.BOOKLEVEL8, s.toString().trim())
                }
            }

        })

        textBack.setOnClickListener {
            onBackPressed()
        }

        binding.imageViewDelete.setOnClickListener {
            binding.textSearch.setText("")
        }

        binding.imageViewTrash.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("删除所有记录")
                .setPositiveButton("确定删除") { dialog, which ->
                    dialog.dismiss()
                    Thread {
                        Log.d(TAG, "删除历史记录")
                        deleteAllHistory()
                        dataListHistoryLive.postValue(listOf())
                        historyWordAdapter.updateDataListHistory(listOf())
                    }.start()
                }
                .setNegativeButton("取消", null)
                .show()
        }

    }

    private fun deleteAllHistory(){
        DatabaseManager.db.historySearchDao.deleteAllHistoryWords()
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }

    fun init() {
        recyclerWordSearch = findViewById(R.id.recycler_search_result)
        textWordInput = findViewById(R.id.text_search)
        layoutNothing = findViewById(R.id.relative_nothing)
        textBack = findViewById(R.id.text_back)
        searchAdapter = SearchAdapter()
        historyWordAdapter = HistoryWordAdapter()
        binding.recyclerSearchHistory.layoutManager = LinearLayoutManager(this)
        binding.recyclerSearchHistory.adapter = historyWordAdapter
        recyclerWordSearch.layoutManager = LinearLayoutManager(this)
        recyclerWordSearch.adapter = searchAdapter

        // 限制15条历史记录
        val historyList = DatabaseManager.db.historySearchDao.getAllHistoryWords()
        dataListHistoryLive.value = historyList?.reversed()?.subList(0, if (15 <= historyList.size) 15 else historyList.size)
    }

    private fun setData(s: String) {
        lifecycleScope.launch {
            itemSearch.clear()
            val words: List<Word>? = DatabaseManager.db.wordDao.queryWord(s)
            if (words?.isEmpty() == false) {
                layoutNothing.visibility = View.GONE
                recyclerWordSearch.visibility = View.VISIBLE
                _dataListLive.value = words
//                for(word in words){
//                    Log.d(TAG, "查询到的单词：$word")
//                }
            } else {
                layoutNothing.visibility = View.VISIBLE
                recyclerWordSearch.visibility = View.GONE
            }
        }
    }

    fun queryWord(bookID: Int, str: String){
        val book = ConstantData.getWordBookString(bookID)
        val query = LCQuery<LCObject>(book)
        query.whereStartsWith("word", str).limit(15).findInBackground().subscribe(object :
            io.reactivex.Observer<List<LCObject>> {
            override fun onSubscribe(d: Disposable) {}

            override fun onError(e: Throwable) {
                Log.e(TAG, "${e.message}")
            }

            override fun onComplete() {}

            override fun onNext(t: List<LCObject>) {
                if (t.isNotEmpty()){
                    layoutNothing.visibility = View.GONE
                    binding.layoutHistory.visibility = View.GONE
                    recyclerWordSearch.visibility = View.VISIBLE
                    _dataListLiveQuery.value = t
                } else {
                    recyclerWordSearch.visibility = View.GONE
                    binding.layoutHistory.visibility = View.GONE
                    layoutNothing.visibility = View.VISIBLE
                }
            }

        })
    }

    fun queryWordFromSql(bookID: Int, str: String){
        CoroutineScope(Dispatchers.IO).launch {
            val wordIDs = DBHelper.queryWordID(bookID, str)
            if (wordIDs.isNotEmpty()){
                val wordList = wordIDs.map {
                    //val word = DBHelper.queryWord(it)
                    //word
                }
                withContext(Dispatchers.Main){
                    if (wordList.isNotEmpty()){
//                        layoutNothing.visibility = View.GONE
//                        binding.layoutHistory.visibility = View.GONE
//                        recyclerWordSearch.visibility = View.VISIBLE
//                        _dataListID.value = wordList
                    }
                }
            }
        }
    }
}