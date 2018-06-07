package com.meier.marina.magiccoroutines

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.meier.marina.magiccoroutines.data.MainRepository
import com.meier.marina.magiccoroutines.data.User
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI

class MainViewModel : ViewModel() {

    private val mainRepository = MainRepository()
    private val users = mutableSetOf<User>()

    val userLiveData = MutableLiveData<List<User>>()

    fun useLaunchCoroutines() {
        runCoroutine { runLaunchCoroutines() }
    }

    fun useLaunchMultiCoroutines() {
        runCoroutine { runLaunchMultiCoroutines() }
    }

    fun useLaunchJoinCoroutines() {
        runCoroutine { runLaunchJoinCoroutines() }
    }

    fun useAsyncCoroutine() {
        runCoroutine { runAsyncCoroutine() }
    }

    fun useAsyncMultiCoroutine() {
        runCoroutine { runAsyncMultiCoroutine() }
    }

    fun useOneCoroutine() {
        runCoroutine { runOneCoroutine() }
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
        "Async $result".logD()
    }

    private suspend fun runAsyncMultiCoroutine() {
        val startTime = System.currentTimeMillis()

        val firstUser = async { mainRepository.getRandomUser() }

        val secondUser = async { mainRepository.getRandomUser() }

        val thirdUser = async { mainRepository.getRandomUser() }

        users.add(firstUser.await())
        users.add(secondUser.await())
        users.add(thirdUser.await())

        val result = System.currentTimeMillis() - startTime
        "Async multi $result".logD()
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
        "A lot of joined $result".logD()
    }

    private suspend fun runLaunchMultiCoroutines() {
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

        job.join()
        job2.join()
        job3.join()

        val result = System.currentTimeMillis() - startTime
        "A lot of joined JOB $result".logD()
    }

    private suspend fun runOneCoroutine() {
        val startTime = System.currentTimeMillis()

        withContext(CommonPool) {
            users.add(mainRepository.getRandomUser())
            users.add(mainRepository.getRandomUser())
            users.add(mainRepository.getRandomUser())

            val result = System.currentTimeMillis() - startTime
            "One coroutine withContext $result".logD()
        }
    }

    private suspend fun runLaunchJoinCoroutines() {
        val startTime = System.currentTimeMillis()

        launch {
            users.add(mainRepository.getRandomUser())
            users.add(mainRepository.getRandomUser())
            users.add(mainRepository.getRandomUser())

            val result = System.currentTimeMillis() - startTime
            "One launch with join $result".logD()
        }.join()
    }
}
