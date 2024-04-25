package com.example.leaning_english.activity

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cn.leancloud.LCUser
import com.example.leaning_english.R
import com.example.leaning_english.database.DatabaseManager
import com.example.leaning_english.databinding.ActivityMainBinding
import com.example.leaning_english.utils.UserUtils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DatabaseManager.saveApplication(this.application)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.supportActionBar?.hide()
        val navView: BottomNavigationView = binding.navView

        val user = DatabaseManager.db.userDao.getUser(LCUser.currentUser().objectId)
        user?.let {
            it.habitPhonetic = 1
            if (it.maxScores == null){
                it.maxScores = mutableMapOf()
            }
            it.maxScores?.set("WordTest", 0)
            DatabaseManager.db.userDao.updateUsers(it)
        }
        UserUtils.USER_HABIT_PHONETIC = user?.habitPhonetic?: 0
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}