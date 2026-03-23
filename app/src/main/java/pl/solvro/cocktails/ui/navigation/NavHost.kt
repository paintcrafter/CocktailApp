package pl.solvro.cocktails.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Cocktails : BottomNavItem(
        route = "cocktail_list",
        title = "Cocktails",
        icon = Icons.Default.LocalBar
    )

    data object Ingredients : BottomNavItem(
        route = "ingredient_list",
        title = "Ingredients",
        icon = Icons.Default.MenuBook
    )
}