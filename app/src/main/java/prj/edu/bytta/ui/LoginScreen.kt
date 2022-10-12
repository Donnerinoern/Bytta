package prj.edu.bytta.ui

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import prj.edu.bytta.SecondActivity
import prj.edu.bytta.R



@Preview
@Composable
fun LoginScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon()
        Email()
        Password()
        SignInButton()
        SignUpButton()
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
fun Email() {
    val emailState = remember { mutableStateOf("") }
    TextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = emailState.value,
        onValueChange = {emailState.value = it},
        label = { Text(text = stringResource(R.string.email_bg))},
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
fun Password() {
    val passwordState = remember { mutableStateOf("") }
    //val showPassword = remember { mutableStateOf(false) }
    TextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = passwordState.value,
        onValueChange = {passwordState.value = it},
        label = { Text(text = stringResource(R.string.password_bg))},
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
      /*  trailingIcon = {
            if (showPassword.value) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Filled.
                    )
                }
            }
        }*/

    )
}

@Composable
fun SignInButton() {
    val context = LocalContext.current
    Button(
        onClick = {
            val intent = Intent(context, SecondActivity::class.java)
            context.startActivity(intent)
            Toast.makeText(context, "Velkommen", Toast.LENGTH_SHORT).show()
        },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.log_in)
        )
    }
}


@Composable
fun SignUpButton() {
    Button(
        onClick = {

        },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.sign_up)
        )
    }
}



