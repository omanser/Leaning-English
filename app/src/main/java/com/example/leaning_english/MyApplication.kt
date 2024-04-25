package com.example.leaning_english

import android.app.Application
import android.content.Context
import cn.leancloud.LeanCloud


class MyApplication: Application() {
    private var context: Context? = null
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        // 注意这里千万不要调用 cn.leancloud.core.LeanCloud 的 initialize 方法，否则会出现 NetworkOnMainThread 等错误。
        LeanCloud.initialize(this,
            "qtElbL7m8RSPMwQjD8VDt8be-gzGzoHsz",
            "y536AZH6EEv9xHimADUfyyyr",
            "https://qtelbl7m.lc-cn-n1-shared.com")
    }

    fun getContext(): Context? {
        return context
    }
}