package com.github.gr3gdev.jserver.thymeleaf;

import com.github.gr3gdev.jserver.plugin.ServerPlugin;
import com.github.gr3gdev.jserver.route.HttpStatus;
import com.github.gr3gdev.jserver.route.Response;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.AbstractContext;
import org.thymeleaf.messageresolver.StandardMessageResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

/**
 * ThymeleafPlugin.
 *
 * @author Gregory Tardivel
 */
public class ThymeleafPlugin implements ServerPlugin {

    private static final TemplateEngine templateEngine = new TemplateEngine();

    public ThymeleafPlugin() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver(Thread.currentThread().getContextClassLoader());
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheTTLMs(3600000L); // 1h by default
        templateResolver.setCacheable(true);
        templateResolver.setCheckExistence(true);
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.setMessageResolver(new StandardMessageResolver());
    }

    public Response process(final String page, final Map<String, Object> variables, final Locale locale) {
        final String content = templateEngine.process(page, new JServerContext(variables, locale));
        return new Response(HttpStatus.OK, "text/html", content.getBytes(StandardCharsets.UTF_8));
    }

    private static final class JServerContext extends AbstractContext {

        public JServerContext(Map<String, Object> variables, Locale locale) {
            super(locale, variables);
        }
    }
}
