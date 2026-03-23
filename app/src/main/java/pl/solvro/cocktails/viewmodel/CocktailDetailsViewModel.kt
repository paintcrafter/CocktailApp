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

class CocktailDetailsViewModel(
    private val repository: CocktailRepository,
    private val cocktailId: Int
) : ViewModel() {

    var uiState by mutableStateOf<UiState<Cocktail>>(UiState.Loading)
        private set

    init {
        fetchCocktailDetails()
    }

    fun refresh() {
        fetchCocktailDetails()
    }

    private fun fetchCocktailDetails() {
        viewModelScope.launch {
            uiState = UiState.Loading
            uiState = try {
                UiState.Success(repository.getCocktailDetails(cocktailId))
            } catch (exception: Exception) {
                UiState.Error(exception.message ?: "ERROR! REFRESH!")
            }
        }
    }

    companion object {
        fun factory(repository: CocktailRepository, cocktailId: Int): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CocktailDetailsViewModel(repository, cocktailId) as T
                }
            }
    }
}
