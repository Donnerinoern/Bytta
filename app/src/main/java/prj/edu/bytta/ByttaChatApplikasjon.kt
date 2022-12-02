package prj.edu.bytta

import android.app.Application
import javax.inject.Inject

class ByttaChatApplikasjon @Inject constructor(): Application()

const val BYTTA_CHAT_PREF = "Bytta_Chat_pref"