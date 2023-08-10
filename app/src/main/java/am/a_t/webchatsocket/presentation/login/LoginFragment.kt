package am.a_t.webchatsocket.presentation.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import am.a_t.webchatsocket.R
import am.a_t.webchatsocket.databinding.FragmentLoginBinding
import am.a_t.webchatsocket.presentation.chat.ChatViewModel
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: ChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        initClickListeners()

        return binding.root
    }

    private fun initClickListeners() {
        binding.btnOpen.setOnClickListener {
            if (!binding.edYourName.text.isNullOrEmpty()) {
                val name = LoginFragmentDirections.actionLoginFragmentToChatFragment(binding.edYourName.text.toString())
                findNavController().navigate(name)
            }
        }
    }

}