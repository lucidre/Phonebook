package com.avanzz.phonebook.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.avanzz.phonebook.R
import com.avanzz.phonebook.model.SmsPojo

class SmsAdapter(private val smsList: ArrayList<SmsPojo>) :
    RecyclerView.Adapter<SmsAdapter.SmsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.sms_item, parent, false)
        return SmsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SmsViewHolder, position: Int) {
        val sms = smsList[position]
        val initials = getInitials(sms.address)
        holder.circularText.text = initials
        holder.addressTextView.text = sms.address
        holder.bodyTextView.text = sms.body
    }

    private fun getInitials(name: String): String {
        val words = name.split(" ")
        return when {
            words.size > 2 -> "${words[0][0]}${words[words.lastIndex][0]}"
            words.isNotEmpty() -> "${words[0][0]}"
            else -> ""
        }
    }

    override fun getItemCount(): Int = smsList.size

    inner class SmsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val addressTextView: TextView = view.findViewById(R.id.addressTextView)
        val bodyTextView: TextView = view.findViewById(R.id.bodyTextView)
        val circularText: TextView = view.findViewById(R.id.circularText)
    }
}