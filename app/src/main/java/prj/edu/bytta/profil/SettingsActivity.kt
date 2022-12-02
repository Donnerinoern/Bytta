package prj.edu.bytta.profil

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import prj.edu.bytta.ui.theme.ByttaTheme


class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            ByttaTheme {
                // A surface container using the 'background' color from the theme
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    
                    ) 
                
                {
                    TopAppbarSettings()
                    SettingScreen()
                }

            }
        }
    }
}


@Composable
fun SettingScreen(){

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 24.dp)
            .fillMaxSize()
    ) {

        innStillinger( context = LocalContext.current)
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppbarSettings() {
    val context = LocalContext.current
    TopAppBar(
        title = {
            Text(text = "Innstillinger", maxLines = 1,) },
        navigationIcon = {
            IconButton(onClick = {
                val intent = Intent(context, MinePosts::class.java)
                context.startActivity(intent) }) {
                androidx.compose.material3.Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Gå tilbake",
                )
            }
        }
    )
}

@Composable
fun innStillinger(
    context: Context
){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Button(onClick = {
            val i = Intent(ACTION_WIRELESS_SETTINGS)
            context.startActivity(i)
        }, modifier = Modifier.width(300.dp)) {
            Text(text = "Tilkobling og delingsinnstillinger", color = Color.White)
        }
    
        CommonDivider()

        Button(onClick = {
            val i = Intent(ACTION_WIFI_SETTINGS)
            context.startActivity(i)
        }, modifier = Modifier.width(300.dp)) {
            Text(text = "Nettverks innstillinger", color = Color.White)
        }

        CommonDivider()

        Button(onClick = {
            val i = Intent(ACTION_INPUT_METHOD_SETTINGS)
            context.startActivity(i)
        }, modifier = Modifier.width(300.dp)) {
            Text(text = "Inndatametode innstillinger", color = Color.White)
        }

        CommonDivider()

        Button(onClick = {
            val i = Intent(ACTION_DISPLAY_SETTINGS)
            context.startActivity(i)
        }, modifier = Modifier.width(300.dp)) {
            Text(text = "Skjerminnstillinger", color = Color.White)
        }

        CommonDivider()

        Button(onClick = {
            val i = Intent(ACTION_LOCATION_SOURCE_SETTINGS)
            context.startActivity(i)
        }, modifier = Modifier.width(300.dp)) {
            Text(text = "Lokasjons innstillinger", color = Color.White)
        }

    }
}
    

    
    



