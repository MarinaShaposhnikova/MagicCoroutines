package com.meier.marina.magiccoroutines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button

class ButtonAdapter(
    private val items: List<ButtonItem>
) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): ButtonAdapter? = null

    override fun getItemId(position: Int): Long = 0L

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val button = if (convertView == null) {
            LayoutInflater.from(parent.context).inflate(R.layout.item_button, parent, false) as Button
        } else {
            convertView as Button
        }
        val item = items[position]

        button.text = item.text
        button.setOnClickListener { item.action() }
        return button
    }
}
