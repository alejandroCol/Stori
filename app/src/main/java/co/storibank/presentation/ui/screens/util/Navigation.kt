package co.storibank.presentation.ui.screens.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    val navController = SetupNavigation()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
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
        composable("registration") {
            RegistrationScreen(
                viewModel = registrationViewModel,
            )
        }
        composable("home") {
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
        composable(
            route = "movementDetail/{movementId}",
            arguments = listOf(navArgument("movementId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val loginState by loginViewModel.loginState.collectAsState()
            if (loginState is LoginState.Success) {
                val movementId = backStackEntry.arguments?.getString("movementId")
                val movement = homeViewModel.getMovementById(movementId ?: "")
                MovementDetailScreen(
                    movement = movement,
                    navigateBack = { navController.popBackStack() },
                )
            }
        }
    }
}

@Composable
fun SetupNavigation(): NavHostController {
    val navController = rememberNavController()
    return navController
}
