package com.github.gr3gdev.jserver.route

import com.github.gr3gdev.jserver.logger.Logger

/**
 * ResponseData.
 *
 * @author Gregory Tardivel
 */
class ResponseData {

    var content = ByteArray(0)

    var status = HttpStatus.OK
    var contentType = "text/html"
    var redirect: String? = null
    val cookies = HashMap<String, String>()

    fun file(file: File) {
        Logger.debug("Load content from $file")
        contentType = file.contentType
        content = javaClass.getResourceAsStream(file.path).readBytes()
    }

    class File(val path: String, val contentType: String) {
        override fun toString(): String {
            return "File(path='$path', contentType='$contentType')"
        }
    }

}