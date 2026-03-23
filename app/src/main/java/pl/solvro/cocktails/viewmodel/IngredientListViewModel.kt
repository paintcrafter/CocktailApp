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

    var selectedType by mutableStateOf("All")
        private set

    var alcoholFilter by mutableStateOf(AlcoholFilter.ALL)
        private set

    private var allIngredients: List<Ingredient> = emptyList()

    val ingredientTypes: List<String>
        get() = listOf("All") + allIngredients
            .mapNotNull { it.type?.takeIf(String::isNotBlank) }
            .distinct()
            .sorted()

    init {
        loadIngredients()
    }

    fun onQueryChange(newQuery: String) {
        query = newQuery
        applyFilters()
    }

    fun onTypeChange(type: String) {
        selectedType = type
        applyFilters()
    }

    fun onAlcoholFilterChange(filter: AlcoholFilter) {
        alcoholFilter = filter
        applyFilters()
    }

    fun resetFilters() {
        query = ""
        selectedType = "All"
        alcoholFilter = AlcoholFilter.ALL
        applyFilters()
    }

    fun refresh() {
        query = ""
        selectedType = "All"
        alcoholFilter = AlcoholFilter.ALL
        repository.clearIngredientCache()
        loadIngredients(forceRefresh = true)
    }

    private fun loadIngredients(forceRefresh: Boolean = false) {
        uiState = UiState.Loading
        viewModelScope.launch {
            uiState = try {
                allIngredients = repository.getIngredientsWithImage(forceRefresh)
                UiState.Success(filterIngredients(allIngredients))
            } catch (e: Exception) {
                UiState.Error(e.message ?: "ERROR! REFRESH!")
            }
        }
    }

    private fun applyFilters() {
        uiState = UiState.Success(filterIngredients(allIngredients))
    }

    private fun filterIngredients(ingredients: List<Ingredient>): List<Ingredient> {
        return ingredients.filter { ingredient ->
            val matchesQuery = query.isBlank() ||
                    ingredient.name.contains(query.trim(), ignoreCase = true)

            val matchesType = selectedType == "All" ||
                    ingredient.type == selectedType

            val matchesAlcohol = when (alcoholFilter) {
                AlcoholFilter.ALL -> true
                AlcoholFilter.ALCOHOLIC -> ingredient.alcohol == true
                AlcoholFilter.NON_ALCOHOLIC -> ingredient.alcohol == false
            }

            matchesQuery && matchesType && matchesAlcohol
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