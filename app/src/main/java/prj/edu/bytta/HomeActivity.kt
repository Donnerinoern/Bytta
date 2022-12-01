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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
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
                Content(ByttaViewModel(Firebase.auth, FirebaseFirestore.getInstance()), LoginViewModel())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(vm: ByttaViewModel, loginViewModel: LoginViewModel) {
    val tradeDataLoading = vm.inProgress.value
    val trades = vm.trades.value
    val tradeFeedLoading = vm.tradesFeedProgress
    val context = LocalContext.current
    Scaffold(
        topBar = {

        },
        bottomBar = {
            NavBar()
        }
    ) {
        paddingValues ->
        Column() {
            Row() {
                Button (
                    content = { Text(text = "New trade")},
                    onClick = {
                        /*loginViewModel.signOut()*/
                        val intent = Intent(context, NewTrade::class.java)
                        context.startActivity(intent)
                    }
                )
                Button (
                    content = { Text(text = "Log out")},
                    onClick = {
                        loginViewModel.signOut()
                        val intent = Intent(context, Login::class.java)
                        context.startActivity(intent)
                    }
                )
            }
            TradesList(tradeList = trades, loading = tradeDataLoading, vm = vm, modifier = Modifier.padding(paddingValues))
        }
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