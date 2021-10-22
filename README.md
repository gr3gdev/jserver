# jserver
![Gradle tests](https://github.com/gr3gdev/jserver/workflows/Gradle%20tests/badge.svg) ![Gradle Publish](https://github.com/gr3gdev/jserver/workflows/Gradle%20Publish/badge.svg)

Java Light Server running with docker.

Default port : 9000

*Start server :*
```
docker run -p 9000:9000 -v /path/to/my/java/application:/apps gr3gdev/jserver
```
> **/path/to/my/java/application :** path to folder contains JAR file or path to build directory

Example, create an application :
```
- example
| - src
| | - main
| | | - java
| | | | - com.github.gr3gdev.example
| | | | | + MyApp.java
| | | | | + MyRoutes.java
| | | - resources
| | | | - templates
| | | | | + home.html
| | | | - css
| | | | | + example.css
```
```java
public class MyApp {

    @Plugin
    public ThymeleafPlugin thymeleafPlugin() {
        return new ThymeleafPlugin();
    }

}
```
```java
public class MyRoutes {

    @RouteMapping(path = "/home", method = RequestMethod.GET)
    public RouteListener home() {
        return new RouteListener()
                .process(route -> {
                    final Map<String, Object> variables = new HashMap<>();
                    variables.put("var1", "Hello");
                    variables.put("var2", "World !");
                    // Process thymeleaf page src/main/resources/template/home.html
                    return route.plugin(ThymeleafPlugin.class)
                            .process("home", variables, Locale.ENGLISH);
                });
    }

    @RouteMapping(path = "/css/{file}", method = RequestMethod.GET)
    public RouteListener css() {
        return new RouteListener(HttpStatus.OK, "/css/example.css", "text/css");
    }

}
```
```html
<!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Home page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" media="all" th:href="@{css/example.css}"/>
</head>

<body>
    <h1 th:text="#{var1}"></h1>
    <h2 th:text="#{var2}"></h2>
</body>

</html>
```

Development version (server auto reload when rebuild) :
```
docker run -p 9000:9000 -e DEV_MODE=1 -v [...]/example/build:/apps gr3gdev/jserver
```
Production version :
```
docker run -p 9000:9000 -v /path/contains/example/jar/file:/apps gr3gdev/jserver
```