package prj.edu.bytta

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MessageView(
    messageViewModel: MessageViewModel = viewModel()
) {
    val message: String by messageViewModel.message.observeAsState(initial = "")
    val messages: List<Map<String, Any>> by messageViewModel.msg.observeAsState(
        initial = emptyList<Map<String, Any>>().toMutableStateList()
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 0.75f, fill = true),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            reverseLayout = true
        ) {
            items(messages) {
                message -> val isCurrentuser = message[MeldingKonstanter.BRUKER] as Boolean

                SingleMessage(
                    message = message[MeldingKonstanter.MELDING].toString(),
                    isCurrentUser = isCurrentuser
                )
            }

    }
        OutlinedTextField(
            value = message ,
            onValueChange = { messageViewModel.updateMelding(it) },
            label = {
                Text("Skriv melding")
            },
            maxLines = 1,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 1.dp)
                .fillMaxWidth()
                .weight(weight = 0.10f, fill = true),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            singleLine = true,
            trailingIcon =  {
                IconButton(
                    onClick = {
                        messageViewModel.sendMelding()
                    }
                ){
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send Knapp"
                    )
                }

            }
            )

    }
}