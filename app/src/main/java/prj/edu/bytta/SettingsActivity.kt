package prj.edu.bytta

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceCategory
import android.provider.Settings
import android.provider.Settings.*
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.Nullable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import prj.edu.bytta.ui.theme.ByttaTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // A surface container using the 'background' color from the theme
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                    TopAppbarSettings(context = LocalContext.current.applicationContext)
                    //utseendeSettings(context = LocalContext.current.applicationContext)
                }
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppbarSettings(context: Context) {
    val context = LocalContext.current
    TopAppBar(



        title = {
            Text(text = "Innstillinger", maxLines = 1,) },


        navigationIcon = {
            IconButton(onClick = {

            }) {
                androidx.compose.material3.Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Gå tilbake",
                )
            }
        }
    )
}





/*

@Composable
fun utseendeSettings(context: Context) {
    val mCheckedState = remember { mutableStateOf(false) }
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp)
            .background(MaterialTheme.colorScheme.primary),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically



    ) {


        Text(
            text = "Utseende",

            maxLines = 1,
            textAlign = TextAlign.Center

        )

        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_dark_mode_24), contentDescription = null)





    }

    Spacer(modifier = Modifier
        .height(5.dp))

    Column(modifier = Modifier
        .fillMaxWidth()
        .background(color = Color.Red)

    ) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp)
                .background(MaterialTheme.colorScheme.secondary),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(text = "Nattmodus")

            Switch(checked = mCheckedState.value, onCheckedChange = { mCheckedState.value = it })


        }
    }



}
    
    
  */
    
    



