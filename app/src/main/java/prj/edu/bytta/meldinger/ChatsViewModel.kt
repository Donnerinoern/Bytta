package prj.edu.bytta.meldinger

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import prj.edu.bytta.Bruker
import prj.edu.bytta.ChatStartedEvent
import prj.edu.bytta.MeldingKonstanter.TAG
import java.lang.Thread.State
import javax.inject.Inject


@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val brukerRepository: BrukerRepository,
    @ApplicationContext private val context: Context,
    private val chatRoomRepository: ChatRoomRepository

    ): ViewModel()  {


    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _brukern = MutableStateFlow(Bruker())
    val brukern: StateFlow<Bruker> = _brukern

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onChatStartedEvent(event: ChatStartedEvent) {
        Log.d(TAG, "Kjør da")
        getCurrentUserAndChats()
    }


    }


