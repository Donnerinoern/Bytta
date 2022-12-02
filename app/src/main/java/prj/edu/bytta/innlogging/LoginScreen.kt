package prj.edu.bytta.innlogging


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import prj.edu.bytta.R
import prj.edu.bytta.ui.theme.ByttaTheme



class Login: ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            ByttaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(
                        viewModel = LoginViewModel(),
                        navController = NavController(context = LocalContext.current))
                }
            }
        }

    }
}

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navController: NavController) {

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (viewModel.error.value.isNotBlank()) {
            viewModel.ErrorField()
        }
        Icon()
        EmailField(viewModel)
        PasswordField(viewModel)
        ButtonEmailPasswordLogin(viewModel, navController)
        ButtonEmailPasswordCreate(navController)
        LogInWithGoogle()
        LoggUtKnapp(viewModel)

    }
}
@Composable
fun Icon() {
    Image(
        painter = painterResource(R.drawable.ic_banner_foreground2),
        contentDescription = "Logo",
        modifier = Modifier.size(200.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailField(
    viewModel: LoginViewModel) {

    val userEmail = viewModel.userEmail.value
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = userEmail,
        label = { Text(text = stringResource(R.string.email)) },
        onValueChange = { viewModel.setUserEmail(it) },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(
    viewModel: LoginViewModel) {

    val password = viewModel.password.value
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
        value = password,
        label = { Text(text = stringResource(R.string.password)) },
        onValueChange = { viewModel.setPassword(it) },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
    ),
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}

@Composable
fun ButtonEmailPasswordLogin(
    viewModel: LoginViewModel,
    navController: NavController) {
    val user = Firebase.auth.currentUser
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = viewModel.isValidEmailAndPassword(),
        content = { Text(text = stringResource(R.string.login)) },
        onClick = {
            // Henter logg inn med bruker funksjonen fra LoginViewModel
            // Dersom det er en match med uid og brukeren som
            // eksisterer i Firebase vil man bli sendt
            // til homescreen (HomeActivity)
            viewModel.signInWithEmailAndPassword()
            if (user != null) {
                navController.navigate("home_screen")
                viewModel.getCurrentUser()
                println("lol")

            } else {
                viewModel._error.value = "Kunne ikke logge inn"
                println("lel")
            }
        }
    )
}

@Composable
fun ButtonEmailPasswordCreate(navController: NavController) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            content = { Text(text = stringResource(R.string.create)) },
            onClick = { navController.navigate("register_screen") }

        )

}

@Composable
fun LogInWithGoogle() {
    val context = LocalContext.current
    Button (
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        content = { Text(text = stringResource(R.string.logingoogle))},
        onClick = { val intent = Intent(context, SignIn::class.java)
            context.startActivity(intent)

        }
            )
}
@Composable
fun LoggUtKnapp(
    viewModel: LoginViewModel
) {
    Button (
        content = { Text(text = stringResource(R.string.loggut))},
        onClick = { viewModel.signOut()
                    viewModel.getCurrentUser()
        }

    )
}










