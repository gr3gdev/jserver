package com.github.gr3gdev.jserver

/**
 * jOwl Utils.
 *
 * @author Gregory Tardivel
 */
object JServer {

    private var server: Server? = null
    private var client: Client? = null

    /**
     * Init a [Server].
     *
     * @return [Server]
     */
    fun server(): Server {
        if (server == null) {
            server = Server()
        }
        return server!!
    }

    /**
     * Init a [Client].
     *
     * @param host Host address
     * @param port Port number
     * @return [Client]
     */
    fun client(host: String, port: Int): Client {
        if (client == null) {
            client = Client().connect(host, port)
        }
        return client!!
    }

}