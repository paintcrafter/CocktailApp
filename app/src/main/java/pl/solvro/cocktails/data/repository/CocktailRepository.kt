package pl.solvro.cocktails.data.repository

import pl.solvro.cocktails.data.model.Cocktail
import pl.solvro.cocktails.data.model.Ingredient
import pl.solvro.cocktails.data.network.CocktailApiService

class CocktailRepository(
    private val api: CocktailApiService
) {

    private var cachedIngredients: List<Ingredient>? = null

    private suspend fun getAllCocktails(): List<Cocktail> {
        val allCocktails = mutableListOf<Cocktail>()
        var page = 1

        while (true) {
            val response = api.getCocktails(page = page)
            val cocktails = response.data

            if (cocktails.isEmpty()) break

            allCocktails.addAll(cocktails)

            if (page >= response.meta.lastPage) break
            page++
        }

        return allCocktails
    }
    suspend fun getCocktails(name: String? = null): List<Cocktail> {
        val cocktails = getAllCocktails()

        return if (name.isNullOrBlank()) {
            cocktails
        } else {
            cocktails.filter { it.name.contains(name, ignoreCase = true) }
        }
    }

    suspend fun getCocktailDetails(id: Int): Cocktail {
        return api.getCocktailDetails(id).data
    }

    suspend fun getIngredientsWithImage(forceRefresh: Boolean = false): List<Ingredient> {
        if (!forceRefresh && cachedIngredients != null) {
            return cachedIngredients!!
        }

        val cocktails = getAllCocktails()

        val allIngredients = cocktails
            .map { cocktail ->
                api.getCocktailDetails(cocktail.id).data
            }
            .flatMap { it.ingredients }

        val result = allIngredients
            .filter { !it.imageUrl.isNullOrBlank() }
            .distinctBy { it.id }
            .sortedBy { it.name.lowercase() }

        cachedIngredients = result
        return result
    }

    suspend fun getIngredientById(id: Int): Ingredient? {
        val ingredients = getIngredientsWithImage()
        return ingredients.firstOrNull { it.id == id }
    }

    fun clearIngredientCache() {
        cachedIngredients = null
    }
}