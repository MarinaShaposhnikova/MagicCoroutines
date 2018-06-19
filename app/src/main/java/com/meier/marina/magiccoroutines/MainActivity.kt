package com.meier.marina.magiccoroutines

import android.os.Bundle
import android.widget.LinearLayout.VERTICAL
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.meier.marina.magiccoroutines.data.User
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val adapter = UserAdapter()
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.userLiveData.observe(this, Observer(::showUser))
        viewModel.countLiveData.observe(this, Observer { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() })

        listUsers.adapter = adapter
        listUsers.layoutManager = LinearLayoutManager(this)
        listUsers.addItemDecoration(DividerItemDecoration(this, VERTICAL))

        grid.adapter = ButtonAdapter(generateGrid())
    }

    private fun showUser(users: List<User>?) {
        users ?: return

        adapter.addData(users)
    }

    private fun generateGrid() =
        listOf(
            ButtonItem("One withContext", { viewModel.useOneWithCoroutine() }),
            ButtonItem("One launch", { viewModel.useLaunchWithCoroutines() }),
            ButtonItem("Many launch", { viewModel.useLaunchCoroutines() }),
            ButtonItem("Many async", { viewModel.useAsyncCoroutine() }),
            ButtonItem("Parallel many launch", { viewModel.useLaunchParallelCoroutines() }),
            ButtonItem("Parallel many async", { viewModel.useAsyncParallelCoroutine() }),
            ButtonItem("Use throttle", { viewModel.useThrottle() })
        )
}
