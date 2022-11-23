package prj.edu.bytta

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.material.Text


@Composable
fun SingleMessage(message: String, isCurrentUser: Boolean) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentUser) MaterialTheme.colorScheme.primary else Color.White
        )
    ) {
        Text(
            text = message,
            textAlign = if (isCurrentUser) TextAlign.End
        else
            TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            color = if (!isCurrentUser) MaterialTheme.colorScheme.primary else Color.White)
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCard(trade: Trade,) {
    val context = LocalContext.current
    Spacer(modifier = Modifier.height(5.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = {/*TODO*/
                 val intent = Intent(context, MessageActivity::class.java)
                  context.startActivity(intent)},
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(modifier = Modifier.padding(all = 5.dp)) {
            Image(
                painter = painterResource(R.drawable.petter_northug_2018_scanpix),
                contentDescription = "Contact profile picture",
                modifier = Modifier
                    // Set image size to 40 dp
                    .size(40.dp)
                    // Clip image to be shaped as a circle
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Column {
                Text(text = trade.user)
                // Add a vertical space between the author and message texts
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = trade.item)
                Spacer(modifier = Modifier.height(5.dp))

            }
        }

       }
}

@Composable
fun TopBarDetails(
    selectedUserDetails: User,
    back: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Card(

        shape = RoundedCornerShape(0),
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
        )
        {
            AsyncImage(
                model = ImageRequest.Builder(
                    LocalContext.current).data(selectedUserDetails.profilePic),

                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
                )



            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BackButton(
                        back = {
                            back()
                            focusManager.clearFocus()
                        },
                        icon = Icons.Default.ArrowBack,
                    )
                }
            }
        }
    }
}

@Composable
fun BackButton(back: () -> Unit, icon: ImageVector, ) {
    Column(
        modifier = Modifier
            .padding(start = 15.dp)
    ) {

        Box(modifier = Modifier
            .size(35.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(10.dp))
        ) {
            IconButton(onClick ={
                back()
            }
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(25.dp),
                    tint = Color.Black
                )
            }


        }
    }
}

@Composable
fun SingelMessage(message: MessageDetails) {
    Row(
        horizontalArrangement = if(message.messageByCurrentUser)
            Arrangement.End else Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth(
                if (message.messageByCurrentUser) 1f
                else 0.5f
            )
            .padding(bottom = 10.dp)
    ) {
        if (message.messageByCurrentUser)
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (message.messageByCurrentUser) MaterialTheme.colorScheme.primary else Color.White,

            ),
                shape = RoundedCornerShape(30),
            ) {
                Text(
                    text = message.message,
                    textAlign = if (message.messageByCurrentUser) TextAlign.End
                    else
                        TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    color = if (!message.messageByCurrentUser) MaterialTheme.colorScheme.primary else Color.White)

            }
    }
}

@Composable
fun Boxern(
    size: Int? = null,
    height: Int? = null,
    width: Int? = null,
) {
    val fix = size != null || height != null || width != null
    
    if (fix) {
        if (size != null) {
            Spacer(modifier = Modifier.size(size.dp))
        } else if (height != null && width != null){
            Spacer(modifier = Modifier
                .height(height.dp)
                .width(width.dp)
            )
        } else if(height != null){
            Spacer(modifier = Modifier.height(height.dp))
        } else if (width != null){
            Spacer(modifier = Modifier.width(width.dp))
        }else {
            Spacer(modifier = Modifier.fillMaxSize(1f))
        }
    }
}


