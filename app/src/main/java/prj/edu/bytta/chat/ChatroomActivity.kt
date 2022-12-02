package prj.edu.bytta.chat


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import prj.edu.bytta.*
import prj.edu.bytta.home.Trade

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
        Test()
    }
}
@Composable
fun Test() {
    UserCard(Trade("Morten", "Basskasse", ""))
    UserCard(Trade("Kurt", "Basskassehus", ""))
    UserCard(Trade("Martin", "MetaQuest2", ""))
    UserCard(Trade("Kalvin", "3Smashplater 190g", ""))

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

