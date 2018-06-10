package com.meier.marina.magiccoroutines.data

import kotlinx.coroutines.experimental.suspendCancellableCoroutine

class ExampleRepository {

    suspend fun applyToMagicSchool(user: User) {
        return suspendCancellableCoroutine { continuation ->

            if (user.wand == null) {
                continuation.resumeWithException(Exception("User  doesn't have a wand!"))
            }

            applyToHogwarts(object : Response {

                //waiting for result :

                override fun accept() {
                    continuation.resume(Unit)
                }

                override fun decline() {
                    continuation.resumeWithException(Exception("Try another school"))
                }
            })
        }
    }

    fun applyToHogwarts(response: Response) {
        // no-op
    }
}

interface Response {
    fun accept()

    fun decline()
}