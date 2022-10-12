package prj.edu.bytta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // A surface container using the 'background' color from the theme
                /*Surface(
                    modifier = Modifier.fillMaxSize()
                    //color = MaterialTheme.colorScheme.background
                ) {*/
                //}
                Content()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun Content() {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Home", "Profile", "Messages")
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.Person,
        Icons.Default.Email
        )
    Scaffold(
        bottomBar = {
            BottomAppBar {
                NavigationBar() {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = {Icon(imageVector = icons[index], contentDescription = items[index])},
                            label = {Text(item)},
                            selected = selectedItem == index,
                            onClick = { selectedItem = index }
                        )
                    }
                }
            }
        }
    ) {
        paddingValues -> Greeting(
            name = "Donnan",
            modifier = Modifier.padding(paddingValues, )
        ) {
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier, function: () -> Unit) {
    Text(text = "Hello $name!")
}