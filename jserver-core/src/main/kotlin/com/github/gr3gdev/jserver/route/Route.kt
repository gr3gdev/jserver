package com.github.gr3gdev.jserver.route

import com.github.gr3gdev.jserver.http.Request
import com.github.gr3gdev.jserver.plugin.ServerPlugin

class Route(val request: Request, private val plugins: Array<out ServerPlugin>?) {

    /**
     * Get plugin.
     *
     * @param pClass Class of plugin
     * @return <P>
     */
    @Suppress("UNCHECKED_CAST")
    fun <P : ServerPlugin> plugin(pClass: Class<P>): P {
        if (plugins == null) {
            throw RuntimeException("No plugins defined")
        }
        return (plugins.firstOrNull { it.javaClass == pClass }
                ?: throw RuntimeException("Plugin undefined ${pClass.simpleName}")) as P
    }

}
