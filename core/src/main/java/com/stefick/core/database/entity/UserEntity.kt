package com.stefick.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stefick.core.model.UserRole

@Entity(tableName = "users")
data class UserEntity (
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val passwordHash: String,
    val role: UserRole,
    val isActive: Boolean = true
)
