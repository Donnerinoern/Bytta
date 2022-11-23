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
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import prj.edu.bytta.ui.theme.ByttaTheme
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext



class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ByttaTheme {
                // A surface container using the 'background' color from the theme
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                    TopAppbarProfile(context = LocalContext.current.applicationContext)
                    ProfileCard()
                }
            }
        }
    }
}
private val optionsList: ArrayList<OptionsData> = ArrayList()




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppbarProfile(context: Context) {
    val context = LocalContext.current
    TopAppBar(



        title = {
            Text(text = "Profil", maxLines = 1,) },




        navigationIcon = {
            IconButton(onClick = {
                val intent = Intent(context, HomeActivity::class.java)
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

@Composable
fun ProfileCard(context: Context = LocalContext.current.applicationContext) {
    var listPrepared by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            optionsList.clear()

            // Add the data to optionsList
            prepareOptionsData()

            listPrepared = true
        }
    }

    if (listPrepared) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {

            item {
                // User's image, name, email and edit button
                UserDetails( viewModel = LoginViewModel())

            }

            // Show the options
            items(optionsList) { item ->
                OptionsItemStyle(item = item, context = context)
            }


        }
    }
}

    @Composable
    fun UserDetails( viewModel: LoginViewModel) {
        val user = Firebase.auth.currentUser


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



            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    modifier = Modifier
                        .wrapContentSize()
                        .size(32.dp),

                    painter = painterResource(id = R.drawable.ic_user),
                    contentDescription = "Ditt profilbilde",


                    )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(weight = 3f, fill = false)
                            .padding(start = 16.dp)

                    ) {

                        // User's name
                        Text(
                            text = "Kristian",
                            style = TextStyle(
                                letterSpacing = (0.8).sp
                            ),
                            maxLines = 1,

                            )


                        Spacer(modifier = Modifier.height(2.dp))


                        if (user != null) {
                            user.email?.let {
                                Text(
                                    text = it,
                                    style = TextStyle(
                                        letterSpacing = (0.8).sp
                                    ),
                                    maxLines = 1,

                                    )
                            }
                        }


                }

                val context = LocalContext.current
                // Edit button
                IconButton(
                    modifier = Modifier
                        .weight(weight = 1f, fill = false),
                    onClick = {
                        val intent = Intent(context, EditProfileActivity::class.java)
                        context.startActivity(intent)


                    }) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit Details",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            }
        }




@Composable
private fun OptionsItemStyle(item: OptionsData, context: Context) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = true) {
                Toast
                    .makeText(context, item.title, Toast.LENGTH_SHORT)
                    .show()
            }
            .padding(all = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Icon
        Icon(
            modifier = Modifier
                .size(32.dp),
            imageVector = item.icon,
            contentDescription = item.title,
            tint = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(weight = 3f, fill = false)
                    .padding(start = 16.dp)
            ) {

                // Title
                Text(
                    text = item.title,
                    style = TextStyle(
                        
                    )
                )

                Spacer(modifier = Modifier
                    .height(2.dp)
                    .background(color = Color.Red))

                // Sub title
                Text(
                    text = item.subTitle,
                    style = TextStyle(
                       
                    )
                )

            }

            // Right arrow icon
            Icon(
                modifier = Modifier
                    .weight(weight = 1f, fill = false),
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = item.title,

            )
        }

    }
}

private fun prepareOptionsData() {

    val appIcons = Icons.Outlined

    optionsList.add(
        OptionsData(
            icon = appIcons.Person,
            title = "Konto",
            subTitle = "",
        )
    )



    optionsList.add(
        OptionsData(
            icon = appIcons.ShoppingCart,
            title = "Dine byttehandler",
            subTitle = "Se alle dine byttehandler"
        )
    )


    optionsList.add(
        OptionsData(
            icon = appIcons.Settings,
            title = "Innstillinger",
            subTitle = "Tilpass dine innstillinger"

        )
    )



    optionsList.add(
        OptionsData(
            icon = appIcons.Star,
            title = "Favoritter",
            subTitle = "Se hvilke byttehandler du har likt"
        )
    )
}

data class OptionsData(val icon: ImageVector, val title: String, val subTitle: String)


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ByttaTheme {
        TopAppbarProfile(context = LocalContext.current.applicationContext)
        ProfileCard()
    }
}

