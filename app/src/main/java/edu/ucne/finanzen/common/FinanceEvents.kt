package edu.ucne.finanzen.common

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object FinanceEvents {
    private val _event = MutableSharedFlow<Boolean>(extraBufferCapacity = 1)
    val event: SharedFlow<Boolean> = _event.asSharedFlow()

    fun notifyChange(shouldAlert: Boolean = true) = _event.tryEmit(shouldAlert)
}