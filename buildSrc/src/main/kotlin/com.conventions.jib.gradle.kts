plugins {
    id("com.google.cloud.tools.jib")
}

jib {
    from {
        image = "openjdk:17.0.2-oracle"
    }
    to {
        image = "registry.cn-hangzhou.aliyuncs.com/sky11961677/dev:${project.name}-${rootProject.version}"
        auth {
            username = "${System.getenv("JIB_USERNAME")?:System.getProperty("JIB_USERNAME")}"
            password = "${System.getenv("JIB_PASSWORD")?:System.getProperty("JIB_PASSWORD")}"
        }
    }
    extraDirectories.paths {
        path {
            setFrom(file("${rootDir}/scripts/extra").toPath())
            into = "/${project.name}"
        }
    }
    container {
        //environment = mapOf("JAVA_TOOL_OPTIONS" to "")
        //args = listOf()
        appRoot = "/${project.name}"
        workingDirectory = "/${project.name}"
        creationTime = "USE_CURRENT_TIMESTAMP"
        //ports = listOf("8082","20880")
        mainClass = "${rootProject.group}.${project.name.substringAfter("-")}.ApplicationKt"
        jvmFlags = listOf(
            "-server",
            "-Djava.awt.headless=true",
            "--add-opens=java.base/sun.net.util=ALL-UNNAMED",
            "--add-opens=java.base/java.lang=ALL-UNNAMED",
            "--add-opens=java.base/java.math=ALL-UNNAMED"
        )
    }
}