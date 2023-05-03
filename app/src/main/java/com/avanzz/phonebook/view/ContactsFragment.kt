package com.avanzz.phonebook.view

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentUris
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avanzz.phonebook.R
import com.avanzz.phonebook.adapter.ContactsAdapter
import com.avanzz.phonebook.model.ContactPojo

class ContactsFragment : Fragment() {

    private lateinit var contactsRecyclerView: RecyclerView
    private lateinit var contacts: List<ContactPojo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        fun newInstance(): ContactsFragment {
            return ContactsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)

        contactsRecyclerView = view.findViewById(R.id.contactsRecyclerView)
        contactsRecyclerView.layoutManager = LinearLayoutManager(context)

        LoadContactsTask().execute()

        return view
    }

    inner class LoadContactsTask : AsyncTask<Void, Void, ArrayList<ContactPojo>>() {

        private lateinit var progressDialog: ProgressDialog

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog(context)
            progressDialog.setMessage("Loading Contacts...")
            progressDialog.setCancelable(false)
            progressDialog.show()
        }

        @SuppressLint("Range")
        override fun doInBackground(vararg params: Void?): ArrayList<ContactPojo> {
            Log.d("tushar","getcontacts")
            val contacts = ArrayList<ContactPojo>()

            val contentResolver = requireActivity().contentResolver
            val cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    var photoUri: Uri? = null
                    if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_ID)) > 0) {
                        photoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id.toLong())
                        photoUri = Uri.withAppendedPath(photoUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
                    }

                    if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        val phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(id),
                            null
                        )
                        while (phoneCursor!!.moveToNext()) {
                            val phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER))
                            val contact = ContactPojo(id, name, phoneNumber)

                            if (photoUri != null) {
                                contact.photoUri = photoUri.toString()
                            }

                            contacts.add(contact)
                        }
                        phoneCursor.close()
                    }
                }
            }

            cursor?.close()
            return contacts
        }

        override fun onPostExecute(result: ArrayList<ContactPojo>?) {
            super.onPostExecute(result)

            if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }

            if (result != null && result.size > 0) {
                val adapter = ContactsAdapter(requireContext(), result)
                contactsRecyclerView.adapter = adapter
            } else {
                Toast.makeText(context, "No contacts found", Toast.LENGTH_SHORT).show()
            }
        }
    }

}


