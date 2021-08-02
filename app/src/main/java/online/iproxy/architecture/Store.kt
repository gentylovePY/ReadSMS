package online.iproxy.architecture

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface Store<State, Input, Effect> {

    val state: StateFlow<State>

    val effects: Flow<Effect>

    fun processInput(input: Input)

}