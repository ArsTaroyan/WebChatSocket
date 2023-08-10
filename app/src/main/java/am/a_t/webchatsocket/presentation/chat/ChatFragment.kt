package am.a_t.webchatsocket.presentation.chat

import am.a_t.webchatsocket.data.sockets.MessageWebSocketListener
import am.a_t.webchatsocket.databinding.FragmentChatBinding
import am.a_t.webchatsocket.extensions.convertGsonToString
import am.a_t.webchatsocket.model.User
import am.a_t.webchatsocket.presentation.adapter.MessageAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var messageAdapter: MessageAdapter
    private val okHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null
    private lateinit var messageWebSocketListener: MessageWebSocketListener
    private var listUser: ArrayList<User> = arrayListOf()
    private val args: ChatFragmentArgs by navArgs()
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        messageWebSocketListener = MessageWebSocketListener(viewModel)

        initAdapter()
        initViewModel()
        initClickListeners()

        return binding.root
    }

    private fun initAdapter() {
        messageAdapter = MessageAdapter(args.name)
        with(binding.rvMessage) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = messageAdapter
        }
    }

    private fun initClickListeners() {
        with(binding) {
            btnConnect.setOnClickListener {
                webSocket = okHttpClient.newWebSocket(createRequest(), messageWebSocketListener)
            }

            btnDisconnect.setOnClickListener {
                webSocket?.close(1000, "Canceled manually.")
            }

            btnSend.setOnClickListener {
                if (!edName.text.isNullOrEmpty()) {
                    webSocket?.apply {
                        user = User(
                            args.name,
                            edText.text.toString(),
                            edName.text.toString()
                        )
                        viewModel.addMessage(user)
                        send(user.convertGsonToString())
                        edText.text?.clear()
                    }
                }
            }
        }
    }

    private fun createRequest(): Request {
        return Request.Builder()
            .url("wss://socketsbay.com/wss/v2/1/demo/")
            .build()
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            viewModel.messages.collectLatest {
                listUser.add(it)
                messageAdapter.submitList(listUser)
            }
        }

        lifecycleScope.launch {
            viewModel.socketStatus.collect {
                binding.tvConnect.text = if (it) "Connected" else "Disconnected"
            }
        }

        lifecycleScope.launch {
            viewModel.getMessages.collectLatest {
                if (it.client == args.name) {
                    listUser.add(it)
                    messageAdapter.submitList(listUser)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        okHttpClient.dispatcher.executorService.shutdown()
    }

}