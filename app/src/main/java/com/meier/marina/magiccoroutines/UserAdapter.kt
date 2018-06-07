package com.meier.marina.magiccoroutines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meier.marina.magiccoroutines.R.id.imagePhoto
import com.meier.marina.magiccoroutines.R.id.textLastName
import com.meier.marina.magiccoroutines.R.id.textName
import com.meier.marina.magiccoroutines.data.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val items = mutableListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        with(holder.itemView) {
            val user = items[position]
            textName.text = user.name
            textLastName.text = user.lastName
            user.photoUrl?.let {
                Picasso.get()
                        .load(user.photoUrl)
                        .into(imagePhoto)
            }
        }
    }

    fun addData(newItems: List<User>) {
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
