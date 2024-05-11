package co.storibank.presentation.ui.screens.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import co.storibank.presentation.ui.screens.home.HomeScreen
import co.storibank.presentation.ui.screens.home.MovementDetailScreen
import co.storibank.presentation.ui.screens.login.LoginScreen
import co.storibank.presentation.ui.screens.registration.RegistrationScreen
import co.storibank.presentation.viewmodel.HomeViewModel
import co.storibank.presentation.viewmodel.LoginViewModel
import co.storibank.presentation.viewmodel.RegistrationViewModel
import co.storibank.presentation.viewmodel.state.LoginState

@Composable
fun Stori(
    loginViewModel: LoginViewModel,
    registrationViewModel: RegistrationViewModel,
    homeViewModel: HomeViewModel,
) {
    val navController = rememberNavController()
    SetupNavigation()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            ShowLoginScreen(navController, loginViewModel)
        }
        composable("registration") {
            RegistrationScreen(
                viewModel = registrationViewModel,
            )
        }
        composable("home") {
            ShowHomeScreen(navController, loginViewModel, homeViewModel)
        }
        composable(
            route = "movementDetail/{movementId}",
            arguments = listOf(navArgument("movementId") { type = NavType.StringType }),
        ) { backStackEntry ->
            ShowMovementDetailScreen(
                navController,
                loginViewModel,
                homeViewModel,
                backStackEntry,
            )
        }
    }
}

@Composable
fun SetupNavigation(): NavHostController {
    val navController = rememberNavController()
    return navController
}

@Composable
fun ShowLoginScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
) {
    val loginState by loginViewModel.loginState.collectAsState()

    if (loginState is LoginState.Success) {
        navController.navigate("home")
    } else {
        LoginScreen(
            viewModel = loginViewModel,
            navigateToRegistration = { navController.navigate("registration") },
            navigateToHome = { navController.navigate("home") },
        )
    }
}

@Composable
fun ShowHomeScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    homeViewModel: HomeViewModel,
) {
    val loginState by loginViewModel.loginState.collectAsState()
    if (loginState is LoginState.Success) {
        HomeScreen(
            viewModel = homeViewModel,
            navigateToMovementDetail = { movementId ->
                navController.navigate("movementDetail/$movementId")
            },
        )
    }
}

@Composable
fun ShowMovementDetailScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    homeViewModel: HomeViewModel,
    backStackEntry: NavBackStackEntry,
) {
    val loginState by loginViewModel.loginState.collectAsState()
    if (loginState is LoginState.Success) {
        val movementId = backStackEntry.arguments?.getString("movementId")
        val movement = homeViewModel.findMovementById(movementId ?: "")
        MovementDetailScreen(
            movement = movement,
            navigateBack = { navController.popBackStack() },
        )
    }
}
