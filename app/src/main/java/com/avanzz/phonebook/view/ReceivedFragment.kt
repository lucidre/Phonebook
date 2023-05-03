package com.avanzz.phonebook.view

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.CallLog
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avanzz.phonebook.R
import com.avanzz.phonebook.adapter.CallListAdapter
import com.avanzz.phonebook.utils.CallType
import com.avanzz.phonebook.model.CallLogItem

class ReceivedFragment : Fragment()  {

    private lateinit var receivedRecyclerView: RecyclerView
    private lateinit var adapter: CallListAdapter
    private lateinit var progress: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        fun newInstance(): ReceivedFragment {
            return ReceivedFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_received, container, false)
        receivedRecyclerView = view.findViewById(R.id.receivedRecyclerView)
        receivedRecyclerView.layoutManager = LinearLayoutManager(activity)
        //adapter = CallListAdapter()
        //receivedRecyclerView.adapter = adapter
        progress = ProgressDialog(activity)

        LoadCallListTask().execute(CallType.RECEIVED)

        return view
    }

    private inner class LoadCallListTask : AsyncTask<CallType, Void, List<CallLogItem>>() {

        private lateinit var type: CallType

        override fun onPreExecute() {
            super.onPreExecute()
            progress.setMessage("Loading calls...")
            progress.show()
        }

        override fun doInBackground(vararg params: CallType?): List<CallLogItem> {
            if (params.isNotEmpty()) {
                type = params[0] ?: CallType.RECEIVED
            } else {
                type = CallType.RECEIVED
            }

            val uri = CallLog.Calls.CONTENT_URI
            val projection = arrayOf(
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION,
                CallLog.Calls.TYPE
            )

            val selection = "${CallLog.Calls.TYPE} = ${type.value}"
            val sortOrder = "${CallLog.Calls.DATE} DESC"
            val cursor = activity?.contentResolver?.query(uri, projection, selection, null, sortOrder)

            /*val sortOrder = "${CallLog.Calls.DATE} ASC"
            val cursor = activity?.contentResolver?.query(uri, projection, selection, null, sortOrder)*/

            val callList = mutableListOf<CallLogItem>()
            cursor?.use { c ->
                val numberIndex = c.getColumnIndex(CallLog.Calls.NUMBER)
                val dateIndex = c.getColumnIndex(CallLog.Calls.DATE)
                val durationIndex = c.getColumnIndex(CallLog.Calls.DURATION)

                while (c.moveToNext()) {
                    val number = c.getString(numberIndex)
                    val date = c.getLong(dateIndex)
                    val duration = c.getInt(durationIndex)
                    val name = getContactName(number) ?: number
                    val callItem = CallLogItem(number, date, duration, name)
                    callList.add(callItem)

                }
            }

            /*val callList = mutableListOf<CallLogItem>()
            for (entry in callMap.entries) {
                val (name, number) = entry.key.split(",")
                val count = entry.value.size
                val date = entry.value.firstOrNull()?.date ?: 0
                val duration = entry.value.sumBy { it.duration.toInt() }
                val callItem = CallLogItem(number, date, duration, type, count, name)
                callList.add(callItem)
            }*/

            return callList
        }

        override fun onPostExecute(result: List<CallLogItem>) {
            super.onPostExecute(result)
            progress.dismiss()
            adapter = CallListAdapter(requireContext(), result)
            receivedRecyclerView.adapter = adapter
        }
    }

    @SuppressLint("Range")
    fun getContactName(number: String): String? {
        val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number))
        val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)
        var name: String? = null
        val cursor = context?.contentResolver?.query(uri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                name = it.getString(it.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME))
            }
        }
        return name
    }

}