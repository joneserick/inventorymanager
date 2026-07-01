package com.stefick.inventorymanager.features.navigation

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable
import java.io.Serial

@Serializable
data object MainShellRoute

@Serializable
sealed class TopLevelDestination(
    val route: Routes,
    val title: String,
    @DrawableRes val icon: Int
) {
    @Serializable
    data object Home : TopLevelDestination(Routes.Home, "Início", android.R.drawable.ic_menu_help)
    data object Catalog : TopLevelDestination(Routes.Catalog, "Catálogo", android )
    data object Scanner :
        TopLevelDestination(Routes.Scanner, "Ler Código", Icons.Default.QrCodeScanner)

    data object Profile : TopLevelDestination(Routes.Profile, "Perfil", Icons.Default.Person)
}

val bottomNavItems = listOf(
    TopLevelDestination.Home,
    TopLevelDestination.Catalog,
    TopLevelDestination.Scanner,
    TopLevelDestination.Profile
)

@Serializable
data object AiAgentRoute