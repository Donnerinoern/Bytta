package prj.edu.bytta

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import prj.edu.bytta.innlogging.LoginViewModel
import prj.edu.bytta.main.CommonDivider
import prj.edu.bytta.main.CommonImage
import prj.edu.bytta.main.CommonProgressSpinner
import prj.edu.bytta.main.MinePosts
import prj.edu.bytta.ui.theme.ByttaTheme


class EditProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ByttaTheme {
                // A surface container using the 'background' color from the theme
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {

                    ProfileScreen(
                        viewModel = LoginViewModel(

                        )
                    )

                }
            }
        }
    }
}

@Composable
fun ProfileScreen(viewModel: LoginViewModel ) {
    val isLoading = viewModel.inProgress.value
    if (isLoading) {
        CommonProgressSpinner()
    } else {
        val userData = viewModel.userData.value
        var username by rememberSaveable { mutableStateOf(userData?.username ?: "") }

        val context = LocalContext.current
        ProfileContent(
            viewModel = LoginViewModel(

            ),
            username = username,
            onUsernameChange = { username = it },
            onSave = { viewModel.updateProfile() },
            onBack = {
                val intent = Intent(context, MinePosts::class.java)
                context.startActivity(intent)
            },
            onLogout = {}
        )


    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    viewModel: LoginViewModel,
    username: String,
    onUsernameChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    onLogout: () -> Unit

) {
    val scrollState = rememberScrollState()
    val imageUrl = viewModel.userData.value?.imageUrl
    val userName = viewModel.userName.value

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Tilbake", modifier = Modifier.clickable { onBack.invoke() })
            Text(text = "Lagre", modifier = Modifier.clickable { onSave.invoke() })
        }

        CommonDivider()

        ProfileImage(imageUrl = imageUrl, viewModel = LoginViewModel())

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Brukernavn", modifier = Modifier.width(100.dp))
            OutlinedTextField(
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.Center

        ) {
            Text(text = "Logg ut", modifier = Modifier.clickable { onLogout.invoke() })
        }
    }
}


@Composable
fun ProfileImage(imageUrl: String?, viewModel: LoginViewModel) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ){uri: Uri? ->

        uri?.let {  }
    }

    Box(modifier = Modifier.height(IntrinsicSize.Min)) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable { launcher.launch("image/*") },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = CircleShape,
                modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp)
            ) {
                CommonImage(data = imageUrl)
            }
            Text(text = "Endre profilbilde")
        }

        val isLoading = viewModel.inProgress.value
        if (isLoading)
            CommonProgressSpinner()
    }
}