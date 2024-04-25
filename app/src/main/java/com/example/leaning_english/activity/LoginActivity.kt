package com.example.leaning_english.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.leancloud.LCUser
import com.example.leaning_english.R
import com.example.leaning_english.database.DatabaseManager
import com.example.leaning_english.utils.DatabaseOperate
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"
    private lateinit var editTextTextEmailAddress: EditText
    private lateinit var editTextTextPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var textViewSignUp: TextView
    private lateinit var progressBarLogin: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(LCUser.currentUser()!=null){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        init()

//        Room.databaseBuilder(this, WordDatabase::class.java, "WordData")
//            .fallbackToDestructiveMigration()
//            .build()

        DatabaseManager.saveApplication(this.application)
//        DatabaseManager.db.wordDao.deleteAllWords().let {
//            Log.d(TAG, "word已全部删除")
//        }
        editTextTextEmailAddress.addTextChangedListener(watcher)
        editTextTextPassword.addTextChangedListener(watcher)
        btnLogin.isEnabled = false
        progressBarLogin.visibility = View.INVISIBLE

        textViewSignUp.setOnClickListener{
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }

        btnLogin.setOnClickListener {
            progressBarLogin.visibility = View.VISIBLE
            login()
        }
    }

    private fun init(){
        editTextTextEmailAddress = findViewById(R.id.editText_user_name)
        editTextTextPassword = findViewById(R.id.editText_password)
        btnLogin = findViewById(R.id.btn_login)
        textViewSignUp = findViewById(R.id.textView_signUp)
        progressBarLogin = findViewById(R.id.progressBar_login)
    }

    private fun login(){
        val name = editTextTextEmailAddress.text?.toString()?.trim()
        val pwd = editTextTextPassword.text?.toString()?.trim()
        LCUser.logIn(name, pwd).subscribe(object : Observer<LCUser> {
            override fun onSubscribe(d: Disposable) {}

            override fun onError(e: Throwable) {
                progressBarLogin.visibility = View.INVISIBLE
                Toast.makeText(this@LoginActivity, "${e.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onComplete() {}

            override fun onNext(t: LCUser) {
                val portraitUrl = t.getString("portraitUrl")
                DatabaseOperate.saveUserData(LCUser.currentUser().objectId, portraitUrl)
                Toast.makeText(this@LoginActivity, "登录成功", Toast.LENGTH_SHORT).show()
                progressBarLogin.visibility = View.INVISIBLE
                startActivity(Intent(this@LoginActivity, ChooseWordBookActivity::class.java))
            }

        })
    }

    private fun loginByEmail(){
        val email = editTextTextEmailAddress.text?.toString()?.trim()
        val pwd = editTextTextPassword.text?.toString()?.trim()
        LCUser.loginByEmail(email, pwd).subscribe(object : Observer<LCUser> {
            override fun onSubscribe(d: Disposable) {}

            override fun onError(e: Throwable) {
                progressBarLogin.visibility = View.INVISIBLE
                Toast.makeText(this@LoginActivity, "${e.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onComplete() {}

            override fun onNext(t: LCUser) {
                val portraitUrl = "portraitUrl"
                DatabaseOperate.saveUserData(LCUser.currentUser().objectId, portraitUrl)
                Toast.makeText(this@LoginActivity, "登录成功", Toast.LENGTH_SHORT).show()
                progressBarLogin.visibility = View.INVISIBLE
                startActivity(Intent(this@LoginActivity, ChooseWordBookActivity::class.java))
            }

        })
    }


    private val watcher = object: TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val b1 = editTextTextEmailAddress.text.toString().isNotEmpty()
            val b2 = editTextTextPassword.text.toString().isNotEmpty()
            btnLogin.isEnabled = b1 and b2
        }

        override fun afterTextChanged(p0: Editable?) {}

    }
}