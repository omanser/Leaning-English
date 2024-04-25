package com.example.leaning_english.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.leaning_english.database.entity.User

@Dao
interface UserDao {
    @Insert
    fun insertUsers(vararg users: User)

    @Update
    fun updateUsers(vararg users: User)

    @Delete
    fun deleteUsers(vararg users: User)

    @Query("SELECT * FROM USER WHERE ID = :id")
    fun getUser(id: String): User?

}