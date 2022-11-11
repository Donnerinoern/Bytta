package prj.edu.bytta

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController = navController, viewModel = LoginViewModel())
        }
        composable(Screen.HomeActivity.route) {
            HomeActivity(navController = navController)
        }
    }
}


