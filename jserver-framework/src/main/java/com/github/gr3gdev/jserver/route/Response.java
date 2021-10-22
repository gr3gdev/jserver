package com.github.gr3gdev.jserver.route;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Response.
 *
 * @author Gregory Tardivel
 */
public class Response {

    private byte[] content;
    private HttpStatus status;
    private String contentType;
    private String redirect;
    private final Set<Cookie> cookies;

    public Response() {
        this.content = new byte[0];
        this.status = HttpStatus.OK;
        this.contentType = "text/html";
        this.cookies = new HashSet<>();
    }

    public Response(HttpStatus status, String contentType, byte[] content) {
        this(status);
        this.contentType = contentType;
        this.content = content;
    }

    public Response(String url) {
        this();
        this.redirect = url;
    }

    public Response(HttpStatus status) {
        this();
        this.status = status;
    }

    public Response(HttpStatus status, String pathFile, String contentType) {
        this(status);
        this.file(pathFile, contentType);
    }

    public Response redirect(String url) {
        this.redirect = url;
        return this;
    }

    /**
     * Add a cookie.
     *
     * @param cookie Cookie
     */
    public Response cookie(Cookie cookie) {
        cookies.add(cookie);
        return this;
    }

    /**
     * Response from file.
     */
    public void file(String pathFile, String contentType) {
        final File file = new File(pathFile, contentType);
        this.contentType = file.contentType;
        try {
            this.content = file.content();
            if (this.content == null) {
                this.status = HttpStatus.NOT_FOUND;
                this.content = file.path.getBytes(StandardCharsets.UTF_8);
            }
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    public byte[] getContent() {
        return Optional.ofNullable(content).orElseGet(() -> new byte[0]);
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public Set<Cookie> getCookies() {
        return cookies;
    }

    public static Cookie createCookie(String name, String value) {
        return new Cookie(name, value);
    }

    enum CookieSameSite {
        STRICT, LAX, NONE;

        public String value() {
            return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
        }
    }

    public static class Cookie {
        private final String name;
        private final String value;
        private int maxAge = 3600;
        private String domain = null;
        private String path = null;
        private Boolean secure = false;
        private Boolean httpOnly = true;
        private CookieSameSite sameSite = CookieSameSite.LAX;

        Cookie(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public int getMaxAge() {
            return maxAge;
        }

        public void setMaxAge(int maxAge) {
            this.maxAge = maxAge;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Boolean getSecure() {
            return secure;
        }

        public void setSecure(Boolean secure) {
            this.secure = secure;
        }

        public Boolean getHttpOnly() {
            return httpOnly;
        }

        public void setHttpOnly(Boolean httpOnly) {
            this.httpOnly = httpOnly;
        }

        public CookieSameSite getSameSite() {
            return sameSite;
        }

        public void setSameSite(CookieSameSite sameSite) {
            this.sameSite = sameSite;
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder(name)
                    .append("=").append(value).append("; ")
                    .append("Max-Age=").append(maxAge);
            if (domain != null) {
                builder.append("; Domain=").append(domain);
            }
            if (path != null) {
                builder.append("; Path=").append(path);
            }
            if (Boolean.TRUE.equals(secure)) {
                builder.append("; Secure");
            }
            if (Boolean.TRUE.equals(httpOnly)) {
                builder.append("; HttpOnly");
            }
            builder.append("; SameSite=").append(sameSite.value());
            return builder.toString();
        }
    }

    static class File {

        private final String path;
        private final String contentType;

        public File(String path, String contentType) {
            if (path.startsWith("/")) {
                this.path = path.substring(1);
            } else {
                this.path = path;
            }
            this.contentType = contentType;
        }

        private byte[] content() throws IOException {
            final InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            if (resourceAsStream == null) {
                System.out.println("Content not found : " + path);
                return null;
            } else {
                return resourceAsStream.readAllBytes();
            }
        }
    }

}
