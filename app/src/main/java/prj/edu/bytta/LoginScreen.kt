package prj.edu.bytta

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import prj.edu.bytta.ui.theme.ByttaTheme
import com.google.android.gms.tasks.Task as Task1


class Login: ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            ByttaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(viewModel = LoginViewModel())
                }
            }
        }

    }
}

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (viewModel.error.value.isNotBlank()) {
            ErrorField(viewModel)
        }
        Icon()
        EmailField(viewModel)
        PasswordField(viewModel)
        ButtonEmailPasswordLogin(viewModel)
        ButtonEmailPasswordCreate(viewModel)
    }
}
@Composable
fun Icon() {
    Icon(
        imageVector = Icons.Rounded.Person,
        contentDescription = null,
        modifier = Modifier.size(78.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailField(viewModel: LoginViewModel) {
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
fun PasswordField(viewModel: LoginViewModel) {
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
fun ButtonEmailPasswordLogin(viewModel: LoginViewModel) {
    val context = LocalContext.current

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = viewModel.isValidEmailAndPassword(),
        content = { Text(text = stringResource(R.string.login)) },
        onClick = {
            try {
                viewModel.signInWithEmailAndPassword()
                if (viewModel._userEmail == viewModel.userEmail || viewModel._password == viewModel.password) {
                    val intent = Intent(context, HomeActivity::class.java)
                    context.startActivity(intent)
                    Log.d(ContentValues.TAG, "SignInWithEmail:success")
                } else {
                    viewModel._error.value = "Unknown error"
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "SignInWithEmail:failure")
                }
            } catch (e: Exception) {
                viewModel._error.value = e.localizedMessage ?: "Unknown error"
                Log.d(ContentValues.TAG, "Sign in fail: $e")
            }
                }
             )
        }

@Composable
fun ButtonEmailPasswordCreate(viewModel: LoginViewModel) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = viewModel.isValidEmailAndPassword(),
        content = { Text(text = stringResource(R.string.create)) },
        onClick = { viewModel.createUserWithEmailAndPassword() }
    )
}

@Composable
fun ErrorField(viewModel: LoginViewModel) {
    Text(
        text = viewModel.error.value,
        modifier = Modifier.fillMaxWidth(),
        color = Color.Red,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
}





