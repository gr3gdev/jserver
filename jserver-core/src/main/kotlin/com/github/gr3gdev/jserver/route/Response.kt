package com.github.gr3gdev.jserver.route

import com.github.gr3gdev.jserver.logger.Logger
import java.io.IOException

/**
 * ResponseData.
 *
 * @author Gregory Tardivel
 */
class Response() {

    internal var content = ByteArray(0)
    internal var status = HttpStatus.OK
    internal var contentType = "text/html"
    internal var redirect: String? = null
    internal val cookies = HashMap<String, String>()

    constructor(status: HttpStatus, contentType: String, content: ByteArray) : this(status) {
        this.contentType = contentType
        this.content = content
    }

    constructor(url: String) : this() {
        this.redirect = url
    }

    constructor(status: HttpStatus) : this() {
        this.status = status
    }

    constructor(status: HttpStatus, file: File) : this(status) {
        this.file(file)
    }

    fun redirect(url: String): Response {
        this.redirect = url
        return this
    }

    fun cookie(name: String, value: String): Response {
        cookies[name] = value
        return this
    }

    /**
     * Response from file.
     */
    fun file(file: File) {
        Logger.debug("Load content from $file")
        contentType = file.contentType
        try {
            content = file.content
        } catch (exc: IOException) {
            Logger.error("Content not loaded : $file", exc)
        }
    }

    override fun toString(): String {
        return "Response(status=$status, contentType='$contentType')"
    }

    class File(private val path: String, internal val contentType: String) {
        internal val content: ByteArray = javaClass.getResourceAsStream(path).readBytes()
        override fun toString(): String {
            return "File(path='$path', contentType='$contentType')"
        }
    }

}