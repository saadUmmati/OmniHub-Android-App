package com.example.omnihub.classes

import com.example.omnihub.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ContactAdapter(private var contacts: List<Contact>) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvContactName)
        val number: TextView = view.findViewById(R.id.tvContactNumber)
        val image: ImageView = view.findViewById(R.id.ivContact)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.name.text = contact.name
        holder.number.text = contact.phoneNumber

        // Luxury touch: Placeholder par violet/gold tint
        holder.image.setImageResource(R.drawable.`user`)
    }

    override fun getItemCount() = contacts.size

    fun updateData(newList: List<Contact>) {
        this.contacts = newList
        notifyDataSetChanged()
    }
}