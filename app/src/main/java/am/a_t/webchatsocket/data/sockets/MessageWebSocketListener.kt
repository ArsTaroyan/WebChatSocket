package am.a_t.webchatsocket.data.sockets

import am.a_t.webchatsocket.extensions.convertStringToGson
import am.a_t.webchatsocket.model.User
import am.a_t.webchatsocket.presentation.chat.ChatViewModel
import dagger.hilt.android.HiltAndroidApp
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

class MessageWebSocketListener(
    private val viewModel: ChatViewModel
): WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        viewModel.setStatus(true)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        viewModel.getMessage(text.convertStringToGson<User>())
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        viewModel.setStatus(false)
    }

}