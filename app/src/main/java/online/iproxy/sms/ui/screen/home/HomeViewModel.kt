package online.iproxy.sms.ui.screen.home

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import online.iproxy.architecture.BaseViewModel
import online.iproxy.sms.data.domain.model.TextMessage
import online.iproxy.sms.data.domain.repo.MessageRepository
import online.iproxy.sms.ui.screen.home.HomeViewModel.*
import java.util.concurrent.CancellationException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val messageRepo: MessageRepository,
) : BaseViewModel<State, Input, Effect>() {

    data class State(
        val isLoading: Boolean = false,
        val messages: List<TextMessage> = emptyList(),
        val error: Throwable? = null,
    )

    sealed class Input {
        object Retry : Input()
    }

    sealed class Effect {
        data class ShowError(val error: Throwable) : Effect()
    }

    init {
        dispatchAction { subscribeToMessages() }
    }

    override fun getInitialState(): State = State()

    override fun processInput(input: Input) {
        when (input) {
            Input.Retry -> {
                dispatchAction { subscribeToMessages() }
            }
        }
    }

    private suspend fun subscribeToMessages() {
        if (getState().isLoading) {
            // already loading
            return
        }

        try {
            messageRepo.observeMessagesBefore()
                .onStart {
                    updateState { it.copy(isLoading = true, error = null) }
                }
                .collect { messages ->
                    updateState { it.copy(isLoading = false, messages = messages) }
                }
        } catch (error: Exception) {
            if (error !is CancellationException) {
                updateState { it.copy(isLoading = false, error = error) }
                emitEffect(Effect.ShowError(error))
                log.e(error, "Failed to load messages")
            }
        }
    }

}