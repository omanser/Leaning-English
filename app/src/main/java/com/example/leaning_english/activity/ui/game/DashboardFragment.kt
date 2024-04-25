package com.example.leaning_english.activity.ui.game

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.leaning_english.ConstantData
import com.example.leaning_english.databinding.FragmentDashboardBinding
import com.example.leaning_english.socket.MyWebSocketClient

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.cardViewChallenge.setOnClickListener {
            val intent = Intent(this.context, LoadActivity::class.java)
            this.context?.startActivity(intent)
        }

        binding.btnBattle.setOnClickListener {
            binding.progressBarBattle.visibility = View.VISIBLE
//            if (MyWebSocketClient.client == null)
//                MyWebSocketClient.client = MyWebSocketClient("${ConstantData.SERVER_URL}/1952183393/1")
//            val isConn = MyWebSocketClient.client!!.connectBlocking()
            binding.progressBarBattle.visibility = View.GONE
//            if (!isConn){
//                return@setOnClickListener
//            }
            val intent = Intent(this.context, BattleLoadActivity::class.java)
            this.context?.startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}