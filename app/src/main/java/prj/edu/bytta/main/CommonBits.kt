package prj.edu.bytta.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import prj.edu.bytta.Signup
import prj.edu.bytta.SignupViewmodel
import prj.edu.bytta.data.Event

val popupNotification = mutableStateOf <Event<String>?>(null)

@Composable
fun NotificationMessage (viewModel: SignupViewmodel) {
    val notifState = viewModel.popupNotification.value
    val notifMessage = notifState?.getContentOrNull()
    if (notifMessage != null) {

    }
}