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
    internal val cookies = HashSet<Cookie>()

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

    /**
     * Add a cookie.
     *
     * @param cookie Cookie
     */
    fun cookie(cookie: Cookie): Response {
        cookies.add(cookie)
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

    enum class CookieSameSite {
        STRICT, LAX, NONE;

        fun value() = name.toLowerCase().capitalize()
    }

    class Cookie(
            private val name: String,
            private val value: String,
            private val maxAge: Int = 3600,
            private val domain: String? = null,
            private val path: String? = null,
            private val secure: Boolean = false,
            private val httpOnly: Boolean = true,
            private val sameSite: CookieSameSite = CookieSameSite.LAX
    ) {
        override fun toString(): String {
            val secureValue = if (secure) {
                "; Secure"
            } else {
                ""
            }
            val httpOnlyValue = if (httpOnly) {
                "; HttpOnly"
            } else {
                ""
            }
            val domainValue = if (domain != null) {
                "; Domain=$domain"
            } else {
                ""
            }
            val pathValue = if (path != null) {
                "; Path=$path"
            } else {
                ""
            }
            return "$name=$value; Max-Age=$maxAge$domainValue$pathValue$secureValue$httpOnlyValue; SameSite=${sameSite.value()}"
        }
    }

    class File(private val path: String, internal val contentType: String) {
        internal val content: ByteArray = javaClass.getResourceAsStream(path).readBytes()
        override fun toString(): String {
            return "File(path='$path', contentType='$contentType')"
        }
    }

}