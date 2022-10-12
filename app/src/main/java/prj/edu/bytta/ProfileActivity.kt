package prj.edu.bytta

import android.content.Intent
import android.graphics.Paint.Align
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.C
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import prj.edu.bytta.ui.theme.ByttaTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import prj.edu.bytta.ui.theme.Purple80

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ByttaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                    ProfileScreen()

                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(){

    val notification = rememberSaveable { mutableStateOf("")}
     if (notification.value.isNotEmpty()){
         Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
         notification.value = ""
     }

    var username by rememberSaveable { mutableStateOf("default username") }
    var eMail by rememberSaveable{ mutableStateOf("default e-mail")}

    
    Column(
        modifier = Modifier

            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val context = LocalContext.current
            Button(onClick = {
                val intent = Intent(context, HomeActivity::class.java)
                context.startActivity(intent)}
            ) {
                Text(text = "Tilbake")
            }
            Button(onClick = { notification.value = "Profil oppdatert" }) {
                Text(text = "Rediger profil")
            }
        }

        ProfileImage()

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Brukernavn", modifier = Modifier.width(180.dp) )

            TextField(value = username, onValueChange = {username = it},
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black
            )  )
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "E-Mail", modifier = Modifier.width(180.dp) )

            TextField(value = eMail, onValueChange = {eMail = it},
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black
                )  )
        }

            hola()


        

        val imageUri = rememberSaveable  { mutableStateOf("")}

    }

}

@Composable
fun ProfileImage(){
    val imageUri = rememberSaveable  { mutableStateOf("")}
    val painter = rememberAsyncImagePainter(
        if (imageUri.value.isEmpty())
            R.drawable.petter_northug_2018_scanpix
    else
        imageUri.value
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ){ uri: Uri? ->
        uri?.let { imageUri.value = it.toString() }
    }


    Column(

        modifier = Modifier

            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

       Card(
           shape = CircleShape,

           modifier = Modifier
               .padding(8.dp)
               .size(100.dp)
       ) {
           Image(
               painter = painter,
               contentDescription = null,
               modifier = Modifier
                   .wrapContentSize()
                   .clickable { launcher.launch("image/*") },
               contentScale = ContentScale.Crop
               )
          }
        Text(text = "Endre profilbilde")
    }
}

@Composable
fun hola() {

    Column( modifier = Modifier

        .padding(16.dp)
        .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { /*TODO*/ }

            ) {
                Text(text = "Ny byttehandel")

            }

            Button(onClick = { /*TODO*/ }

            ) {
                Text(text = "Dine favoritter")

            }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
          ByttaTheme {
        ProfileScreen()
    }
}

