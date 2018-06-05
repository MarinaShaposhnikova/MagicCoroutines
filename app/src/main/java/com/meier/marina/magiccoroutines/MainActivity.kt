package com.meier.marina.magiccoroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.meier.marina.magiccoroutines.data.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.userLiveData.observe(this, Observer(::showUser))
    }

    private fun showUser(user: User?) {
        user ?: return

        textName.text = user.name
        textLastName.text = user.lastName
        Picasso.get().load(user.photoUrl).into(imagePhoto)
    }
}
