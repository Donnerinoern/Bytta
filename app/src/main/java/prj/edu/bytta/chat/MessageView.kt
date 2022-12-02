package prj.edu.bytta.chat

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import prj.edu.bytta.chat.MeldingKonstanter
import prj.edu.bytta.chat.MessageViewModel

@Composable
fun MessageView(
    messageViewModel: MessageViewModel = viewModel(),
    navController: NavController
) {
    val melding: String by messageViewModel.message.observeAsState(initial = "")
    val meldinger: List<Map<String, Any>> by messageViewModel.msg.observeAsState(
        initial = emptyList<Map<String, Any>>().toMutableList()
    )
    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,

                ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.DarkGray),
                    content = { Text(text = stringResource(R.string.Tilbake)) },
                    onClick = {
                        navController.navigate("chat_page")

                    })


                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(weight = 0.85f, fill = true),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    reverseLayout = true
                ) {
                    items(meldinger) { melding ->
                        val isCurrentuser = melding[MeldingKonstanter.BRUKER] as Boolean

                        SingleMessage(
                            message = melding[MeldingKonstanter.MELDINGER].toString(),
                            isCurrentUser = isCurrentuser
                        )
                    }
                }
                OutlinedTextField(
                    value = melding,
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
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                messageViewModel.sendMelding()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Send,
                                contentDescription = "Send Knapp"
                            )
                        }
                    }
                )
            }
        }
    }








