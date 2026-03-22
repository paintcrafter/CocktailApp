package pl.solvro.cocktails.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CocktailListResponse(
    val meta: PaginationMeta,
    val data: List<Cocktail>
)

@Serializable
data class PaginationMeta(
    val total: Int,
    val perPage: Int,
    val currentPage: Int,
    val lastPage: Int,
    val firstPage: Int,
    val firstPageUrl: String? = null,
    val lastPageUrl: String? = null,
    val nextPageUrl: String? = null,
    val previousPageUrl: String? = null
)

@Serializable
data class CocktailDetailsResponse(
    val data: Cocktail
)

@Serializable
data class Cocktail(
    val id: Int,
    val name: String,
    val category: String? = null,
    val glass: String? = null,
    val instructions: String? = null,
    @SerialName("imageUrl") val imageUrl: String? = null,
    val alcoholic: Boolean,
    val ingredients: List<Ingredient> = emptyList()
)

@Serializable
data class Ingredient(
    val id: Int,
    val name: String,
    val description: String? = null,
    val alcohol: Boolean? = null,
    val type: String? = null,
    val percentage: Double? = null,
    @SerialName("imageUrl") val imageUrl: String? = null,
    val measure: String? = null
)