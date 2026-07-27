package com.vehicleloadcontrol.data.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    fun formatDate(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.format(date)
    }

    fun formatDateTime(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return format.format(date)
    }

    fun getCurrentTimestamp(): Long {
        return System.currentTimeMillis()
    }
}
