package com.example.leaning_english.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import cn.leancloud.LCUser
import cn.leancloud.LCUser.requestEmailVerifyInBackground
import cn.leancloud.types.LCNull
import com.example.leaning_english.R
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class SignUpActivity : AppCompatActivity() {

    private lateinit var lBtnSignUp: Button
    private lateinit var lEtEmail: EditText
    private lateinit var lEtUsername: EditText
    private lateinit var lEtPassword: EditText
    private lateinit var lProgressBarSignUp: ProgressBar
    private lateinit var imgBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        init()

        lEtUsername.addTextChangedListener(watcher)
        lEtPassword.addTextChangedListener(watcher)
        lProgressBarSignUp.visibility = View.INVISIBLE

        lBtnSignUp.setOnClickListener {
            signUpByEmail()
        }

        imgBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun init(){
        lBtnSignUp = findViewById(R.id.btn_signUp)
        lEtEmail = findViewById(R.id.et_signUpEmail)
        lEtUsername = findViewById(R.id.et_signUpUsername)
        lEtPassword = findViewById(R.id.et_signUpPassword)
        lProgressBarSignUp = findViewById(R.id.progressBarSignUp)
        imgBack = findViewById(R.id.imageView_sign_back)
    }

    override fun onBackPressed() {
        finishAfterTransition()
    }

    private fun signUp(){
        val email = lEtEmail.text?.trim().toString()
        val name = lEtUsername.text?.trim().toString()
        val pwd = lEtPassword.text?.trim().toString()
        LCUser().apply {
            lProgressBarSignUp.visibility = View.VISIBLE
            this.email = email
            username = name
            password = pwd
            signUpInBackground().subscribe(object : Observer<LCUser> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    lProgressBarSignUp.visibility = View.INVISIBLE
                    Toast.makeText(this@SignUpActivity, "${e.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onComplete() {}

                override fun onNext(t: LCUser) {
                    Toast.makeText(this@SignUpActivity, "注册成功", Toast.LENGTH_SHORT).show()
                    LCUser.logIn(name, pwd).subscribe(object :Observer<LCUser>{
                        override fun onSubscribe(d: Disposable) {}

                        override fun onError(e: Throwable) {
                            lProgressBarSignUp.visibility = View.INVISIBLE
                            Toast.makeText(this@SignUpActivity, "${e.message}", Toast.LENGTH_SHORT).show()
                        }

                        override fun onComplete() {}

                        override fun onNext(t: LCUser) {
                            startActivity(Intent(this@SignUpActivity, ChooseWordBookActivity::class.java).also{
                                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            })
                            finish()
                        }

                    })
                }

            })
        }
    }

    private fun signUpByEmail(){
        val email = lEtEmail.text?.trim().toString()
        val name = lEtUsername.text?.trim().toString()
        val pwd = lEtPassword.text?.trim().toString()
        LCUser().apply {
            lProgressBarSignUp.visibility = View.VISIBLE
            setEmail(email)
            password = pwd
            username = name
            signUpInBackground().subscribe(object : Observer<LCUser> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: LCUser) {
                    Toast.makeText(this@SignUpActivity, "注册成功", Toast.LENGTH_SHORT).show()
                    LCUser.loginByEmail(email, pwd).subscribe(object :Observer<LCUser>{
                        override fun onSubscribe(d: Disposable) {}

                        override fun onError(e: Throwable) {
                            lProgressBarSignUp.visibility = View.INVISIBLE
                            Toast.makeText(this@SignUpActivity, "${e.message}", Toast.LENGTH_SHORT).show()
                            Log.d("SignUpActivity", "${e.message}")
                        }

                        override fun onComplete() {}

                        override fun onNext(t: LCUser) {
                            startActivity(Intent(this@SignUpActivity, ChooseWordBookActivity::class.java).also{
                                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            })
                            finish()
                        }

                    })
                }

                override fun onError(e: Throwable) {
                    lProgressBarSignUp.visibility = View.INVISIBLE
                    Toast.makeText(this@SignUpActivity, "${e.message}", Toast.LENGTH_LONG).show()
                    Log.d("SignUpActivity", "${e.message}")
                }

                override fun onComplete() {}

            })
        }
    }

    private val watcher = object: TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val uname = lEtUsername.text.toString()
            val password = lEtPassword.text.toString()
            lBtnSignUp.isEnabled = uname.isNotEmpty() && password.isNotEmpty()
        }

        override fun afterTextChanged(p0: Editable?) {}

    }
}