package am.a_t.webchatsocket.presentation.chat

import am.a_t.webchatsocket.model.User
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    private val _socketStatus = MutableStateFlow(false)
    val socketStatus: StateFlow<Boolean> = _socketStatus

    private val _messages = MutableSharedFlow<User>()
    val messages: SharedFlow<User> = _messages

    private val _getMessages = MutableSharedFlow<User>()
    val getMessages: SharedFlow<User> = _getMessages

    fun setStatus(status: Boolean) =
        viewModelScope.launch(Dispatchers.Main) {
            _socketStatus.value = status
        }

    fun addMessage(message: User) {
        viewModelScope.launch(Dispatchers.Main) {
            if (_socketStatus.value) {
                _messages.emit(message)
            }
        }
    }

    fun getMessage(message: User) {
        viewModelScope.launch(Dispatchers.Main) {
            if (_socketStatus.value) {
                _getMessages.emit(message)
            }
        }
    }
}