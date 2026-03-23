package pl.solvro.cocktails

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.solvro.cocktails.ui.theme.ThemePreferences
import pl.solvro.cocktails.ui.navigation.CocktailAppNavHost
import pl.solvro.cocktails.ui.theme.CocktailAppTheme
import pl.solvro.cocktails.viewmodel.ThemeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val themeViewModel: ThemeViewModel = viewModel(
                factory = ThemeViewModel.factory(ThemePreferences(applicationContext))
            )

            CocktailAppTheme(
                themeMode = themeViewModel.themeMode
            ) {
                CocktailAppNavHost(
                    themeViewModel = themeViewModel
                )
            }
        }
    }
}