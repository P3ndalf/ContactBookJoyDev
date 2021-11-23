package com.example.contactbook.ui.views.mainscreens.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.contactbook.R
import com.example.contactbook.data.entities.Contact

class ContactsAdapter: RecyclerView.Adapter<ContactsAdapter.MyViewHolder>() {

    private var contactsList = emptyList<Contact>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.contact_row, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = contactsList[position]
        holder.itemView.findViewById<TextView>(R.id.nameTV).text = currentItem.contactName
        holder.itemView.findViewById<TextView>(R.id.phoneNumberTV).text = currentItem.phoneNumber
    }

    fun setData(contacts: List<Contact>) {
        this.contactsList = contacts
        notifyDataSetChanged()
    }
}