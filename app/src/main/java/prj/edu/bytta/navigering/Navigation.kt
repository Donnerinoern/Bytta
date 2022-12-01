package prj.edu.bytta.navigering

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import prj.edu.bytta.*
import prj.edu.bytta.innlogging.LoginScreen
import prj.edu.bytta.innlogging.LoginViewModel
import prj.edu.bytta.innlogging.RegisterScreen
import prj.edu.bytta.innlogging.TilbakeKnapp


@Composable
fun Navigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login_screen",
        builder = {

        composable(
            "login_screen", content = { LoginScreen(
            navController = navController,
            viewModel = LoginViewModel()
        )
        })
        composable(
            "register_screen", content = { RegisterScreen(
            navController = navController,
            viewModel = LoginViewModel()
        )
        })
        composable(
            "tilbake_knapp", content = { TilbakeKnapp(
            navController = navController
        )
        })
         composable(
             "home_screen", content = { Content(
            navController = navController,
            vm = ByttaViewModel(Firebase.auth, FirebaseFirestore.getInstance()),
            viewModel = LoginViewModel()

        )
        })

})
}


