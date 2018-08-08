package com.meier.marina.magiccoroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.meier.marina.magiccoroutines.data.MainRepository
import com.meier.marina.magiccoroutines.data.User
import com.meier.marina.magiccoroutines.utils.logD
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.*
import kotlin.coroutines.experimental.CoroutineContext

class MainViewModel : ViewModel() {

    private val mainRepository = MainRepository()
    private val users = mutableSetOf<User>()

    private var count = 0

    val userLiveData = MutableLiveData<List<User>>()
    val countLiveData = MutableLiveData<String>()

    val channel: SendChannel<Int>

    init {
        channel = Channel()
        launch(UI) {
            channel.throttle().consumeEach {
                "received value: $it".logD()
                countLiveData.value = "Last value $it"
            }
        }

        launch(CommonPool) {
            try {
                users.add(mainRepository.getMyWizard())
                userLiveData.postValue((users.toList()))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun useThrottle() {
        launch {
            count++
            "sent value: $count".logD()
            channel.send(count)
        }
    }

    fun useLaunchCoroutines() {
        runCoroutine { runLaunchCoroutines() }
    }

    fun useLaunchParallelCoroutines() {
        runCoroutine { runLaunchParallelCoroutines() }
    }

    fun useAsyncCoroutine() {
        runCoroutine { runAsyncCoroutine() }
    }

    fun useAsyncParallelCoroutine() {
        runCoroutine { runAsyncParallelCoroutine() }
    }

    fun useLaunchWithCoroutines() {
        runCoroutine { runOneLaunchCoroutines() }
    }

    fun useOneWithCoroutine() {
        runCoroutine { runOneWithCoroutine() }
    }

    private fun runCoroutine(coroutineHandler: suspend () -> Unit) = launch(UI) {
        users.clear()
        coroutineHandler()
        userLiveData.value = users.toList()
    }

    private suspend fun runAsyncCoroutine() {
        val startTime = System.currentTimeMillis()

        val firstUser = async { mainRepository.getRandomUser() }
        users.add(firstUser.await())

        val secondUser = async { mainRepository.getRandomUser() }
        users.add(secondUser.await())

        val thirdUser = async { mainRepository.getRandomUser() }
        users.add(thirdUser.await())

        val result = System.currentTimeMillis() - startTime
        "Many coroutines async $result".logD()
    }

    private suspend fun runAsyncParallelCoroutine() {
        val startTime = System.currentTimeMillis()

        val firstUser = async { mainRepository.getRandomUser() }

        val secondUser = async { mainRepository.getRandomUser() }

        val thirdUser = async { mainRepository.getRandomUser() }

        users.addAll(awaitAll(firstUser, secondUser, thirdUser))

        val result = System.currentTimeMillis() - startTime
        "Many coroutines async parallel $result".logD()
    }

    private suspend fun runLaunchCoroutines() {
        val startTime = System.currentTimeMillis()

        launch {
            users.add(mainRepository.getRandomUser())
        }.join()

        launch {
            users.add(mainRepository.getRandomUser())
        }.join()

        launch {
            users.add(mainRepository.getRandomUser())
        }.join()

        val result = System.currentTimeMillis() - startTime
        "Many coroutines launch $result".logD()
    }

    private suspend fun runLaunchParallelCoroutines() {
        val startTime = System.currentTimeMillis()

        val job = launch {
            users.add(mainRepository.getRandomUser())
        }

        val job2 = launch {
            users.add(mainRepository.getRandomUser())
        }

        val job3 = launch {
            users.add(mainRepository.getRandomUser())
        }

        joinAll(job, job2, job3)

        val result = System.currentTimeMillis() - startTime
        "Many coroutines launch parallel $result".logD()
    }

    private suspend fun runOneWithCoroutine() {
        val startTime = System.currentTimeMillis()

        withContext(CommonPool) {
            users.add(mainRepository.getRandomUser())
            users.add(mainRepository.getRandomUser())
            users.add(mainRepository.getRandomUser())

            val result = System.currentTimeMillis() - startTime
            "Switching ex. thread withContext $result".logD()
        }
    }

    private suspend fun runOneLaunchCoroutines() {
        val startTime = System.currentTimeMillis()

        launch {
            users.add(mainRepository.getRandomUser())
            users.add(mainRepository.getRandomUser())
            users.add(mainRepository.getRandomUser())

            val result = System.currentTimeMillis() - startTime
            "Create new coroutine and join $result".logD()
        }.join()
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
}
