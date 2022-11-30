package prj.edu.bytta

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase







@SuppressLint("SuspiciousIndentation")
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
         composable("home_screen", content = { Content(
            navController = navController,
            vm = ByttaViewModel(Firebase.auth, FirebaseFirestore.getInstance()),
            viewModel = LoginViewModel()
        )
        })


})
}



