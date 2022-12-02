package prj.edu.bytta

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import prj.edu.bytta.innlogging.Login
import prj.edu.bytta.innlogging.LoginViewModel
import prj.edu.bytta.innlogging.LoginScreen
import prj.edu.bytta.main.CommonProgressSpinner

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Content(
                    ByttaViewModel(
                        Firebase.auth,
                        FirebaseFirestore.getInstance(),
                        FirebaseStorage.getInstance()
                    ),
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
    val context = LocalContext.current
    Scaffold(
            topBar = {

            },
        bottomBar = {
            NavBar()
        }
    ) {
        paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Row(modifier = Modifier.align(CenterHorizontally)) {
                Button (
                    content = { Text(text = "New trade")},
                    onClick = {
                        /*loginViewModel.signOut()*/
                        val intent = Intent(context, NewTrade::class.java)
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
            TradeCard(trade = item, storage = vm.storage)
        }
    }
    if (loading) {
        CommonProgressSpinner()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeCard(trade: TradeData, storage: FirebaseStorage) {
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
                    .size(40.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(text = trade.user!!)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = trade.item!!)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = trade.body!!)
                Spacer(modifier = Modifier.height(4.dp))
                Image(
                    painter = rememberImagePainter(
                        data = trade.imageURI,
                        builder = {
                            crossfade(false)
                            placeholder(R.drawable.spinner)
                        }
                    ),
                    contentDescription = "description",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}