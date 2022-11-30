package prj.edu.bytta

import android.content.Intent
import android.text.format.DateUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import androidx.compose.material.Text
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.size.Size
import prj.edu.bytta.Chat.Brukern
import prj.edu.bytta.Chat.MessageActivity
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.random.Random

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
fun UserRow(user: Brukern, modifier: Modifier = Modifier) {
    @Composable
    fun OnlineIndicator() {
        Box(
            Modifier
                .size(15.dp)
                .clip(CircleShape)
        )
    }
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(20.dp)
        ){
        PB(url = user.profileImageUrl, modifier = Modifier.padding(end = 15.dp))
        Column(
            modifier = Modifier
                .weight(1f, fill = true)
        ) {
            Text(
                user.navn,
                fontWeight = FontWeight.Bold,
            )
            Text(
                lastActiveStatus(user),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 5.dp),
                fontSize = 10.sp
            )
        }
        if (user.isOnline) {
            OnlineIndicator()
        }
    }
}

@Composable
@ReadOnlyComposable
private fun lastActiveStatus(user: Brukern): String {
    return when {
        user.isOnline -> stringResource(R.string.bruker_online)
        else -> stringResource(
            R.string.bruker_sist_på,
            DateUtils.getRelativeTimeSpanString(user.lastOnline.time)
        )
    }
}
@Composable
fun PB(
    url: String,
    modifier: Modifier = Modifier,
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .size(Size.ORIGINAL)
            .build()
    )
    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier
            .size(60.dp)
            .clip(RoundedCornerShape(10.dp))
    )
}
fun xd(size: Int = 30): List<Brukern> {
    return List(size) { stagedKontaktList() }
        .sortedByDescending { it.lastOnline }
        .sortedByDescending { it.isOnline }
}
fun stagedKontaktList(): Brukern {
    val names = listOf(
        "Marky Mark",
        "Breisås Kaffi slurpern",
        "Gi meg no weed for fan",
        "MetaQuestern",
        "Knut :)",
        "SibeWest",
    )
    return Brukern(
        navn = names.random(),
        isOnline = Random.nextFloat() < 0.4,
        lastOnline = Date(
            Random.nextLong(
                minTime.toEpochMilli(),
                maxTime.toEpochMilli()
            )
        ),
        profileImageUrl = randomAvatarUrl(),
        uid = "shgjkd",
    )
}

private val maxTime = Instant.now()
private val minTime = maxTime.minus(2, ChronoUnit.DAYS)

fun randomAvatarUrl(): String {
    val category = listOf("men", "women").random()
    val index = (0..99).random()
    return "https://randomuser.me/api/portraits/$category/$index.jpg"
}