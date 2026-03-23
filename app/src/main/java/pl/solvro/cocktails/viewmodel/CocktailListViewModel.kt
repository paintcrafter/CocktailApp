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

enum class AlcoholFilter {
    ALL, ALCOHOLIC, NON_ALCOHOLIC
}

class CocktailListViewModel(
    private val repository: CocktailRepository
) : ViewModel() {

    var uiState: UiState<List<Cocktail>> by mutableStateOf(UiState.Loading)
        private set

    var query by mutableStateOf("")
        private set

    var selectedCategory by mutableStateOf("All")
        private set

    var alcoholFilter by mutableStateOf(AlcoholFilter.ALL)
        private set

    private var allCocktails: List<Cocktail> = emptyList()

    val categories: List<String>
        get() = listOf("All") + allCocktails
            .mapNotNull { it.category?.takeIf(String::isNotBlank) }
            .distinct()
            .sorted()

    init {
        loadCocktails()
    }

    fun onQueryChange(newQuery: String) {
        query = newQuery
        applyFilters()
    }

    fun onCategoryChange(category: String) {
        selectedCategory = category
        applyFilters()
    }

    fun onAlcoholFilterChange(filter: AlcoholFilter) {
        alcoholFilter = filter
        applyFilters()
    }

    fun resetFilters() {
        query = ""
        selectedCategory = "All"
        alcoholFilter = AlcoholFilter.ALL
        applyFilters()
    }

    fun refresh() {
        query = ""
        selectedCategory = "All"
        alcoholFilter = AlcoholFilter.ALL
        loadCocktails()
    }

    private fun loadCocktails() {
        uiState = UiState.Loading
        viewModelScope.launch {
            uiState = try {
                allCocktails = repository.getCocktails()
                UiState.Success(filterCocktails(allCocktails))
            } catch (e: Exception) {
                UiState.Error(e.message ?: "ERROR! REFRESH!")
            }
        }
    }

    private fun applyFilters() {
        uiState = UiState.Success(filterCocktails(allCocktails))
    }

    private fun filterCocktails(cocktails: List<Cocktail>): List<Cocktail> {
        return cocktails.filter { cocktail ->
            val matchesQuery = query.isBlank() ||
                    cocktail.name.contains(query.trim(), ignoreCase = true)

            val matchesCategory = selectedCategory == "All" ||
                    cocktail.category == selectedCategory

            val matchesAlcohol = when (alcoholFilter) {
                AlcoholFilter.ALL -> true
                AlcoholFilter.ALCOHOLIC -> cocktail.alcoholic
                AlcoholFilter.NON_ALCOHOLIC -> !cocktail.alcoholic
            }

            matchesQuery && matchesCategory && matchesAlcohol
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