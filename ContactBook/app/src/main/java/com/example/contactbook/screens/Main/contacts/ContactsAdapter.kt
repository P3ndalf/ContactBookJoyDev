package com.example.contactbook.screens.Main.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.contactbook.R
import com.example.contactbook.data.entities.Contact
import com.example.contactbook.screens.List.ListAdapter

class ContactsAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var contactsList = emptyList<Contact>()

    class MyViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.MyViewHolder {
        return ListAdapter.MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.contact_row, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    override fun onBindViewHolder(holder: ListAdapter.MyViewHolder, position: Int) {
        val currentItem = contactsList[position]
        holder.itemView.findViewById<TextView>(R.id.nameTV).text = currentItem.contactName
        holder.itemView.findViewById<TextView>(R.id.phoneNumberTV).text = currentItem.phoneNumber
    }

    fun setData(contacts : List<Contact>){
        this.contactsList = contacts
        notifyDataSetChanged()
    }
}