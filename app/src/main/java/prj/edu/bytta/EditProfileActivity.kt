package prj.edu.bytta

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import prj.edu.bytta.ui.theme.ByttaTheme


class EditProfileActivity : ComponentActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ByttaTheme {
                // A surface container using the 'background' color from the theme
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                    TopAppbarEditProfile(context = LocalContext.current.applicationContext)
                    EditProfilePage(viewModel = ProfileViewmodel())

                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppbarEditProfile(context: Context) {
    val context = LocalContext.current
    TopAppBar(

        title = {

            Text(text = "Rediger profil", maxLines = 1,) },

        navigationIcon = {
            IconButton(onClick = {
                val intent = Intent(context, ProfileActivity::class.java)
                context.startActivity(intent)
            }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Gå tilbake",
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfilePage(context: Context = LocalContext.current.applicationContext, viewModel: ProfileViewmodel) {
    val user = Firebase.auth.currentUser
    val email = viewModel._userEmail.value

    ProfileImage(context = context)
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize(),

        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        var text by remember { mutableStateOf("") }
        OutlinedTextField(
            value = text,
            onValueChange = { text = it
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType
                = KeyboardType.Email
            ),

            label = {
                Text(text = "Email")
            }
        )
/*
        OutlinedTextField(
            value = userInputText,
            onValueChange = {newText ->
                userInputText = newText
            },
            label = {
                Text(text = "Passord")
            }
        )
*/
        Button(onClick = {
            viewModel.updateEmail(email)

            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()

        }
        ) {
            Text(text = "Lagre endringer")
        }



    }
}



@Composable
fun ProfileImage(context: Context) {
    val imageUri = rememberSaveable { mutableStateOf("") }
    val painter = rememberAsyncImagePainter(
        if (imageUri.value.isEmpty())
            R.drawable.ic_user
        else
            imageUri.value
    )
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imageUri.value = it.toString() }
    }

    Column (
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card (
            shape = CircleShape,
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
                ){
            Image(painter = painter,
                  contentDescription = "Profilbilde",
                  modifier = Modifier
                      .wrapContentSize()
                      .clickable { launcher.launch("image/*") },
                  contentScale = ContentScale.Crop

            )
        }

        Text(text = "Endre profilbilde")

    }



}

