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
        val permissionsState: PermissionState = PermissionState.NOT_REQUESTED,
    )

    enum class PermissionState {
        NOT_REQUESTED,
        REQUESTED,
        GRANTED,
        DENIED,
    }

    sealed class Input {
        data class Initialize(
            val permissions: List<String>,
            val showPermissionsRationale: Boolean
        ) : Input()

        data class UpdateMissingPermissions(
            val permissions: List<String>
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
            is Input.Initialize -> {
                initialize(input.permissions, input.showPermissionsRationale)
            }

            is Input.UpdateMissingPermissions -> {
                updatePermissionsState(input.permissions)
            }
            is Input.RequestPermissions -> {
                handleRequestPermissions()
            }
            is Input.PermissionsGranted -> {
                handlePermissionsResult(input.result)
            }
        }
    }

    private fun initialize(
        permissions: List<String>,
        showPermissionsRationale: Boolean
    ) {
        val state = getState()
        if (state.initialized) {
            return
        }

        val newState = if (permissions.isNotEmpty() && !showPermissionsRationale) {
            emitEffect(Effect.RequestPermissions(permissions))
            state.copy(
                initialized = true,
                missingPermissions = permissions,
                permissionsState = PermissionState.REQUESTED
            )
        } else {
            state.copy(
                initialized = true,
                missingPermissions = permissions,
                permissionsState = PermissionState.NOT_REQUESTED
            )
        }

        emitState(newState)
    }

    private fun updatePermissionsState(permissions: List<String>) {
        val state = getState()
        val allGranted = permissions.isEmpty()

        val newState = state.copy(
            initialized = true,
            missingPermissions = permissions,
            permissionsState = if (allGranted) PermissionState.GRANTED else state.permissionsState
        )

        emitState(newState)
    }

    private fun handleRequestPermissions() {
        val state = getState()
        if (state.missingPermissions.isNotEmpty()) {
            emitEffect(Effect.RequestPermissions(state.missingPermissions))
            emitState(state.copy(permissionsState = PermissionState.REQUESTED))
        }
    }

    private fun handlePermissionsResult(result: Map<String, Boolean>) {
        val state = getState()
        val newPermissions = state.missingPermissions.filterNot { result[it] ?: false }
        val granted = newPermissions.isEmpty()
        val newState = state.copy(
            missingPermissions = newPermissions,
            permissionsState = if (granted) PermissionState.GRANTED else PermissionState.DENIED,
        )
        emitState(newState)
    }

}