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

class IngredientDetailsViewModel(
    private val repository: CocktailRepository,
    private val ingredientId: Int
) : ViewModel() {

    var uiState: UiState<Ingredient> by mutableStateOf(UiState.Loading)
        private set

    init {
        loadIngredient()
    }

    fun refresh() {
        loadIngredient()
    }

    private fun loadIngredient() {
        uiState = UiState.Loading
        viewModelScope.launch {
            uiState = try {
                val ingredient = repository.getIngredientById(ingredientId)
                if (ingredient != null) {
                    UiState.Success(ingredient)
                } else {
                    UiState.Error("Nie znaleziono składnika")
                }
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Nie udało się pobrać składnika")
            }
        }
    }

    companion object {
        fun factory(
            repository: CocktailRepository,
            ingredientId: Int
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return IngredientDetailsViewModel(repository, ingredientId) as T
                }
            }
    }
}