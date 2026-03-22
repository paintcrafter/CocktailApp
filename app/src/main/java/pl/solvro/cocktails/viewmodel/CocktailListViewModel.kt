package pl.solvro.cocktails.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.solvro.cocktails.data.model.Cocktail
import pl.solvro.cocktails.data.repository.CocktailRepository

class CocktailListViewModel(
    private val repository: CocktailRepository
) : ViewModel() {

    var uiState by mutableStateOf<UiState<List<Cocktail>>>(UiState.Loading)
        private set

    var query by mutableStateOf("")
        private set

    private var searchJob: Job? = null

    init {
        fetchCocktails()
    }

    fun onQueryChange(newValue: String) {
        query = newValue
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(350)
            fetchCocktails()
        }
    }

    fun refresh() {
        fetchCocktails()
    }

    private fun fetchCocktails() {
        viewModelScope.launch {
            uiState = UiState.Loading
            uiState = try {
                UiState.Success(repository.getCocktails(query))
            } catch (exception: Exception) {
                UiState.Error(exception.message ?: "Nie udało się pobrać listy koktajli.")
            }
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
