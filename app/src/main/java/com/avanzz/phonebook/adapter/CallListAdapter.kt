package com.avanzz.phonebook.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.avanzz.phonebook.R
import com.avanzz.phonebook.model.CallLogItem
import java.text.DateFormat
import java.util.*

class CallListAdapter(private val context: Context, private val items: List<CallLogItem>)
    : RecyclerView.Adapter<CallListAdapter.CallViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.call_item, parent, false)
        return CallViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CallViewHolder, position: Int) {
        val item = items[position]
        holder.nameTextView.text = item.name
        holder.numberTextView.text = item.number
        holder.dateTextView.text = DateFormat.getDateTimeInstance().format(Date(item.date))
        holder.durationTextView.text = context.getString(
            R.string.duration_format,
            item.duration / 60, item.duration % 60
        )
        holder.itemView.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${item.number}")
            context.startActivity(intent)
        })
    }

    override fun getItemCount(): Int = items.size

    inner class CallViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val numberTextView: TextView = itemView.findViewById(R.id.numberTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val durationTextView: TextView = itemView.findViewById(R.id.durationTextView)
        //val typeTextView: TextView = itemView.findViewById(R.id.typeTextView)
    }

}