# jserver
![Gradle tests](https://github.com/gr3gdev/jserver/workflows/Gradle%20tests/badge.svg) ![Gradle Publish](https://github.com/gr3gdev/jserver/workflows/Gradle%20Publish/badge.svg)

Java Light Server

Default port : 9000

*Example :*
```
JServer.server()
    .port(8080)
    .get("/get1", RouteListener(HttpStatus.OK, "application/json", "{\"status\":1}"))
    .get("/get2", RouteListener(HttpStatus.OK, "application/json", "{\"status\":2}"))
    .post("/post", RouteListener(HttpStatus.OK, ResponseData.File("/index.html", "text/html"))
    .process("/put", RequestMethod.PUT, MyCustomListener.put())
    .start()
```
