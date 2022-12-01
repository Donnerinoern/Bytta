package prj.edu.bytta.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import coil.annotation.ExperimentalCoilApi
import prj.edu.bytta.MessageView
import prj.edu.bytta.ui.theme.ByttaTheme

class MessageActivity : ComponentActivity() {
    

    @OptIn(ExperimentalFoundationApi::class, ExperimentalCoilApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            ByttaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MessageView()
                    //Conversation(SampleData.conversationSample)
                    //ChatView(userMessage = String.Companion)
                    /*{
                        val userId = it.arguments?.getString(ChatKonstanter.userId) ?: ""
                        ChatViewBruker(userId = userId, back = { actions.navigateBack})
                    }
*/

                }
            }
        }
    }
}


























