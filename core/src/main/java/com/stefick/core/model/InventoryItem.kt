package com.stefick.core.model


data class InventoryItem (
    val id: String,
    val name: String,
    val description: String,
    val sku: String,
    val quantity: Int,
    val price: Double,
    val barcode: String
)