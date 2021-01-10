package com.github.gr3gdev.jserver.route

import java.io.File
import java.io.FileInputStream
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

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

    fun file(filePath: String) {
        val fileURI = javaClass.getResource(filePath)?.toURI()
                ?: throw RuntimeException("File not found: $filePath")
        val path = Paths.get(fileURI)
        contentType = Files.probeContentType(path)
        content = File(fileURI).inputStream().readBytes()
    }

}