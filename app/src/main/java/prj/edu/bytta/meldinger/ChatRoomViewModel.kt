package prj.edu.bytta.meldinger

import android.content.Context
import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import com.google.errorprone.annotations.Keep
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import prj.edu.bytta.Bruker
import prj.edu.bytta.ChatStartedEvent
import prj.edu.bytta.MeldingKonstanter.TAG
import prj.edu.bytta.R
import java.lang.Thread.State
import javax.inject.Inject



@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val chatRoomRepository: ChatRoomRepository,
    private val brukerRepository: BrukerRepository,
    private val firebaseAuth: FirebaseAuth
): ViewModel() {

    val displaying = false

    private val _loading = MutableStateFlow(false)
    var loading: StateFlow<Boolean> = _loading


    //Viser frem ChatteRom
    private val _chatteRom = MutableStateFlow(ChatteRom())
    val chatteRom: StateFlow<ChatteRom> = _chatteRom
    //Brukerne i chatterommet
    private val _bruker = MutableStateFlow(listOf<Bruker>())
    val bruker: StateFlow<List<Bruker>> = _bruker

    // Meldingene i chatterommet
    private val _meldinger = MutableStateFlow(listOf<Melding>())
    val meldinger: StateFlow<List<Melding>> = _meldinger

    //Brukere som er logget inn og bruker appen
    private val _lokalBruker = MutableStateFlow<Bruker>(Bruker())
    val lokalBruker: StateFlow<Bruker> = _lokalBruker

    private val _tittel = MutableStateFlow("")
    val tittel: StateFlow<String> = _tittel

    // Lagrer det som blir skrevet inn i meldingsfeltet
    private val _skrevetMelding = MutableStateFlow(InputField())
    val skrevetMelding: StateFlow<InputField> = _skrevetMelding

    // Antall mengder taksk som må bli fullført før en chat kan vises
    private var loadingMengde: Int = 0


    var lazyColumnState: LazyListState? = null

    var composableCoroutineScope: CoroutineScope? = null

    init {
        EventBus.getDefault().register(this)
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }

    private fun finnLokalBruker() {
        val lokalBrukerUid = firebaseAuth.currentUser!!.uid
        _lokalBruker.value = finnBrukerMedUid(lokalBrukerUid)
    }

    private fun finnBrukerMedUid(brukerUid: String): Bruker {
        for (bruker  in bruker.value) {
            if (bruker.uid == brukerUid) {
                return bruker
            }
        }
        return bruker.value[0]
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onChatteRomBytte(event: ChatteRomBytteEvent) {
        Log.d(TAG, "Chat byttet, ny ID er ${event.chatteRomId}")

        loadingProsessMengde = 2

        _loading.value = true

        getChatRoomAndMemeber(event.chatteRomId)
        navigationDispatcher.dispatchNavigationCommand { navController ->
            navController.navigate(NavDest.CHATTEROM)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoadingProsessFinished(event: LoadingFinishedEvent) {
    //loadingProsessMengde--
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMeldingRecieved(event: MeldingRecievedEvent) {
        Log.d(TAG, "Meldings event fått for chat rommet ${event.chatteRomUid}")

        if (displaying && event.chatteRomUid == chatteRom.value.chatUid) {
            Log.d(TAG, "Viser noe kult sikkert")
            reloadMelding(event.chatteRomUid)
        } else {
            val id = ByttaChatMessageService.MeldingsType.getIdForMeldingsType(type = event.meldingsType, chatteRomUid = event.chatteRomUid)

            buildNotification(
                context = context,
                tittel = event.tittel,
                text = event.text,


            )
        }
    }

    private fun reloadMelding(chatteRomUid: String) {
        chatRoomRepository.getChatRoom(chatteRomUid).addOnCompleteListener{
            reloadResult -> if (reloadResult.isSuccessful && reloadResult.result != null) {
                Log.d(TAG, "Meldingen skal bli sett i chatterommet")
            val queriedMeldinger = reloadResult.result!!.toObject(Melding::class.java)
            queriedMeldinger.sortBy{it.meldingTid}
            _meldinger.value = queriedMeldinger
            composableCoroutineScope?.launch {
                lazyColumnState?.animateScrollToItem(meldinger.value.size)
            }
        } else {
            val melding = context.getString(R.string.chat_faila_med_å_loade)
                Log.d(TAG, melding)
        }
        }
    }

    fun PrivatChat(): Boolean {
        return !chatteRom.value.gruppe
    }

    fun tilbakeKlikk() {

    }



    private fun hentChatteromTittel(): String {
        return if(PrivatChat() && bruker.value.size == 2) {
            if(lokalBruker.value.brukerNavn == bruker.value[0].brukerNavn) bruker.value[1].brukerNavn else bruker.value[0].brukerNavn
        } else {
            chatteRom.value.chatRomNavn
        }
    }

    fun onSendMelding() {
        val isFirstMelding = meldinger.value.isEmpty()
        // Lager meldings objektet
        val melding = Melding(
            meldingText = skrevetMelding.value.input,
            senderUid =  lokalBruker.value.uid,
            senderNavn = lokalBruker.value.brukerNavn
        )
        //Meldingene blir automatisk oppdatert
        _meldinger.value = meldinger.value + melding

        _skrevetMelding.value = skrevetMelding.value.copy(input = "", isError = false)

        chatRoomRepository.addMessagesToChatRoom(chatRoomUid = chatteRom.value.chatUid, melding = melding)
            ?.addOnCompleteListener{ meldingsResultat ->
                if (meldingsResultat.isSuccessful) {
                    if (isFirstMelding) {
                        EventBus.getDefault().post(ChatStartedEvent)
                    }
                } else {
                    val error = context.getString(R.string.chat_melding_ble_aldri_sendt)
                    Log.d(TAG, error)
                }

            }
        composableCoroutineScope?.launch {
            lazyColumnState?.animateScrollToItem(meldinger.value.size)
        }
    }

}

@Keep
object LoadingFinishedEvent

@Keep
class MeldingRecievedEvent(val chatteRomUid: String, val meldingsType: String, val tittel: String, val text: String)