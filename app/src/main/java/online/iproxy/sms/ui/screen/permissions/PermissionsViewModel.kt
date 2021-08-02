package online.iproxy.sms.ui.screen.permissions

import dagger.hilt.android.lifecycle.HiltViewModel
import online.iproxy.architecture.BaseViewModel
import online.iproxy.sms.ui.screen.permissions.PermissionsViewModel.*
import javax.inject.Inject

@HiltViewModel
class PermissionsViewModel @Inject constructor() :
    BaseViewModel<State, Input, Effect>() {

    data class State(
        val initialized: Boolean = false,
        val missingPermissions: List<String> = emptyList(),
        val requested: Boolean = false,
        val granted: Boolean = false
    )

    sealed class Input {
        data class UpdatePermissionsState(
            val permissions: List<String>,
            val showPermissionsRationale: Boolean
        ) : Input()

        object RequestPermissions : Input()
        data class PermissionsGranted(val result: Map<String, Boolean>) : Input()
    }

    sealed class Effect {
        data class RequestPermissions(val permissions: List<String>) : Effect()
    }

    override fun getInitialState() = State()

    override fun processInput(input: Input) {
        when (input) {
            is Input.UpdatePermissionsState -> {
                updatePermissionsState(input.permissions, input.showPermissionsRationale)
            }
            is Input.RequestPermissions -> {
                handleRequestPermissions()
            }
            is Input.PermissionsGranted -> {
                handlePermissionsResult(input.result)
            }
        }
    }

    private fun updatePermissionsState(
        permissions: List<String>,
        showPermissionsRationale: Boolean
    ) {
        val state = getState()
        val newState = state.copy(
            initialized = true,
            missingPermissions = permissions,
            requested = state.requested,
            granted = permissions.isEmpty()
        )
        emitState(newState)
    }

    private fun handleRequestPermissions() {
        val state = getState()
        if (state.missingPermissions.isNotEmpty()) {
            emitEffect(Effect.RequestPermissions(state.missingPermissions))
        }
        emitState(state.copy(requested = true))
    }

    private fun handlePermissionsResult(result: Map<String, Boolean>) {
        val state = getState()
        val newPermissions = state.missingPermissions.filterNot { result[it] ?: false }
        emitState(
            state.copy(
                missingPermissions = newPermissions,
                requested = true,
                granted = newPermissions.isEmpty()
            )
        )
    }

}