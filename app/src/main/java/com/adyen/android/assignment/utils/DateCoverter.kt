package com.adyen.android.assignment.utils

import java.text.SimpleDateFormat
import java.util.*

class DateConverter {

    companion object {
        fun formatDateToDdMmYyyy(date: String): String {
            val ddMmYyyyFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val yyyyMMddFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val yyyyMMddDate = yyyyMMddFormat.parse(date)

            yyyyMMddDate?.let {
                return ddMmYyyyFormat.format(yyyyMMddDate)
            }

            return ""
        }
    }

}