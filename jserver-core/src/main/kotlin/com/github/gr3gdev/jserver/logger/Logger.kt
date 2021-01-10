package com.github.gr3gdev.jserver.logger

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Logger {

    private val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    enum class Level(val priority: Int) {
        DEBUG(99), INFO(10), ERROR(1)
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

    fun error(msg: Any) {
        log(msg, Level.ERROR)
    }

}