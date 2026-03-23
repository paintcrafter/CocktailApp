package pl.solvro.cocktails.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pl.solvro.cocktails.data.network.NetworkModule
import pl.solvro.cocktails.data.repository.CocktailRepository
import pl.solvro.cocktails.ui.screens.CocktailDetailsScreen
import pl.solvro.cocktails.ui.screens.CocktailListScreen
import pl.solvro.cocktails.ui.screens.IngredientDetailsScreen
import pl.solvro.cocktails.ui.screens.IngredientListScreen
import pl.solvro.cocktails.viewmodel.CocktailDetailsViewModel
import pl.solvro.cocktails.viewmodel.CocktailListViewModel
import pl.solvro.cocktails.viewmodel.IngredientDetailsViewModel
import pl.solvro.cocktails.viewmodel.IngredientListViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel


sealed class Destinations(val route: String) {
    data object CocktailList : Destinations("cocktail_list")
    data object CocktailDetails : Destinations("cocktail_details/{cocktailId}") {
        fun createRoute(cocktailId: Int) = "cocktail_details/$cocktailId"
    }

    data object IngredientList : Destinations("ingredient_list")
    data object IngredientDetails : Destinations("ingredient_details/{ingredientId}") {
        fun createRoute(ingredientId: Int) = "ingredient_details/$ingredientId"
    }
}

@Composable
fun CocktailAppNavHost(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val repository = CocktailRepository(NetworkModule.apiService)

    val cocktailListViewModel: CocktailListViewModel = viewModel(
        factory = CocktailListViewModel.factory(repository)
    )

    val ingredientListViewModel: IngredientListViewModel = viewModel(
        factory = IngredientListViewModel.factory(repository)
    )

    val bottomNavItems = listOf(
        BottomNavItem.Cocktails,
        BottomNavItem.Ingredients
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.route in listOf(
        Destinations.CocktailList.route,
        Destinations.IngredientList.route
    )

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                when (item.route) {
                                    Destinations.CocktailList.route -> {
                                        cocktailListViewModel.resetFilters()
                                    }
                                    Destinations.IngredientList.route -> {
                                        ingredientListViewModel.resetFilters()
                                    }
                                }

                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title
                                )
                            },
                            label = {
                                Text(item.title)
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = Destinations.CocktailList.route
            ) {
                composable(Destinations.CocktailList.route) {
                    CocktailListScreen(
                        viewModel = cocktailListViewModel,
                        onCocktailClick = { cocktailId ->
                            navController.navigate(
                                Destinations.CocktailDetails.createRoute(cocktailId)
                            )
                        }
                    )
                }

                composable(
                    route = Destinations.CocktailDetails.route,
                    arguments = listOf(navArgument("cocktailId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val cocktailId =
                        backStackEntry.arguments?.getInt("cocktailId") ?: return@composable

                    val viewModel: CocktailDetailsViewModel = viewModel(
                        factory = CocktailDetailsViewModel.factory(repository, cocktailId)
                    )

                    CocktailDetailsScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable(Destinations.IngredientList.route) {
                    IngredientListScreen(
                        viewModel = ingredientListViewModel,
                        onIngredientClick = { ingredientId ->
                            navController.navigate(
                                Destinations.IngredientDetails.createRoute(ingredientId)
                            )
                        }
                    )
                }

                composable(
                    route = Destinations.IngredientDetails.route,
                    arguments = listOf(navArgument("ingredientId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val ingredientId =
                        backStackEntry.arguments?.getInt("ingredientId") ?: return@composable

                    val viewModel: IngredientDetailsViewModel = viewModel(
                        factory = IngredientDetailsViewModel.factory(repository, ingredientId)
                    )

                    IngredientDetailsScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}