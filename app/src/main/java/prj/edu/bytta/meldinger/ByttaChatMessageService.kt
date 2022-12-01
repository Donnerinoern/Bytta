package prj.edu.bytta.meldinger

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.greenrobot.eventbus.EventBus
import prj.edu.bytta.GroupsChangedEvent
import prj.edu.bytta.MeldingKonstanter.TAG
import javax.inject.Inject

const val MELDING_TOKEN = "melding_token"

const val TOKEN_IKKE_FUNNET = "Token_ikke_funnet"

const val INTENT_CHATROM_ID = "intent_chatrom_id"


class ByttaChatMessageService: FirebaseMessagingService() {

    @Inject
    lateinit var brukerRepository: BrukerRepository

    override fun onNewToken(token: String) {
        val firebaseAuth = FirebaseAuth.getInstance()
        Log.d(TAG, "FCM refreshed token: $token")

        if (firebaseAuth.currentUser != null) {
            Log.d(TAG, "Brukeren er logget inn, oppdaterer meldings token deres")
            brukerRepository.updateBrukerMeldingToken(firebaseAuth.currentUser!!.uid).addOnCompleteListener {
                tokenUpdateResult -> if (tokenUpdateResult.isSuccessful) {
                    Log.d(TAG, "Melding token oppdatert for brukeren ${firebaseAuth.currentUser!!.displayName}.")
            }else {
                Log.d(TAG, "Token feilet for brukeren ${firebaseAuth.currentUser!!.displayName}")
            }
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onMeldingRecieved(remoteMessage: RemoteMessage) {
        Log.d(TAG, "melding fra ${remoteMessage.from}")

        if (
            remoteMessage.notification != null &&
            remoteMessage.notification!!.title != null &&
            remoteMessage.notification!!.body != null &&
            remoteMessage.data.containsKey("chatteRomUid") &&
            remoteMessage.data.containsKey("meldingsType")
        ) {
            Log.d(TAG, "Denne melding er en notifikasjon med chatteroms UID. ${remoteMessage.notification!!.body}")
            Log.d(TAG, "Denne har data: ${remoteMessage.data}")

            val event = MeldingRecievedEvent(
                chatteRomUid = remoteMessage.data["chatteRomUid"]!!,
                meldingType = remoteMessage.data["meldingsType"]!!,
                title = remoteMessage.notification!!.title!!,
                text = remoteMessage.notification!!.body!!,

                )
                EventBus.getDefault().post(event)

                if(remoteMessage.data["meldingsType"] == MeldingsType.NY_GRUPPE) {
                    EventBus.getDefault().post(GroupsChangedEvent)
                }
        }
    }


    object MeldingsType {
        const val NY_MELDING = "ny_melding"

        const val NY_GRUPPE = "ny_gruppe"


        fun getIdForMeldingsType(type: String, chatteRomUid: String): Int {
            return when (type) {
              NY_MELDING -> {
                  chatteRomUid.hashCode()
              }
              NY_GRUPPE -> {
                  chatteRomUid.hashCode() +1
              }
                else -> {
                    chatteRomUid.hashCode()
                }
            }


        }
    }



}