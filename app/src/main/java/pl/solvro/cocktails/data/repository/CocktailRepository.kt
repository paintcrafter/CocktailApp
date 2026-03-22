package pl.solvro.cocktails.data.repository

import pl.solvro.cocktails.data.model.Cocktail
import pl.solvro.cocktails.data.network.CocktailApiService

class CocktailRepository(
    private val apiService: CocktailApiService
) {
    suspend fun getCocktails(query: String): List<Cocktail> {
        return apiService.getCocktails(name = query.takeIf { it.isNotBlank() }).data
    }

    suspend fun getCocktailDetails(id: Int): Cocktail {
        return apiService.getCocktailDetails(id).data
    }
}
