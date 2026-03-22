package pl.solvro.cocktails.data.network

import pl.solvro.cocktails.data.model.CocktailDetailsResponse
import pl.solvro.cocktails.data.model.CocktailListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CocktailApiService {
    @GET("api/v1/cocktails")
    suspend fun getCocktails(
        @Query("page") page: Int = 1,
        @Query("perPage") perPage: Int = 100,
        @Query("name") name: String? = null
    ): CocktailListResponse

    @GET("api/v1/cocktails/{id}")
    suspend fun getCocktailDetails(
        @Path("id") id: Int
    ): CocktailDetailsResponse
}
