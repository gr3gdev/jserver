package com.github.gr3gdev.jserver.route

/**
 * ResponseData.
 *
 * @author Gregory Tardivel
 */
class ResponseData {

    var status = HttpStatus.OK
    var contentType = "text/html"
    lateinit var content: String

}