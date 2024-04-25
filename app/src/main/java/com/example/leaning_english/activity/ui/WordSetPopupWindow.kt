package com.example.leaning_english.activity.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Toast
import com.example.leaning_english.R
import com.example.leaning_english.databinding.ActivityWordSetPopupWindowBinding

class WordSetPopupWindow : Activity() {
    private lateinit var binding: ActivityWordSetPopupWindowBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordSetPopupWindowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        overridePendingTransition(R.anim.push_bottom_in, 0)
//        binding.layoutPop.setOnClickListener{
//            Toast.makeText(this, "提示：点击窗口外部关闭窗口！",
//                Toast.LENGTH_SHORT).show();
//        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        finish()
        return true
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.push_bottom_out)
    }

}