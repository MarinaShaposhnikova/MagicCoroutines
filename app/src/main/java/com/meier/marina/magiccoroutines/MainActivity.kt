package com.meier.marina.magiccoroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.meier.marina.magiccoroutines.R.id.listUsers
import com.meier.marina.magiccoroutines.data.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


class MainActivity : AppCompatActivity() {

    private val adapter = UserAdapter()
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.userLiveData.observe(this, Observer(::showUser))

        listUsers.adapter = adapter
        listUsers.layoutManager = LinearLayoutManager(this)

        buttonOne.setOnClickListener {
            viewModel.useOneCoroutine()
        }

        buttonLaunch.setOnClickListener {
            viewModel.useLaunchCoroutines()
        }

        buttonLaunchJoin.setOnClickListener {
            viewModel.useLaunchJoinCoroutines()
        }

        buttonAsync.setOnClickListener {
            viewModel.useAsyncCoroutine()
        }

        buttonAsyncMulti.setOnClickListener {
            viewModel.useAsyncMultiCoroutine()
        }

        buttonLaunchJoinMulti.setOnClickListener{
            viewModel.useLaunchMultiCoroutines()
        }
    }

    private fun showUser(users: List<User>?) {
        users ?: return

        adapter.addData(users)
    }
}
