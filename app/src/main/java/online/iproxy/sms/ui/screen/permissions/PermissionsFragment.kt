package online.iproxy.sms.ui.screen.permissions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import online.iproxy.sms.R
import online.iproxy.sms.databinding.FragmentPermissionsBinding
import online.iproxy.sms.ui.screen.permissions.PermissionsViewModel.Effect
import online.iproxy.sms.ui.screen.permissions.PermissionsViewModel.Input
import online.iproxy.sms.util.Permissions.getMissingPermissions
import online.iproxy.sms.util.Permissions.shouldShowRequestPermissionsRationale

@AndroidEntryPoint
class PermissionsFragment : Fragment() {

    private lateinit var binding: FragmentPermissionsBinding

    private val viewModel by viewModels<PermissionsViewModel>()

    private val requestPermissions =
        registerForActivityResult(RequestMultiplePermissions()) { result ->
            viewModel.processInput(Input.PermissionsGranted(result))
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPermissionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.state
                .collect { state -> renderState(state) }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.effects
                .collect { effect -> handleEffect(effect) }
        }
    }

    override fun onStart() {
        super.onStart()
        updatePermissionsState()
    }

    private fun handleEffect(effect: Effect) {
        when (effect) {
            is Effect.RequestPermissions -> {
                requestPermissions.launch(effect.permissions.toTypedArray())
            }
        }
    }

    private fun renderState(state: PermissionsViewModel.State) {
        when {
            !state.initialized -> {
                // do nothing
            }
            state.granted || state.missingPermissions.isEmpty() -> {
                binding.message.text = "All permissions granted."
                binding.actionButton.text = "Done"
                binding.actionButton.setOnClickListener {
                    findNavController().navigate(R.id.nav_action_to_home)
                }
            }
            !state.requested -> {
                binding.message.text = "The app requires SMS permissions to operate."
                binding.actionButton.text = "Grant permissions"
                binding.actionButton.setOnClickListener {
                    viewModel.processInput(Input.RequestPermissions)
                }
            }
            !state.granted -> {
                binding.message.text =
                    "You denied permissions. Please allow them in the app Settings."
                binding.actionButton.text = "Go to Settings"
                binding.actionButton.setOnClickListener {
                    // todo: goto app settings
                }
            }
        }
    }

    private fun updatePermissionsState() {
        viewModel.processInput(
            Input.UpdatePermissionsState(
                permissions = getMissingPermissions(requireActivity()),
                showPermissionsRationale = shouldShowRequestPermissionsRationale(requireActivity())
            )
        )
    }


}