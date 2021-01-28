package com.github.gr3gdev.jserver.logger

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Logger
 *
 * @author Gregory Tardivel
 */
object Logger {

    private val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    enum class Level(val priority: Int) {
        DEBUG(99), INFO(10), WARN(2), ERROR(1)
    }

    private var level = Level.INFO

    fun changeLevel(newLevel: Level) {
        level = newLevel
    }

    private fun log(msg: Any, logLevel: Level) {
        if (logLevel.priority <= level.priority) {
            println("${LocalDateTime.now().format(format)} [${logLevel.name}] $msg")
        }
    }

    fun info(msg: Any) {
        log(msg, Level.INFO)
    }

    fun debug(msg: Any) {
        log(msg, Level.DEBUG)
    }

    fun warn(msg: Any) {
        log(msg, Level.WARN)
    }

    fun error(msg: Any) {
        log(msg, Level.ERROR)
    }

    fun error(msg: Any, exc: Throwable) {
        log("$msg : ${exc.stackTrace.joinToString("\n")}", Level.ERROR)
    }

}