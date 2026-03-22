package pl.solvro.cocktails.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pl.solvro.cocktails.data.network.NetworkModule
import pl.solvro.cocktails.data.repository.CocktailRepository
import pl.solvro.cocktails.ui.screens.CocktailDetailsScreen
import pl.solvro.cocktails.ui.screens.CocktailListScreen
import pl.solvro.cocktails.viewmodel.CocktailDetailsViewModel
import pl.solvro.cocktails.viewmodel.CocktailListViewModel

private sealed class Destinations(val route: String) {
    data object List : Destinations("cocktail_list")
    data object Details : Destinations("cocktail_details/{cocktailId}") {
        fun createRoute(cocktailId: Int) = "cocktail_details/$cocktailId"
    }
}

@Composable
fun CocktailAppNavHost() {
    val navController = rememberNavController()
    val repository = CocktailRepository(NetworkModule.apiService)

    NavHost(
        navController = navController,
        startDestination = Destinations.List.route
    ) {
        composable(Destinations.List.route) {
            val viewModel: CocktailListViewModel = viewModel(
                factory = CocktailListViewModel.factory(repository)
            )
            CocktailListScreen(
                viewModel = viewModel,
                onCocktailClick = { id ->
                    navController.navigate(Destinations.Details.createRoute(id))
                }
            )
        }

        composable(
            route = Destinations.Details.route,
            arguments = listOf(navArgument("cocktailId") { type = NavType.IntType })
        ) { backStackEntry ->
            val cocktailId = requireNotNull(backStackEntry.arguments?.getInt("cocktailId"))
            val viewModel: CocktailDetailsViewModel = viewModel(
                factory = CocktailDetailsViewModel.factory(repository, cocktailId)
            )
            CocktailDetailsScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
