package pl.solvro.cocktails.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.solvro.cocktails.ui.theme.ThemePreferences
import pl.solvro.cocktails.ui.theme.ThemeMode

class ThemeViewModel(
    private val preferences: ThemePreferences
) : ViewModel() {

    var themeMode by mutableStateOf(preferences.getThemeMode())
        private set

    fun changeThemeMode(mode: ThemeMode) {
        themeMode = mode
        preferences.setThemeMode(mode)
    }

    companion object {
        fun factory(preferences: ThemePreferences): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ThemeViewModel(preferences) as T
                }
            }
    }
}