plugins {
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(components["java"])

            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }

            pom {
                name.set("dubbo3-demo")
                description.set("service mesh dubbo3-demo")
                url.set("https://github.com/xk11961677/service-mesh-dubbo3-demo/blob/master/README.md")
                properties.set(mapOf("kotlin.version" to Versions.KOTLIN_VERSION))
                licenses {
                    license {
                        name.set("The MIT License")
                        url.set("https://www.mit-license.org")
                    }
                }
                developers {
                    developer {
                        id.set("sky")
                        name.set("sky")
                        email.set("shen11961677@163.com")
                    }
                }
                scm {
                    connection.set("scm:git://github.com/xk11961677/service-mesh-dubbo3-demo.git")
                    developerConnection.set("scm:git:ssh://git@github.com:xk11961677/service-mesh-dubbo3-demo.git")
                    url.set("https://github.com/xk11961677/service-mesh-dubbo3-demo.git")
                }
                organization {
                    name.set("sky")
                    url.set("https://sky-blogs.netlify")
                }
                issueManagement {
                    system.set("github")
                    url.set("https://github.com/xk11961677/service-mesh-dubbo3-demo/issues")
                }
            }
        }
    }

    repositories {
        maven {
            credentials {
                username = "${System.getenv("NEXUS_USERNAME")?:System.getProperty("NEXUS_USERNAME")}"
                password = "${System.getenv("NEXUS_PASSWORD")?:System.getProperty("NEXUS_PASSWORD")}"
            }
            isAllowInsecureProtocol = true

            val projectVersionStr = project.version.toString()

            if (projectVersionStr.endsWith("RELEASE")) {
                url = uri("https://repo.rdc.aliyun.com/repository/109496-release-o4UFqu")
            }
            if (projectVersionStr.endsWith("SNAPSHOT")) {
                url = uri("https://repo.rdc.aliyun.com/repository/109496-snapshot-yQkopR")
            }
            if (!projectVersionStr.endsWith("RELEASE") && !projectVersionStr.endsWith("SNAPSHOT")) {
                throw IllegalArgumentException("版本后缀不符合规范")
            }
        }
    }
}