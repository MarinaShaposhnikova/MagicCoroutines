package com.meier.marina.magiccoroutines.utils

import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.newSingleThreadContext
import kotlin.coroutines.experimental.CoroutineContext

private val parallelism by lazy {
    (Runtime.getRuntime().availableProcessors() - 1).coerceAtLeast(1)
}

val BG by lazy {
    if (parallelism > 1) DefaultDispatcher
    else newSingleThreadContext("BgThread")
}

fun <T> Channel<T>.throttle(
        wait: Long = 1500,
        context: CoroutineContext = DefaultDispatcher
): ReceiveChannel<T> = produce(context) {

    var mostRecent: T

    consumeEach {
        mostRecent = it

        delay(wait)
        while (!isEmpty) {
            mostRecent = receive()

        }
        send(mostRecent)
    }
}
