rootProject.name = "jserver"
include("jserver-core", "jserver-plugins")
include("jserver-plugins:jserver-plugin-html")
findProject(":jserver-plugins:jserver-plugin-html")?.name = "jserver-plugin-html"
