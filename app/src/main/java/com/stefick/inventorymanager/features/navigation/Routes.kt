package com.stefick.inventorymanager.features.navigation

import kotlinx.serialization.Serializable

 @Serializable
sealed class Routes {

    @Serializable
    data object Login: Routes()

    @Serializable
    data object Home: Routes()

    @Serializable
    data object Inventory: Routes()

    @Serializable
    data object Scanner: Routes()

    @Serializable
    data object AiAgent: Routes()
}

