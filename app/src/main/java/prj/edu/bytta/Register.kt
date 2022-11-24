package prj.edu.bytta

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.navArgument
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import prj.edu.bytta.ui.theme.ByttaTheme



@Composable
fun RegisterScreen(viewModel: LoginViewModel, navController: NavController) {

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
        IconReg()
        UserName(viewModel)
        EmailRegister(viewModel)
        PasswordRegister(viewModel)
        ButtonEmailPasswordRegister(viewModel, navController)
        TilbakeKnapp(navController)
    }
}


@Composable
fun IconReg() {
    Image(
        painter = painterResource(R.drawable.ic_banner_foreground2),
        contentDescription = "Logo",
        modifier = Modifier.size(200.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserName(viewModel: LoginViewModel) {
    val userName = viewModel.userName.value
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = userName,
        label = { Text(text = stringResource(R.string.username)) },
        onValueChange = { viewModel.setUserName(it) },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailRegister(viewModel: LoginViewModel) {
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
fun PasswordRegister(viewModel: LoginViewModel) {
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



@SuppressLint("SuspiciousIndentation")
@Composable
fun ButtonEmailPasswordRegister(viewModel: LoginViewModel, navController: NavController) {
    val context = LocalContext.current
    val user = Firebase.auth.currentUser
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = viewModel.isValidEmailAndPassword(),
        content = { Text(text = stringResource(R.string.create)) },
        onClick = { viewModel.createUserWithEmailAndPassword()
            if (user != null) {
                navController.navigate("login_screen") {
                    Toast.makeText(
                        context,
                        "Bruker Opprettet" ,
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.getCurrentUser()
                }
            } else
                viewModel._error.value = "Email allerede i bruk"
            }

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TilbakeKnapp(navController: NavController) {
    TopAppBar(

        title = {

            Text(text = "Tilbake", maxLines = 1,) },

        navigationIcon = {
            IconButton(onClick = {
                navController.navigate("login_screen")
            }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Gå tilbake",
                )
            }
        }
    )
}




