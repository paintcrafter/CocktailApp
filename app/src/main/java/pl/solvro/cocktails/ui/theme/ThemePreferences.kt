package pl.solvro.cocktails.ui.theme

import android.content.Context
import pl.solvro.cocktails.ui.theme.ThemeMode

class ThemePreferences(context: Context) {

    private val prefs = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

    fun getThemeMode(): ThemeMode {
        val saved = prefs.getString(KEY_THEME_MODE, ThemeMode.SYSTEM.name)
            ?: ThemeMode.SYSTEM.name
        return runCatching { ThemeMode.valueOf(saved) }
            .getOrDefault(ThemeMode.SYSTEM)
    }

    fun setThemeMode(themeMode: ThemeMode) {
        prefs.edit()
            .putString(KEY_THEME_MODE, themeMode.name)
            .apply()
    }

    companion object {
        private const val KEY_THEME_MODE = "theme_mode"
    }
}