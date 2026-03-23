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
        title = "Koktajle",
        icon = Icons.Default.LocalBar
    )

    data object Ingredients : BottomNavItem(
        route = "ingredient_list",
        title = "Składniki",
        icon = Icons.Default.MenuBook
    )
}