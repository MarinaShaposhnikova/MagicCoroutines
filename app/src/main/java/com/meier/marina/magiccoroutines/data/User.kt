package com.meier.marina.magiccoroutines.data

data class User(val id: Int, val name: String, val lastName: String, val photoUrl: String? = null) {

    var wand: MagicWand? = null
}
