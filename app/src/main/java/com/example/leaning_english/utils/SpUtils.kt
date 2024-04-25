package com.example.leaning_english.utils

import android.content.Context
import android.content.SharedPreferences

object SpUtils {
    private var isLogged: Boolean = false
    var isLoggedName = "isLogged"
    var SharedDataName = "SpUtils"
    fun setIsLogged(context: Context, isLogged: Boolean) {
        val editor: SharedPreferences.Editor = context.getSharedPreferences(
            SharedDataName,
            Context.MODE_PRIVATE
        ).edit()
        editor.putBoolean(isLoggedName, isLogged)
        editor.apply()
    }
}