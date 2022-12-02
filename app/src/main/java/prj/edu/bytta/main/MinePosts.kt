package prj.edu.bytta.main


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import prj.edu.bytta.*
import prj.edu.bytta.R
import prj.edu.bytta.innlogging.LoginViewModel
import prj.edu.bytta.ui.theme.ByttaTheme
import prj.edu.bytta.NavBar


class MinePosts : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {

            ByttaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MinePostsScreen(
                        viewModel = LoginViewModel(),
                        vm = ByttaViewModel(
                            auth = Firebase.auth, db = Firebase.firestore, storage = Firebase.storage

                        )
                    )
                }
            }
        }

    }


    @Composable
    fun MinePostsScreen( vm: ByttaViewModel, viewModel: LoginViewModel) {
        val userData = viewModel.userData.value
        val context = LocalContext.current
        val user = Firebase.auth.currentUser



        Column {


            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    ProfileImage(userData?.imageUrl) {

                    }

                    IconButton(
                        modifier = Modifier
                            .weight(weight = 1f, fill = false)
                            .padding(8.dp)
                            .align(Alignment.CenterVertically),

                        onClick = {
                            val intent = Intent(context, SettingsActivity::class.java)
                            context.startActivity(intent)


                        }) {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Edit Details",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }


                }

                Column(modifier = Modifier.padding(8.dp)) {

                    val usernameDisplay =
                        if (user?.displayName == null) "" else "${user?.displayName}"

                    Text(text = usernameDisplay)

                    // user?.displayName?.let { Text(text = it) }
                }


                OutlinedButton(
                    onClick = {
                        val intent = Intent(context, EditProfileActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10)

                ) {
                    Text(text = "Rediger profil")
                }

                CommonDivider()

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Trades list")
                }

                NavBar()

            }
        }

    }
}




@Composable
fun ProfileImage(imageUrl: String?, onClick: () -> Unit) {
    Box(modifier = Modifier
        .padding(top = 16.dp)
        .clickable { onClick.invoke() }) {

        UserImageCard(
            userImage = imageUrl, modifier = Modifier
                .padding(8.dp)
                .size(80.dp)
        )

        Card(
            shape = CircleShape,
            border = BorderStroke(width = 2.dp, color = Color.White),
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.BottomEnd)
                .padding(bottom = 8.dp, end = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = null,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)

            )
        }
    }
}



