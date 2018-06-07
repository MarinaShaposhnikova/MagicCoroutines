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
    private val timeLaunch = mutableListOf<Long>()
    private val timeOne = mutableListOf<Long>()

    val userLiveData = MutableLiveData<List<User>>()

    fun printLogs() {
        Log.d(LOG_TAG, "Launch: ${timeLaunch.joinToString(", ")}")
        Log.d(LOG_TAG, "One: ${timeOne.joinToString(", ")}")
    }

    fun useLaunchCoroutines() {
        users.clear()
        launch(UI) {
            runLaunchCoroutines()
            userLiveData.value = users.toList()
            Log.d(LOG_TAG, "show user : launch")
        }
    }

    fun useOneCoroutine() {
        users.clear()
        launch(UI) {
            runOneCoroutine()
            userLiveData.value = users.toList()
            Log.d(LOG_TAG, "show user : one")
        }
    }

    private suspend fun runLaunchCoroutines() {
        val startTime = System.currentTimeMillis()

        Log.d(LOG_TAG, "START LAUNCH coroutines")

        launch {

            Log.d(LOG_TAG, "Create a new launch coroutine ${System.currentTimeMillis() - startTime}")

            val firstUser = mainRepository.getRandomUser()

            launch(UI) {

                users.add(firstUser)

                launch {

                    val secondUser = mainRepository.getRandomUser()


                    launch(UI) {

                        users.add(secondUser)


                        launch {
                            val thirdUser = mainRepository.getRandomUser()

                            val userWithWand = mainRepository.getWand(thirdUser)

                            users.add(userWithWand)

                            val result = System.currentTimeMillis() - startTime
                            Log.d(LOG_TAG, "SUMMARY: A lot of ${result}")
                            timeLaunch.add(result)

                        }.join()
                    }.join()
                }.join()
            }.join()
        }.join()
    }

    private suspend fun runOneCoroutine() {
        val startTime = System.currentTimeMillis()

        Log.d(LOG_TAG, "START ONE coroutine")

        withContext(CommonPool) {

            Log.d(LOG_TAG, "Switch execution thread One ${System.currentTimeMillis() - startTime}")

            val firstUserWC = mainRepository.getRandomUser()


            withContext(UI) {

                users.add(firstUserWC)


                withContext(CommonPool) {

                    val secondUserWC = mainRepository.getRandomUser()

                    withContext(UI) {

                        users.add(secondUserWC)

                        withContext(CommonPool) {
                            val thirdUserWC = mainRepository.getRandomUser()

                            val userWithWandWC = mainRepository.getWand(thirdUserWC)
                            users.add(userWithWandWC)

                            val result = System.currentTimeMillis() - startTime
                            Log.d(LOG_TAG, "SUMMARY: One ${result}")
                            timeOne.add(result)
                        }
                    }
                }
            }
        }
    }
}
