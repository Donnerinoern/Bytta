package prj.edu.bytta.meldinger

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.toObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import prj.edu.bytta.Bruker
import prj.edu.bytta.ChatRoomChangedEvent
import prj.edu.bytta.MeldingKonstanter.TAG
import javax.inject.Inject



@HiltViewModel
class MeldingerViewModel @Inject constructor(
    private val brukerRepository: BrukerRepository,
    private val chatRoomRepository: ChatRoomRepository,

): ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    @SuppressLint("RestrictedApi")
    private val _currentUser = MutableStateFlow(Bruker())
    val currentUser: StateFlow<Bruker> = _currentUser


    // Henter alle chatterom brukeren har deltat i
    private val _chats = MutableStateFlow(listOf<DisplayChatteRommet>())
    val chats: StateFlow<List<DisplayChatteRommet>> = _chats

    init {
        EventBus.getDefault().register(this)
        getCurrentUserAndChats()
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }

    private fun getCurrentUserAndChats() {
        _loading.value = true
        brukerRepository.getCurrentUser().addOnCompleteListener {
            currentUserResults -> _currentUser.value = currentUserResults.result!!.toObjects(
            Bruker::class.java)[0]
            //Henter alle chatsene brukeren er med i
            chatRoomRepository.getChatRoomsOfUser(currentUser.value.uid).addOnCompleteListener{
                chatsResults -> if (chatsResults.isSuccessful && chatsResults != null) {
                    var chats = chatsResults.result!!.toObjects(ChatteRom::class.java)

                chats = getChatRoomWithMessages(chats)

                val andreBrukerIds = getAndreBrukere(chats)
            }
            }

        }
    }
    //Henter chatsene som inneholder meldinger
    private fun getChatRoomWithMessages(rawData: List<ChatteRom>): List<ChatteRom> {
        val chatsMedMeldinger= mutableListOf<ChatteRom>()
        for (chatteRom in rawData) {
            if (chatteRom.sisteMeldingTid != null && chatteRom.sisteMeldingText != null) {
                chatsMedMeldinger.add(chatteRom)
            }
        }
        return  chatsMedMeldinger
    }

    private fun getAndreBrukere(chatteRoms: List<ChatteRom>): List<String> {
        val andreBrukere = mutableListOf<String>()
        for (chatteRom in chatteRoms) {
            andreBrukere.add(getAndreBrukereUid(chatteRom))
        }
        return andreBrukere
    }

    private fun getAndreBrukereUid(chatteRom: ChatteRom): String {
        return if(chatteRom.gruppe) {
            chatteRom.admin!!
        }else {
            if (chatteRom.chatRomBrukere[0]== currentUser.value.uid) {
                chatteRom.chatRomBrukere[1]
            }else {
                chatteRom.chatRomBrukere[0]
            }
        }
    }

  private fun lagDisplayChatteRom(chats: List<ChatteRom>, andreBrukere: List<Bruker>) {
      val onCompletion = {displayChatteRommet: List<DisplayChatteRommet> ->
          _chats.value = displayChatteRommet.sortedByDescending { it.sisteMeldingTid }
          _loading.value = false
      }
      mergeChatteromAndBruker(
          chatteRoms = chats,
          brukere = andreBrukere,
          onCompletion = onCompletion,
      )

  }

   private fun navigateTilChatRom(chatteRomUid: String) {
       Log.d(TAG,"Navigerer til et chatterom, med ID $chatteRomUid")

       val event = ChatRoomChangedEvent(chatteRomUid)
       EventBus.getDefault().post(event)
   }

    //Sender brukeren til Chatten h*n har klikket på
    fun onChatKlikket(position: Int) {
        val chatUid = chats.value[position].chatUid
        navigateTilChatRom(chatUid)
    }




}