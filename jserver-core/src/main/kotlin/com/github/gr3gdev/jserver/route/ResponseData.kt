package com.github.gr3gdev.jserver.route

import com.github.gr3gdev.jserver.logger.Logger
import java.io.IOException

/**
 * ResponseData.
 *
 * @author Gregory Tardivel
 */
class ResponseData() {

    var content = ByteArray(0)

    var status = HttpStatus.OK
    var contentType = "text/html"
    var redirect: String? = null
    val cookies = HashMap<String, String>()

    constructor(status: HttpStatus, contentType: String, content: ByteArray): this(status) {
        this.contentType = contentType
        this.content = content
    }

    constructor(redirect: String): this() {
        this.redirect = redirect
    }

    constructor(status: HttpStatus): this() {
        this.status = status
    }

    constructor(status: HttpStatus, file: File): this(status) {
        this.file(file)
    }

    /**
     * Response from file.
     */
    fun file(file: File) {
        Logger.debug("Load content from $file")
        contentType = file.contentType
        try {
            content = javaClass.getResourceAsStream(file.path).readBytes()
        } catch (exc: IOException) {
            Logger.error("Content not loaded : $file", exc)
        }
    }

    class File(val path: String, val contentType: String) {
        override fun toString(): String {
            return "File(path='$path', contentType='$contentType')"
        }
    }

}