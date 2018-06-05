package com.meier.marina.magiccoroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.meier.marina.magiccoroutines.data.MainRepository
import com.meier.marina.magiccoroutines.data.User
import kotlinx.coroutines.experimental.launch

class MainViewModel : ViewModel() {

    private val mainRepository = MainRepository()

    val userLiveData = MutableLiveData<User>()

    init {
        launch {
            val user = mainRepository.getUser("id")
            val userWithWand = mainRepository.getWand(user)
            userLiveData.postValue(userWithWand)
        }
    }
}
