package com.avanzz.phonebook.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.avanzz.phonebook.R
import com.avanzz.phonebook.model.ContactPojo
import com.bumptech.glide.Glide
import java.util.*

class ContactsAdapter(private val context: Context, private val contacts: List<ContactPojo>) :
    RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val phoneTextView: TextView = itemView.findViewById(R.id.phoneTextView)
        val circularView: ImageView = itemView.findViewById(R.id.circularView)
        val circularLayout: RelativeLayout = itemView.findViewById(R.id.circularLayout)
        val circularText: TextView = itemView.findViewById(R.id.circularText)
        val layoutMain: LinearLayout = itemView.findViewById(R.id.layoutMain)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Log.d("tushar", "contact in recycler view::${contacts[position].name}")
        val contact = contacts[position]
        holder.nameTextView.text = contact.name
        holder.phoneTextView.text = contact.phoneNumber

        if (contact.photoUri != null) {
            val imageUrl = contact.photoUri
            Glide.with(context)
                .load(imageUrl)
                .into(holder.circularView)
        } else {
            val initials = getInitials(contact.name)
            //val color = getRandomColor()
            holder.circularView.visibility = View.GONE
            holder.circularLayout.visibility = View.VISIBLE
            //holder.circularLayout.backgroundTintList = ColorStateList.valueOf(color)
            holder.circularText.text = initials
        }

        holder.layoutMain.setOnClickListener {
            showContactDialog(contact)
        }

    }

    private fun showContactDialog(contact: ContactPojo) {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.contact_dialog, null)

        val photoImageView = dialogView.findViewById<ImageView>(R.id.contactImageView)
        val nameTextView = dialogView.findViewById<TextView>(R.id.nameTextView)
        val phoneTextView = dialogView.findViewById<TextView>(R.id.phoneTextView)
        val callButton = dialogView.findViewById<View>(R.id.callButton)
        val smsButton = dialogView.findViewById<View>(R.id.messageButton)

        nameTextView.text = contact.name
        phoneTextView.text = contact.phoneNumber

        if (contact.photoUri != null) {
            val imageUrl = contact.photoUri
            Glide.with(context)
                .load(imageUrl)
                .into(photoImageView)
        } else {
            val initials = getInitials(contact.name)
            photoImageView.visibility = View.GONE
            val circularLayout = dialogView.findViewById<RelativeLayout>(R.id.circularLayoutDialog)
            circularLayout.visibility = View.VISIBLE
            val circularText = dialogView.findViewById<TextView>(R.id.circularTextDialog)
            circularText.text = initials
        }

        callButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${contact.phoneNumber}")
            context.startActivity(intent)
        }

        smsButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("sms:${contact.phoneNumber}")
            context.startActivity(intent)
        }

        builder.setView(dialogView)
        builder.show()
    }

    private fun getInitials(name: String): String {
        val initials = StringBuilder()

        name.split(" ").forEach { word ->
            initials.append(word.firstOrNull())
        }

        return initials.toString().toUpperCase(Locale.getDefault())
    }

    /*private fun getRandomColor(): Int {
        val colors = context.resources.getIntArray(R.array.random_colors)
        val index = (Math.random() * colors.size).toInt()
        return colors[index]
    }*/

    override fun getItemCount(): Int = contacts.size
}