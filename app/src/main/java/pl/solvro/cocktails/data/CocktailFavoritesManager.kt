package pl.solvro.cocktails.data

import android.content.Context

class CocktailFavoritesManager(context: Context) {

    private val prefs = context.getSharedPreferences("cocktail_favorites", Context.MODE_PRIVATE)

    fun getFavorites(): Set<Int> {
        return prefs.getStringSet(KEY_FAVORITES, emptySet())
            ?.mapNotNull { it.toIntOrNull() }
            ?.toSet()
            ?: emptySet()
    }

    fun toggleFavorite(cocktailId: Int) {
        val updated = getFavorites().toMutableSet()
        if (updated.contains(cocktailId)) {
            updated.remove(cocktailId)
        } else {
            updated.add(cocktailId)
        }

        prefs.edit()
            .putStringSet(KEY_FAVORITES, updated.map { it.toString() }.toSet())
            .apply()
    }

    companion object {
        private const val KEY_FAVORITES = "favorites"
    }
}