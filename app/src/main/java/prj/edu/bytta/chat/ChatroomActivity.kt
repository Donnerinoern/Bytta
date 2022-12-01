package prj.edu.bytta.chat


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import prj.edu.bytta.*

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
                ChatPage()

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
        Search()
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
fun ChatPage(
) {

    Scaffold(
        bottomBar =  {
            NavBar()

        }

    ) {
        paddingValues ->  Column(modifier = Modifier.padding(paddingValues)) {
            Chatroom()
        }
      }
}

