package com.example.leaning_english.activity.ui.game

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.leaning_english.databinding.ActivityLoadBinding
import com.example.leaning_english.mysql.DBHelper
import com.example.leaning_english.utils.Coroutine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoadActivity : AppCompatActivity() {
    private val TAG = "LoadActivity"
    private lateinit var binding: ActivityLoadBinding

    private var baseTime: Long = 0
    private val progressBarMax = 5000
    private val progressBarInterval = 10 // 每 100 毫秒更新一次进度
    private val totalLoadingTime = 10000 // 总共加载时间为 10 秒

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewEvent()

        // 使用
        //MyWebSocketClient.main(arrayOf())

        //progress()
    }

    private fun viewEvent(){
        binding.textViewLoadContinue.setOnClickListener {
            val intent = Intent(this@LoadActivity, GameActivity::class.java)
            this@LoadActivity.startActivity(intent)
            finish()
        }

        binding.imageViewNext.setOnClickListener {
            val intent = Intent(this@LoadActivity, GameActivity::class.java)
            this@LoadActivity.startActivity(intent)
            finish()
        }

        binding.imageLoadBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }

    private fun progress(){
        baseTime = System.currentTimeMillis()
        // 初始化进度条
        binding.progressBar2.max = progressBarMax
        binding.progressBar2.progress = (System.currentTimeMillis() - baseTime).toInt()

        // 开始加载
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            private var progress = 0

            override fun run() {
                if (progress < progressBarMax) {
                    progress = (System.currentTimeMillis() - baseTime).toInt()
                    binding.progressBar2.progress = progress
                    handler.postDelayed(this, progressBarInterval.toLong())
                } else {
                    val intent = Intent(this@LoadActivity, GameActivity::class.java)
                    this@LoadActivity.startActivity(intent)
                }
            }
        }

        handler.postDelayed(runnable, progressBarInterval.toLong())

        // 模拟加载完成
        handler.postDelayed({
            binding.progressBar2.progress = progressBarMax
        }, totalLoadingTime.toLong())
    }
}