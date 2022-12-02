package prj.edu.bytta

import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import prj.edu.bytta.chat.ChatroomActivity
import prj.edu.bytta.home.HomeActivity
import prj.edu.bytta.profil.MinePosts

@Composable
fun NavBar() {
    var selectedItem by remember { mutableStateOf(1) }
    val items = listOf("Profile", "Home", "Messages")
    val icons = listOf(
        Icons.Default.Person,
        Icons.Default.Home,
        Icons.Default.Email
    )
    val intents = listOf(
        MinePosts::class.java,
        HomeActivity::class.java,
        ChatroomActivity::class.java
    )
    BottomAppBar {
        NavigationBar() {
            val context = LocalContext.current
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = { androidx.compose.material3.Icon(imageVector = icons[index], contentDescription = items[index]) },
                    label = { Text(item) },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index
                        val intent = Intent(context, intents[index])
                        context.startActivity(intent)}
                )
            }
        }
    }
}
