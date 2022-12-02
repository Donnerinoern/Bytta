package prj.edu.bytta

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import prj.edu.bytta.main.CommonImage
import prj.edu.bytta.main.NotificationMessage
import prj.edu.bytta.navigering.Navigation
import prj.edu.bytta.ui.theme.ByttaTheme

class NewTrade: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ByttaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Navigation()
                    NewTradeContent(
                        NavController(LocalContext.current),
                        ByttaViewModel(
                            FirebaseAuth.getInstance(),
                            FirebaseFirestore.getInstance(),
                            FirebaseStorage.getInstance())
                        )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTradeContent(navController: NavController, vm: ByttaViewModel) {
    var itemDesc by remember{ mutableStateOf(TextFieldValue("")) }
    var itemText by remember{ mutableStateOf(TextFieldValue("")) }

    Column {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
        ){ uri: Uri? ->

            uri?.let { vm.uploadTrade(uri, itemDesc.text, itemText.text) }
        }

        Box(modifier = Modifier.height(IntrinsicSize.Min)) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Item name")
                TextField(value = itemText, onValueChange = {itemText = it})

                Text(text = "Item description")
                TextField(value = itemDesc, onValueChange = {itemDesc = it})

                Card(
                    shape = RectangleShape,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(100.dp)
                ) {
                }
                Button(onClick = {launcher.launch("image/*")}, content = {Text("Upload image and save trade")})
            }
        }
        NavBar()
    }

}








