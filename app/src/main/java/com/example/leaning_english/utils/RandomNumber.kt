package com.example.leaning_english.utils

import java.util.Collections
import kotlin.random.Random
import kotlin.random.nextInt

object RandomNumber {

    fun getRandomList(size: Int): MutableList<Int>{
        val randomList = mutableListOf<Int>()
        for (i in 0 until size){
            randomList.add(i)
        }
        randomList.shuffle(java.util.Random())
        return randomList
    }

}