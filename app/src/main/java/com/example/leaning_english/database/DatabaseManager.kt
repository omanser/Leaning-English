package com.example.leaning_english.database

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseManager {
    private const val DB_NAME = "WordData"
    private val MIGRATIONS = arrayOf(Migration1)
    private lateinit var application: Application
    val db: WordDatabase by lazy {
        Room.databaseBuilder(application.applicationContext, WordDatabase::class.java, DB_NAME)
            .addCallback(CreatedCallBack)
            .addMigrations(*MIGRATIONS)
            .allowMainThreadQueries()
            .build()
    }

    fun saveApplication(application: Application){
        DatabaseManager.application = application
    }

    private object CreatedCallBack: RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            MIGRATIONS.map{
                Migration1.migrate(db)
            }
        }
    }

    private object Migration1: Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
//            database.execSQL("ALTER TABLE USER " +
//                    "ADD COLUMN habitPhonetic INTEGER NOT NULL DEFAULT 0")
//            database.execSQL("ALTER TABLE WORD " +
//                    "ADD COLUMN explanation TEXT")
//            database.execSQL("ALTER TABLE WORD " +
//                    "ADD COLUMN translation TEXT")
//            database.execSQL("ALTER TABLE WORD " +
//                    "ADD COLUMN phrases TEXT")
//            database.execSQL("ALTER TABLE WORD " +
//                    "ADD COLUMN sentence TEXT")
//            database.execSQL("ALTER TABLE WORD " +
//                    "ADD COLUMN belongChuZhong INTEGER NOT NULL DEFAULT 0")
//            database.execSQL("ALTER TABLE WORD " +
//                    "ADD COLUMN belongGaoZhong INTEGER NOT NULL DEFAULT 0")
//            database.execSQL("ALTER TABLE WORD " +
//                    "ADD COLUMN belongGRE INTEGER NOT NULL DEFAULT 0")
//            database.execSQL("ALTER TABLE WORD " +
//                    "ADD COLUMN belongLevel8 INTEGER NOT NULL DEFAULT 0")
//            database.execSQL("ALTER TABLE WORD " +
//                    "ADD COLUMN belongIELTS INTEGER NOT NULL DEFAULT 0")
        }
    }

    private object Migration4: Migration(3, 4){
        override fun migrate(database: SupportSQLiteDatabase) {
            Log.d("migration", "数据库更新")
            database.execSQL("CREATE TABLE tempUser(" +
                    "currentBookId INTEGER not Null default null," +
                    "habitPhonetic INTEGER not Null default null," +
                    "id TEXT primary key not Null default null," +
                    "userName TEXT not Null default null," +
                    "userPortrait INTEGER not null default null," +
                    "maxScores TEXT default null" +
                    ")")

            database.execSQL("INSERT INTO tempUser (" +
                    "currentBookId, id, userName, userPortrait) " +
                    "SELECT currentBookId, id, userName, userPortrait FROM User")

            database.execSQL(" DROP TABLE User")

            database.execSQL(" ALTER TABLE tempUser RENAME to User")
        }

    }
}