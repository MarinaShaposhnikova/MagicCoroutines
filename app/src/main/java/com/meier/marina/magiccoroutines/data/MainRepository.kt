package com.meier.marina.magiccoroutines.data

import kotlin.coroutines.experimental.suspendCoroutine

class MainRepository {

    suspend fun getUser(id: String): User {
        return suspendCoroutine { continuation ->
            if (id.isEmpty()) continuation.resumeWithException(Exception("Id is empty"))
            else continuation.resume(getMyUser())
        }
    }

    suspend fun getWand(user: User?): User {
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
            "Marina",
            "Meier",
            "https://pbs.twimg.com/profile_images/509064160215183360/GE01Ht4h_400x400.jpeg")
    }
}
