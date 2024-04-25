package com.example.leaning_english.activity.ui.mine

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cn.leancloud.LCUser
import coil.load
import com.example.leaning_english.activity.ChooseWordBookActivity
import com.example.leaning_english.activity.LoginActivity
import com.example.leaning_english.database.DatabaseManager
import com.example.leaning_english.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this)[NotificationsViewModel::class.java]
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        viewEvent()



        val root: View = binding.root
        return root
    }

    private fun viewEvent(){
        _binding!!.buttonLogout.setOnClickListener {
            LCUser.logOut()
            val intent = Intent(this@NotificationsFragment.context, LoginActivity::class.java)
            this.context?.startActivity(intent)
            this.activity?.finish()
        }

        binding.textViewUserName.text = LCUser.currentUser().username

        val user = DatabaseManager.db.userDao.getUser(LCUser.currentUser().objectId)
        user?.apply {
            userName = LCUser.currentUser().username
            userPortrait = LCUser.currentUser().getString("portraitUrl")
            DatabaseManager.db.userDao.updateUsers(this)
        }
        var portraitUrl = DatabaseManager.db.userDao.getUser(LCUser.currentUser().objectId)?.userPortrait
        portraitUrl?.let {
            _binding!!.imageViewPortrait.load(portraitUrl)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}