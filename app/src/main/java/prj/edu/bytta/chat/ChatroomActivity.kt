package prj.edu.bytta.chat


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import prj.edu.bytta.*
import prj.edu.bytta.R
import prj.edu.bytta.innlogging.Login
import prj.edu.bytta.innlogging.LoginViewModel
import prj.edu.bytta.main.MinePosts

class ChatroomActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // A surface container using the 'background' color from the theme
                /*Surface(
                    modifier = Modifier.fillMaxSize()
                    //color = MaterialTheme.colorScheme.background
                ) {
                }*/
                ChatPage(viewModel = LoginViewModel())

            }
        }
    }
}
@Composable
fun Chatroom() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 0.85f, fill = true),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            reverseLayout = false
        ) {
            {

            }
        }
        Test()
    }
}
@Composable
fun Test() {
    UserCard(Trade("Morten", "Basskasse", ""))
    UserCard(Trade("Morten", "Basskasse", ""))
    UserCard(Trade("Morten", "Basskasse", ""))
    UserCard(Trade("Morten", "Basskasse", ""))
    UserCard(Trade("Morten", "Basskasse", ""))
    UserCard(Trade("Morten", "Basskasse", ""))
    UserCard(Trade("Morten", "Basskasse", ""))
    UserCard(Trade("Morten", "Basskasse", ""))
    UserCard(Trade("Morten", "Basskasse", ""))
    UserCard(Trade("Morten", "Basskasse", ""))
    UserCard(Trade("Morten", "Basskasse", ""))
    UserCard(Trade("Morten", "Basskasse", ""))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search() {
    var text by remember{ mutableStateOf(TextFieldValue("")) }
    TextField(
        
        value = text,
        shape = RoundedCornerShape(20.dp),
        label = { Text(text = "Søk i meldinger") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        onValueChange = {
            it -> text = it
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatPage(viewModel: LoginViewModel) {
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Home", "Profile", "Messages")
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.Person,
        Icons.Default.Email
    )
    val intents = listOf(
        HomeActivity::class.java,
        MinePosts::class.java,
        ChatroomActivity::class.java
    )
    Scaffold(
        topBar = {
            Button (
                content = { Text(text = stringResource(R.string.loggut))},
                onClick = { viewModel.signOut()
                    val intent = Intent(context, Login::class.java)
                    context.startActivity(intent)}
            )
            Search()
        },
        bottomBar =  {
            BottomAppBar {
                NavigationBar() {

                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = {Icon(imageVector = icons[index], contentDescription = items[index])},
                            label = {Text(item)},
                            selected = selectedItem == index,
                            onClick = { selectedItem = index
                                val intent = Intent(context, intents[index])
                                context.startActivity(intent)}
                        )
                    }
                }
            }
        }
        
    ) {
        paddingValues ->  Column(modifier = Modifier.padding(paddingValues)) {
            Chatroom()
        }
      }
}

