package com.meier.marina.magiccoroutines

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.meier.marina.magiccoroutines.data.MainRepository
import com.meier.marina.magiccoroutines.data.User
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

class MainViewModel : ViewModel() {

    private val mainRepository = MainRepository()

    val userLiveData = MutableLiveData<User>()

    init {
        val startTime = System.currentTimeMillis()
        launch {
            val user = mainRepository.getUser("id")
            val userWithWand = mainRepository.getWand(user)
            userLiveData.postValue(userWithWand)
            Log.d(LOG_TAG, "${System.currentTimeMillis() - startTime}")
        }
    }
}
