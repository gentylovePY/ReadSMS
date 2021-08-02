package online.iproxy.sms.ui.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import online.iproxy.sms.R
import online.iproxy.sms.databinding.FragmentHomeBinding
import online.iproxy.sms.ui.screen.home.HomeViewModel.Effect
import online.iproxy.sms.ui.screen.home.HomeViewModel.State
import online.iproxy.sms.util.Permissions

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (!allPermissionsGranted()) {
            findNavController().navigate(R.id.nav_action_to_permissions)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.state
                .collect { state -> renderState(state) }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.effects
                .collect { effect -> handleEffect(effect) }
        }
    }

    private fun renderState(state: State) {
        when {
            state.messages.isEmpty() -> {
                when {
                    state.isLoading -> {
                        binding.message.text = "Loading..."
                    }
                    state.error != null -> {
                        binding.message.text = "Error: ${state.error.message}"
                    }
                    else -> {
                        binding.message.text = "No messages"
                    }
                }
            }
            else -> {
                binding.message.text = state.messages
                    .joinToString("\n---\n") {
                        "${it.from}: ${it.body}"
                    }
            }
        }
    }

    private fun handleEffect(effect: Effect) {
        when (effect) {
            is Effect.ShowError -> {
                Toast.makeText(
                    requireContext(),
                    "Failed to load messages: ${effect.error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun allPermissionsGranted(): Boolean {
        return Permissions.getMissingPermissions(requireContext()).isEmpty()
    }

}