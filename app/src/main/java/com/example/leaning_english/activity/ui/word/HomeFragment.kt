package com.example.leaning_english.activity.ui.word

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.leaning_english.activity.ChooseWordBookActivity
import com.example.leaning_english.activity.SearchActivity
import com.example.leaning_english.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    val TAG = "HomeFragment"

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        _binding?.cardSearch?.setOnClickListener {
            val intent = Intent(this@HomeFragment.context, SearchActivity::class.java)
            this.context?.startActivity(intent)
        }

        _binding?.layoutPlan?.setOnClickListener{
            val intent = Intent(this@HomeFragment.context, ChooseWordBookActivity::class.java)
            this.context?.startActivity(intent)
        }

        val androidId = Settings.System.getString(context?.contentResolver, Settings.System.ANDROID_ID);
        Log.i("HomeFragment", "唯一标示==$androidId")
//        _binding?.button2?.setOnClickListener{
//            val word = _binding?.editTextWordInput?.text.toString()
//            Log.d(TAG, "input: --$word--")
//            val words = DatabaseManager.db.wordDao.getWordsByWord(word)
//            var str = ""
//            if (words != null) {
//                if (words.isNotEmpty()) {
//                    Log.d(TAG, "words: $words")
//                    for (item in words){
//                        str += item.toString() + "\n"
//                        Log.d(TAG, "item: ${item.toString()}")
//                    }
//                    _binding?.textView3?.text = str
//                    Log.d(TAG, str)
//                } else {
//                    Log.d(TAG, "未查询到此单词")
//                    _binding?.textView3?.text = "未查询到此单词"
//                }
//            }
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}