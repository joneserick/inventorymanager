package com.stefick.core.model

enum class UserRole(val permissions: List<String>) {
    ADMIN(listOf("ALL")),
    SUPERVISOR(listOf("READ", "WRITE", "ADJUST", "MANAGE_USERS")),
    STOCKER(listOf("READ", "WRITE", "TRANSFER")),
    VENDOR(listOf("READ", "SELL"))
}