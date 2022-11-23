package prj.edu.bytta

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore




@Composable
fun Navigation(){
    var navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login_screen", builder = {

        composable("login_screen", content = { LoginScreen(
            navController = navController,
            viewModel = LoginViewModel()
        )
        })
        composable("register_screen", content = { RegisterScreen(
            navController = navController,
            viewModel = LoginViewModel()
        )
        })
        composable("tilbake_knapp", content = { TilbakeKnapp(
            navController = navController
        )
        })
  /*      composable("home_screen", content = { Content(
            navController = navController,
            viewModel = LoginViewModel(),

        )
        })
*/
})
}



