package com.example.leaning_english.database

import com.google.gson.Gson

class GsonInstance {
    var INSTANCE: GsonInstance? = null
        get() {
            if (field == null){
                synchronized(GsonInstance::class.java){
                    if (field == null){
                        field = GsonInstance()
                    }
                }
            }
            return field
        }
    var gson: Gson? = null
        get() {
            if (field == null){
                synchronized(GsonInstance::class.java){
                    if(field == null){
                        field = Gson()
                    }
                }
            }
            return field
        }


}