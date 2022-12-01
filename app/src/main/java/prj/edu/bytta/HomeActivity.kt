package prj.edu.bytta

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import prj.edu.bytta.Chat.ChatroomActivity
import prj.edu.bytta.innlogging.LoginViewModel
import prj.edu.bytta.main.CommonProgressSpinner

class HomeActivity : ComponentActivity() {
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
                Content(
                    ByttaViewModel(
                        Firebase.auth,
                        FirebaseFirestore.getInstance()),
                    viewModel = LoginViewModel(),
                    navController = NavController(context = LocalContext.current))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(
    vm: ByttaViewModel,
    viewModel: LoginViewModel,
    navController: NavController,
) {

    val tradeDataLoading = vm.inProgress.value
    val trades = vm.trades.value
    val tradeFeedLoading = vm.tradesFeedProgress
    val user = Firebase.auth.currentUser
    Scaffold(
        topBar = {
            Button (
                content = { Text(text = stringResource(R.string.loggut))},
                onClick = { loginViewModel.signOut()
                    viewModel.signOut()
                    navController.navigate("login_screen") }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(db: FirebaseFirestore, trade: Trade, viewModel: LoginViewModel) {
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
                     val intent = Intent(context, Login::class.java)
                     context.startActivity(intent)}
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
            )
        },
        bottomBar = {
            NavBar()
        }
    ) {
        paddingValues ->
        TradesList(tradeList = trades, loading = tradeDataLoading, vm = vm, modifier = Modifier.padding(paddingValues))
    }
}

@Composable
fun TradesList(tradeList: List<TradeData>, loading: Boolean, vm: ByttaViewModel, modifier: Modifier) {
    LazyColumn {
        items(items = tradeList) {  
            item -> 
            TradeCard(trade = item)
        }
    }
    if (loading) {
        CommonProgressSpinner()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeCard(trade: TradeData) {
    Spacer(modifier = Modifier.height(5.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = {/*TODO*/}
    ) {
        Row(modifier = Modifier.padding(all = 8.dp)) {
            Image(
                painter = painterResource(R.drawable.ic_user),
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
                //Text(text = trade.user!!)
                // Add a vertical space between the author and message texts
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = trade.item!!)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = trade.body!!)
            }
        }
    }
}