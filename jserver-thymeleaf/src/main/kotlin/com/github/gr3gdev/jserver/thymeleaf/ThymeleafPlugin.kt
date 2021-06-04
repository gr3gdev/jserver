package com.github.gr3gdev.jserver.thymeleaf

import com.github.gr3gdev.jserver.plugin.ServerPlugin
import com.github.gr3gdev.jserver.route.HttpStatus
import com.github.gr3gdev.jserver.route.Response
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.AbstractContext
import org.thymeleaf.messageresolver.StandardMessageResolver
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.nio.charset.StandardCharsets
import java.util.*

class ThymeleafPlugin(cacheTTLMs: Long = 3600000L) : ServerPlugin {

    private val templateEngine = TemplateEngine()

    init {
        val templateResolver = ClassLoaderTemplateResolver(ThymeleafPlugin::class.java.classLoader)
        templateResolver.templateMode = TemplateMode.HTML
        templateResolver.prefix = "/templates/"
        templateResolver.suffix = ".html"
        templateResolver.cacheTTLMs = cacheTTLMs // 1h by default
        templateResolver.isCacheable = true
        templateResolver.checkExistence = true
        templateEngine.setTemplateResolver(templateResolver)
        templateEngine.setMessageResolver(StandardMessageResolver())
    }

    fun process(page: String, variables: Map<String, Any?> = emptyMap(), locale: Locale = Locale.FRANCE): Response {
        val content = templateEngine.process(page, JServerContext(variables, locale))
        return Response(HttpStatus.OK, "text/html", content.toByteArray(StandardCharsets.UTF_8))
    }

    internal class JServerContext(variables: Map<String, Any?>, locale: Locale) : AbstractContext(locale, variables)

}
