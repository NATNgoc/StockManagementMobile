package com.example.keygencetestapp.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun getCurDay(): String = SimpleDateFormat("yyyyMMdd").format(Date())
fun getTimeStamp() = SimpleDateFormat("yyyyMMddss").format(Date())
fun formatTimestamp(timestamp: String): String {
    val instant = Instant.ofEpochMilli(timestamp.toLong())
    val zonedDateTime = instant.atZone(ZoneId.of("UTC"))
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")
    return zonedDateTime.format(formatter)
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

fun generateUnique20DigitId(): String {
    val timestamp = System.currentTimeMillis().toString() // 13 chữ số
    val randomLength = 20 - timestamp.length // 7 chữ số còn lại
    val randomPart = (1..randomLength)
        .map { ('0'..'9').random() }
        .joinToString("")
    return timestamp + randomPart
}

fun getLastUpdatedString(lastUpdatedTime: Long): String {
    val currentTime = System.currentTimeMillis()
    val diffMillis = currentTime - lastUpdatedTime

    val diffHours = diffMillis / (1000 * 60 * 60)
    val diffDays = diffMillis / (1000 * 60 * 60 * 24)
    val diffMonths = diffDays / 30

    return when {
        diffHours < 24 -> "Update ${diffHours.toInt()} hours ago"
        diffDays < 30 -> "Function… ${diffDays.toInt()} days trước"
        else -> "Function… ${diffMonths.toInt()} months ago"
    }
}