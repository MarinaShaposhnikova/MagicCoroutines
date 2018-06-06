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


    init {

        launch(UI) {
            runOneCoroutine()

            runLaunchCoroutines()

            userLiveData.value = users.toList()
            Log.d(LOG_TAG, "show user")

        }
    }

    private suspend fun runLaunchCoroutines() {
        Log.d(LOG_TAG, "START LAUNCH coroutines")

        val startTime = System.currentTimeMillis()

        launch {

            val firstUser = mainRepository.getRandomUser()

            launch(UI) {

                Log.d(LOG_TAG, "Create a new launch coroutine ${System.currentTimeMillis() - startTime}")

                users.add(firstUser)

                launch {

                    val secondUser = mainRepository.getRandomUser()


                    launch(UI) {

                        users.add(secondUser)


                        launch {
                            val thirdUser = mainRepository.getRandomUser()

                            val userWithWand = mainRepository.getWand(thirdUser)

                            users.add(userWithWand)
                            Log.d(LOG_TAG, "A lot of ${System.currentTimeMillis() - startTime}")
                        }
                    }
                }
            }
        }.join()
    }

    private suspend fun runOneCoroutine() {
        Log.d(LOG_TAG, "START ONE coroutine")


        val startTime = System.currentTimeMillis()

        launch {
            val firstUserWC = mainRepository.getRandomUser()


            withContext(UI) {

                Log.d(LOG_TAG, "Switch execution thread One ${System.currentTimeMillis() - startTime}")


                users.add(firstUserWC)


                withContext(CommonPool) {

                    val secondUserWC = mainRepository.getRandomUser()

                    withContext(UI) {

                        users.add(secondUserWC)

                        withContext(CommonPool) {
                            val thirdUserWC = mainRepository.getRandomUser()

                            val userWithWandWC = mainRepository.getWand(thirdUserWC)
                            users.add(userWithWandWC)

                            Log.d(LOG_TAG, "One ${System.currentTimeMillis() - startTime}")
                        }
                    }
                }
            }
        }.join()
    }
}
