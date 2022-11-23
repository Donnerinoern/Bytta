package prj.edu.bytta

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val db = Firebase.firestore
        var trade: Trade = Trade("error", "error", "error")
        db.collection("trades")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    trade = Trade(document.get("user") as String, document.get("item") as String, document.get("body") as String)
                    println(document.get("user"))
                }
            }
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // A surface container using the 'background' color from the theme
                /*Surface(
                    modifier = Modifier.fillMaxSize()
                    //color = MaterialTheme.colorScheme.background
                ) {
                }*/
                Content(db, trade, viewModel = LoginViewModel(), navController = NavController(context = LocalContext.current))
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(db: FirebaseFirestore, trade: Trade, viewModel: LoginViewModel, navController: NavController) {
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
        ProfileActivity::class.java,
        MessageActivity::class.java
    )
    db.collection("trades")
        .get()
    Scaffold(
    topBar = {
             Button (
                 content = { Text(text = stringResource(R.string.loggut))},
                 onClick = { viewModel.signOut()
                     val intent = Intent(context, SignIn::class.java)
                     context.startActivity(intent)
                 }
                     )
    },
        bottomBar = {
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
        paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TradeCard(Trade("Morten", "Basskasse LOLZ", "Prøver å bytte en basskasse shamener. Ønsker ikke noe spesifikt bare kom med tilbud \uD83D\uDE1C"))
            TradeCard(Trade("Petter Northug", "Ski", "Selger skiene mine :=)"))
            TradeCard(trade)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeCard(trade: Trade) {
    Spacer(modifier = Modifier.height(5.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = {/*TODO*/}
    ) {
        Row(modifier = Modifier.padding(all = 8.dp)) {
            Image(
                painter = painterResource(R.drawable.petter_northug_2018_scanpix),
                contentDescription = "Contact profile picture",
                modifier = Modifier
                    // Set image size to 40 dp
                    .size(40.dp)
                    // Clip image to be shaped as a circle
                    .clip(CircleShape)
            )

            // Add a horizontal space between the image and the column
            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(text = trade.user)
                // Add a vertical space between the author and message texts
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = trade.item)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = trade.body)
            }
        }
    }
}