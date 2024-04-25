package com.example.leaning_english.activity.ui.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.leancloud.LCObject
import cn.leancloud.LCUser
import com.example.leaning_english.ConstantData
import com.example.leaning_english.R
//import com.example.leaning_english.adapter.BookBattleAdapter
import com.example.leaning_english.database.DatabaseManager
import com.example.leaning_english.database.entity.WordBook
import com.example.leaning_english.databinding.ActivityBattleLoadBinding
import com.example.leaning_english.socket.MyWebSocketClient

class BattleLoadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBattleLoadBinding

    //private lateinit var bookBattleAdapter: BookBattleAdapter
    private var _dataListLive = MutableLiveData<List<WordBook>>()
    var dataListLive = _dataListLive
    init {
        val wordbook = DatabaseManager.db.wordBookDao.getAllWordBooks()
        dataListLive.value = wordbook
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBattleLoadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("test", "BattleLoadActivity")
        dataListLive.observe(this, Observer {
            //bookBattleAdapter.updateDataList(it)
            //bookBattleAdapter.notifyDataSetChanged()
        })

        binding.imageView15.setOnClickListener {
            onBackPressed()
        }

        binding.buttonTest.setOnClickListener {
            binding.progressBar4.visibility = View.VISIBLE
            MyWebSocketClient.client = MyWebSocketClient("${ConstantData.SERVER_URL}/${LCUser.currentUser().username}/1")
            MyWebSocketClient.client?.checkAndReconnect()
            binding.progressBar4.visibility = View.GONE
            MyWebSocketClient.client?.sendMessage("发送信息")
        }
    }

    private fun init(){
        //bookBattleAdapter = BookBattleAdapter()
        binding.recyclerViewBook.layoutManager = LinearLayoutManager(this)
        //binding.recyclerViewBook.adapter = bookBattleAdapter
    }

    override fun onBackPressed() {
        MyWebSocketClient.client?.close()
        supportFinishAfterTransition()
    }
}