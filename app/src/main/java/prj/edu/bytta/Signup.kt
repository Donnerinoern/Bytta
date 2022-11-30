/*
package prj.edu.bytta


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController




    @Composable
    fun SignupScreen(viewModel: SignupViewmodel, navController: NavController) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .verticalScroll(
                        rememberScrollState()
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val usernameState = remember { mutableStateOf(TextFieldValue()) }
                val emailState = remember { mutableStateOf(TextFieldValue()) }
                val passwordState = remember { mutableStateOf(TextFieldValue()) }

                androidx.compose.material3.Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = null,
                    modifier = Modifier.size(78.dp),
                )


                Text(
                    text = "Registrer deg",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 30.sp,


                    )
                OutlinedTextField(
                    value = usernameState.value,
                    onValueChange = { usernameState.value = it },
                    modifier = Modifier.padding(8.dp),
                    label = { Text(text = "Username") }
                )
                OutlinedTextField(
                    value = emailState.value,
                    onValueChange = { emailState.value = it },
                    modifier = Modifier.padding(8.dp),
                    label = { Text(text = "Email") }
                )
                OutlinedTextField(
                    value = passwordState.value,
                    onValueChange = { passwordState.value = it },
                    modifier = Modifier.padding(8.dp),
                    label = { Text(text = "Password") },
                    visualTransformation = PasswordVisualTransformation()
                )
                Button(
                    onClick = {
                        viewModel.onSignup(
                            usernameState.value.text,
                            emailState.value.text,
                            passwordState.value.text,
                            )
                        navController.navigate("login_screen")


                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Registrer deg")
                }
                Text(text = "Har du allerede en bruker? gå til innlogging ->",
                    color = Color.Blue,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { }
                )


            }
        }


    }


*/
