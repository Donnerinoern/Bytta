package prj.edu.bytta

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import prj.edu.bytta.ui.theme.ByttaTheme

class MessageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            ByttaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {

                    //Conversation(SampleData.conversationSample)
                    FullChat()

                }
            }
        }
    }
}

data class Message(val user: String, val body: String)

object SampleData {
    // En haug med sample meldinger
    val conversationSample = listOf(
        Message(
            "Erik",
            "Halla du vil ikke bytte den derre eple telefonen din nr 7 mot en rå Meta Quest 2 256gb VR-Briller, uåpnet? "
        ),
        Message(
            "Erik",
            "Broder er du der eller?"
        ),
        Message(
            "Erik",
            " Min Meta Quest 2 256gb VR-Briller, uåpnet har en verdi på 6800kr liksom og den gjerrige mobben din er kun verdt 3200kr ubrukt!!!!"
        ),
        Message(
            "Erik",
            "Åløøh jeg ringer faren min asss!"
        ),
        Message(
            "Svein",
            "Med hvilken mobil?"
        ),


        )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMelding() {
        var text by remember { mutableStateOf(TextFieldValue(""))}
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text(text = "Send Melding")},
        )
}

@Composable
fun SendBtn() {
    Button(onClick = { /*TODO*/ }) {
        Image(
            painter = painterResource(id = R.drawable.arrowforward),
            contentDescription = "Send",
            modifier = Modifier.size(15.dp))
        Text(text = "Send", Modifier.padding(start = 2.dp))
    }
}

@Composable
fun TilbakeBtn() {
    Button(onClick = { /*TODO*/ }) {
        Image(
            painter = painterResource(id = R.drawable.arrow_back),
            contentDescription = "Tilbake til meldinger",
            modifier = Modifier.size(15.dp)
        )
    }
}

@Composable
fun MeldingLinje() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceEvenly,


    ){
        SendMelding()
        SendBtn()
    }
}

@Composable
fun MessageCard(msg: Message) {
    // Padding på melding
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(id = R.drawable.steve),
            contentDescription ="PB",
            modifier = Modifier
                // bilde størrelse
                .size(50.dp)
                // Gjør bildet om til en sirkel
                .clip(CircleShape)
                // Setter en border rundt PB
                .border(1.5.dp, MaterialTheme.colorScheme.primary)
        )
        // Horisontalt mellomrom mellom bildet og kolonnene
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(text = msg.user,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )
            // Vertikalt mellomrom mellom text linjenene
            Spacer(modifier = Modifier.height(2.dp))

            Surface(shape = MaterialTheme.shapes.medium, shadowElevation = 3.dp ) {
                Text(text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        }
    }
}

@Composable
fun Conversation(messages: List<Message>){
    LazyColumn{
        items(messages) { message -> MessageCard(message) }
    }
}
@Preview
@Composable
fun FullChat(){
    Box(Modifier.fillMaxSize()) {
        Box(Modifier.align(Alignment.TopStart)) {
            TilbakeBtn()
        }
        Spacer(modifier = Modifier.size(30.dp))
        Box(Modifier.align(Alignment.Center)){
            Conversation(SampleData.conversationSample)
        }
        Box(Modifier.align(Alignment.BottomCenter)) {
            MeldingLinje()
        }
    }
}

//@Preview("Light Mode")
/*@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)*/
@Composable
fun PreviewConversation() {
    ByttaTheme {
        Surface {
            Conversation(SampleData.conversationSample)
        }
    }
}



