package com.meier.marina.magiccoroutines

import com.meier.marina.magiccoroutines.data.User
import java.util.*

const val LOG_TAG = "marina_meier"

val NAMES = listOf(
    "Marina",
    "Anton",
    "Denis",
    "George",
    "Anna",
    "Jenessa",
    "Jack",
    "Mike"
)

val LASTNAMES = listOf(
    "Smett",
    "Black",
    "Great",
    "Sparrow",
    "Lispov"
)


object UserRandom {

    fun getUser(): User {
        val id = (Math.random() * 1000).toInt()
        return User(
            id = id,
            name = NAMES.random(),
            lastName = LASTNAMES.random(),
            photoUrl = "https://picsum.photos/200/200/?image=$id")
    }
}

fun <E> List<E>.random(): E = get(Random().nextInt(size))