package pl.solvro.cocktails.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.solvro.cocktails.data.model.Cocktail
import pl.solvro.cocktails.data.repository.CocktailRepository

class CocktailListViewModel(
    private val repository: CocktailRepository
) : ViewModel() {

    var uiState: UiState<List<Cocktail>> by mutableStateOf(UiState.Loading)
        private set

    var query by mutableStateOf("")
        private set

    private var allCocktails: List<Cocktail> = emptyList()

    init {
        loadCocktails()
    }

    fun onQueryChange(newQuery: String) {
        query = newQuery
        filterCocktails()
    }

    fun refresh() {
        loadCocktails()
    }

    private fun loadCocktails() {
        uiState = UiState.Loading
        viewModelScope.launch {
            uiState = try {
                allCocktails = repository.getCocktails()
                UiState.Success(filterByQuery(allCocktails, query))
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Nie udało się pobrać koktajli")
            }
        }
    }

    private fun filterCocktails() {
        uiState = UiState.Success(filterByQuery(allCocktails, query))
    }

    private fun filterByQuery(
        cocktails: List<Cocktail>,
        query: String
    ): List<Cocktail> {
        if (query.isBlank()) return cocktails

        return cocktails.filter {
            it.name.contains(query.trim(), ignoreCase = true)
        }
    }

    companion object {
        fun factory(repository: CocktailRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CocktailListViewModel(repository) as T
                }
            }
    }
}