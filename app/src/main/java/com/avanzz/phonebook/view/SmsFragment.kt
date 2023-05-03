package com.avanzz.phonebook.view

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Telephony
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avanzz.phonebook.R
import com.avanzz.phonebook.adapter.SmsAdapter
import com.avanzz.phonebook.model.SmsPojo

class SmsFragment : Fragment() {

    private lateinit var smsRecyclerView: RecyclerView
    private lateinit var SMS: List<SmsPojo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        fun newInstance(): SmsFragment {
            return SmsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sms, container, false)

        smsRecyclerView = view.findViewById(R.id.smsRecyclerView)
        smsRecyclerView.layoutManager = LinearLayoutManager(activity)

        RetrieveSmsTask().execute()

        return view
    }

    inner class RetrieveSmsTask() :
        AsyncTask<Void, Void, ArrayList<SmsPojo>>() {

        private val progressDialog = ProgressDialog(activity)

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog.setMessage("Loading SMS messages...")
            progressDialog.setCancelable(false)
            progressDialog.show()
        }

        @SuppressLint("Range")
        override fun doInBackground(vararg params: Void?): ArrayList<SmsPojo> {
            val smsList = ArrayList<SmsPojo>()
            val cursor = activity?.contentResolver?.query(
                Telephony.Sms.Inbox.CONTENT_URI,
                arrayOf(Telephony.Sms.Inbox._ID, Telephony.Sms.Inbox.ADDRESS, Telephony.Sms.Inbox.BODY),
                null,
                null,
                Telephony.Sms.Inbox.DEFAULT_SORT_ORDER
            )
            cursor?.let {
                while (it.moveToNext()) {
                    val id = it.getLong(it.getColumnIndex(Telephony.Sms.Inbox._ID))
                    val address = it.getString(it.getColumnIndex(Telephony.Sms.Inbox.ADDRESS))
                    val body = it.getString(it.getColumnIndex(Telephony.Sms.Inbox.BODY))

                    val sms = SmsPojo(id, address, body)
                    smsList.add(sms)
                }
                it.close()
            }
            return smsList
        }

        override fun onPostExecute(result: ArrayList<SmsPojo>?) {
            super.onPostExecute(result)
            progressDialog.dismiss()

            if (result != null && result.size > 0) {
                val adapter = SmsAdapter(result)
                smsRecyclerView.adapter = adapter
            } else {
                Toast.makeText(context, "No contacts found", Toast.LENGTH_SHORT).show()
            }

        }

    }

}