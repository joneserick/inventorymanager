package com.stefick.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.stefick.core.database.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Insert
    suspend fun insertUser(user: UserEntity)
}