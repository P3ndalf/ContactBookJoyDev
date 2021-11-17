package com.example.contactbook.screens.List

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.contactbook.R
import com.example.contactbook.data.entities.User

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var userList = emptyList<User>()

    class MyViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false))
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]
        with(holder){
            itemView.findViewById<TextView>(R.id.firstname).text = currentItem.firstName
            itemView.findViewById<TextView>(R.id.lastname).text = currentItem.lastName
            itemView.findViewById<TextView>(R.id.email).text = currentItem.email
        }
    }

    fun setData(users : List<User>){
        this.userList = users
        notifyDataSetChanged()
    }

}