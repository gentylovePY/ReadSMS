package online.iproxy.architecture

import android.os.Looper
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseViewModel<State : Any, Input : Any, Effect : Any>() :
    Store<State, Input, Effect>,
    ViewModel() {

    private val effectsChannel = Channel<Effect>(Channel.BUFFERED)

    private val stateFlow by lazy(LazyThreadSafetyMode.NONE) {
        MutableStateFlow(getInitialState())
    }

    @Suppress("MemberVisibilityCanBePrivate")
    protected val log: Timber.Tree
        get() = Timber.tag(tag)

    protected open val tag: String = "MVI:${javaClass.simpleName}"

    override val state: StateFlow<State>
        get() = stateFlow

    override val effects: Flow<Effect>
        get() = effectsChannel.receiveAsFlow()

    init {
        log.v("View model created")
    }

    override fun onCleared() {
        log.v("View model destroyed")
        super.onCleared()
    }

    protected fun getState(): State = stateFlow.value

    abstract fun getInitialState(): State

    protected fun emitEffect(effect: Effect) {
        val result = effectsChannel.trySendBlocking(effect)
        log.v("Dispatching effect: %s", effect)
        if (!result.isSuccess) {
            log.w("Failed to dispatch effect: %s", result)
        }
    }

    @MainThread
    protected fun emitState(state: State): State {
        require(Looper.getMainLooper() == Looper.myLooper()) {
            "Must be running on main thread"
        }
        if (stateFlow.compareAndSet(stateFlow.value, state)) {
            log.v("New state: %s", state)
        } else {
            log.w("New state REJECTED: %s", state)
        }
        return stateFlow.value
    }

    @MainThread
    protected fun updateState(update: (state: State) -> State): State {
        val newState = update(getState())
        return emitState(newState)
    }

    protected fun dispatchAction(action: suspend (State) -> Unit): Job {
        return viewModelScope.launch {
            action(getState())
        }
    }

    protected fun dispatchCancelableAction(
        cancelCondition: (State) -> Boolean,
        action: suspend (State) -> Unit
    ): Job {
        return viewModelScope.launch {
            val actionScope = this
            launch(start = CoroutineStart.UNDISPATCHED) {
                stateFlow.dropWhile { !cancelCondition(it) }.first()
                actionScope.cancel()
            }

            launch {
                val currentState = getState()
                if (!cancelCondition(currentState)) {
                    action(currentState)
                }
                actionScope.cancel()
            }
        }
    }


    protected inline fun <reified T : State> requireState() = getState() as T
}