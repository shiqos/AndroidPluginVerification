package utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Log {

    private var logLevel: Int = 0

    const val DEBUG = 1
    const val INFO = 2
    const val ERROR = 3

    fun setLogLevel(level: Int) {
        logLevel = level
    }

    fun d(msg: String) {
        if (DEBUG >= logLevel) {
            println("[DEBUG] ${formatCurrentTime()} $msg")
        }
    }

    fun i(msg: String) {
        if (INFO >= logLevel) {
            println("[INFO] ${formatCurrentTime()} $msg")
        }
    }

    fun e(msg: String) {
        if (ERROR >= logLevel) {
            println("[ERROR] ${formatCurrentTime()} $msg")
        }
    }

    private fun formatCurrentTime(): String {
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return currentTime.format(formatter)
    }
}