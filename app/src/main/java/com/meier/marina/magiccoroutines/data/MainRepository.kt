package com.meier.marina.magiccoroutines.data

import com.meier.marina.magiccoroutines.utils.UserRandom
import kotlinx.coroutines.experimental.delay
import kotlin.coroutines.experimental.suspendCoroutine

class MainRepository {

    suspend fun getUser(id: String): User {
        return suspendCoroutine { continuation ->
            if (id.isEmpty()) continuation.resumeWithException(Exception("Id is empty"))
            else continuation.resume(getMyUser())
        }
    }

    suspend fun getRandomUser(): User {
        delay(1000)
        return suspendCoroutine { continuation ->
            continuation.resume(UserRandom.getUser())
        }
    }

    suspend fun getMyWizard(): User {
        val myUser = getUser("myId")

        return getWizard(myUser)
    }

    suspend fun getWizard(user: User?): User {
        return suspendCoroutine { continuation ->
            when {
                user == null -> continuation.resumeWithException(Exception("User is null"))
                user.wand != null -> continuation.resumeWithException(Exception("User can use only one wand"))
                else -> {
                    user.wand = getMyWand()
                    continuation.resume(user)
                }
            }
        }
    }

    private fun getMyWand(): MagicWand {
        return MagicWand(
            wood = "Corylus",
            core = "Phoenix feather",
            length = 11
        )
    }

    private fun getMyUser(): User {
        return User(
            95,
            "Marina",
            "Meier",
            "https://avatars3.githubusercontent.com/u/18406544?s=460&v=4")
    }
}
