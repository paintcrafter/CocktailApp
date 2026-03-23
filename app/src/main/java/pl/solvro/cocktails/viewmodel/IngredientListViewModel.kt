package pl.solvro.cocktails.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.solvro.cocktails.data.model.Ingredient
import pl.solvro.cocktails.data.repository.CocktailRepository

class IngredientListViewModel(
    private val repository: CocktailRepository
) : ViewModel() {

    var uiState: UiState<List<Ingredient>> by mutableStateOf(UiState.Loading)
        private set

    var query by mutableStateOf("")
        private set

    private var allIngredients: List<Ingredient> = emptyList()

    init {
        loadIngredients()
    }

    fun onQueryChange(newQuery: String) {
        query = newQuery
        filterIngredients()
    }

    fun refresh() {
        repository.clearIngredientCache()
        loadIngredients(forceRefresh = true)
    }

    private fun loadIngredients(forceRefresh: Boolean = false) {
        uiState = UiState.Loading
        viewModelScope.launch {
            uiState = try {
                allIngredients = repository.getIngredientsWithImage(forceRefresh)
                val filtered = filterByQuery(allIngredients, query)
                UiState.Success(filtered)
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Nie udało się pobrać składników")
            }
        }
    }

    private fun filterIngredients() {
        uiState = UiState.Success(filterByQuery(allIngredients, query))
    }

    private fun filterByQuery(
        ingredients: List<Ingredient>,
        query: String
    ): List<Ingredient> {
        if (query.isBlank()) return ingredients

        return ingredients.filter {
            it.name.contains(query.trim(), ignoreCase = true)
        }
    }

    companion object {
        fun factory(repository: CocktailRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return IngredientListViewModel(repository) as T
                }
            }
    }
}