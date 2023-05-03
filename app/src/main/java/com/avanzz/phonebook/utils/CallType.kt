package com.avanzz.phonebook.utils

import android.provider.CallLog

enum class CallType(val value: Int) {
    RECEIVED(CallLog.Calls.INCOMING_TYPE),
    DIALED(CallLog.Calls.OUTGOING_TYPE),
    MISSED(CallLog.Calls.MISSED_TYPE)
}
