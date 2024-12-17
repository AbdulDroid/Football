package droid.abdul.football.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.Calendar

suspend fun hasInternetConnection(dispatcher: CoroutineDispatcher): Boolean = withContext(dispatcher) {
    try {
        // Connect to api to check for connection
        val timeoutMs = 1500
        val socket = Socket()
        val socketAddress = InetSocketAddress("api.football-data.org", 443)

        socket.connect(socketAddress, timeoutMs)
        socket.close()
        true
    } catch (e: IOException) {
        e.printStackTrace()
        false
    }
}

/**
 * Converts a [Date] object to its string representation
 * @param date current date from user device
 * @return date [String] in the format 'yyyy-MM-dd'
 */
fun getDate(date: Date): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(date)
}

/**
 * Extracts year from a date string
 * @param date the date string to be formatted in the format 'yyyy-MM-dd'
 * @return [String] formatted date string in the format 'yyyy'
 */
fun getYear(date: String): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val d = dateFormat.parse(date)
    val formatter = SimpleDateFormat("yyyy", Locale.getDefault())
    return d?.let { formatter.format(it) } ?: "N/A"
}

/**
 * Extracts year in short form from a date string
 * @param date [String] date string to be formatted in format 'yyyy-MM-dd
 * @return [String] formatted date in the format 'yy'
 */
fun getYearShort(date: String): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val d = dateFormat.parse(date)
    val formatter = SimpleDateFormat("yy", Locale.getDefault())
    return d?.let { formatter.format(it) } ?: "N/A"
}

/**
 * Extract time from the provided date string for UTC times
 * @param date [String] date in full format yyyy-MM-dd'T'HH:mm:ss'Z'
 * @return [String] formatted date in format 'HH:mm'
 */
fun getTime(date: String): String {
    var dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val d = dateFormat.parse(date)
    dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getDefault()
    return d?.let { dateFormat.format(it) } ?: "N/A"
}

/**
 * Extract time from the provided date string for Local times
 * @param date [Date] date object
 * @return [String] formatted date in format 'HH:mm'
 */
private fun getTimeDefault(date: Date): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(date)
}

/**
 * Gets time elapsed per match
 * @param startTime date in full format yyyy-MM-dd'T'HH:mm:ss'Z' when the match is to start
 * @param past [Int] modifier for half time and the likes
 * @return [String] minutes played for the match
 */
fun getMatchTime(startTime: String, past: Int): String {
    val cal = Calendar.getInstance()
    cal.add(Calendar.MINUTE, past)
    cal.timeZone = TimeZone.getTimeZone("UTC")
    val matchTime: Date = cal.time
    val start = getTime(startTime)
    val current = getTimeDefault(matchTime)
    return if ((getMinutes(current) - getMinutes(start)) > 97)
        "FT"
    else
        "${getMinutes(current) - getMinutes(start)}"
}

/**
 * Convert date [String] to minutes in [Int]
 * @date A date [String] to be converted in format HH:mm
 * @return [Int] converted date in minutes
 */
private fun getMinutes(date: String): Int {
    return date.substring(0, 2).toInt() * 60 + date.substring(3).toInt()
}