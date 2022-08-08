import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

// apply false 作用: plugins默认立即解析并应用此插件 ， false后可在子项目自行apply plugin
plugins {
    `java-library`
    idea
    kotlin("jvm")  version Versions.KOTLIN_VERSION
    kotlin("kapt") version Versions.KOTLIN_VERSION
    kotlin("plugin.spring") version Versions.KOTLIN_VERSION apply false
    kotlin("plugin.noarg")  version Versions.KOTLIN_VERSION apply false
    kotlin("plugin.allopen") version Versions.KOTLIN_VERSION  apply false
    id("org.springframework.boot") version Versions.SPRING_BOOT_VERSION apply false //  禁用,防止dubbo-api也被使用
    id("io.spring.dependency-management") version Versions.SPRING_DEPENDENCY_MANAGEMENT_VERSION
}

group = "com.demo"

val jarModules by extra(
    listOf(
        project(":dubbo-api")
    )
)

//allprojects {
configure((allprojects-rootProject)) {
    apply {
        plugin("io.spring.dependency-management")
        if(!jarModules.contains(project)) plugin("org.springframework.boot")
    }

    group = "com.demo"
    repositories {
        mavenLocal()
        mavenCentral()
    }

    extra["sb.version"] = Versions.SPRING_BOOT_VERSION
    extra["dubbo.version"] = Versions.DUBBO_VERSION

    val springBootVersion = extra["sb.version"] as String
    val dubboVersion = extra["dubbo.version"] as String

    dependencyManagement {
        imports {
            mavenBom("org.jetbrains.kotlin:kotlin-bom:${Versions.KOTLIN_VERSION}")
            mavenBom("org.springframework:spring-framework-bom:${Versions.SPRING_FRAMEWORK_BOM_VERSION}")
            mavenBom("org.springframework.boot:spring-boot-dependencies:${springBootVersion}")
            mavenBom("org.apache.dubbo:dubbo-dependencies-bom:${dubboVersion}")
            mavenBom("org.apache.dubbo:dubbo-bom:${dubboVersion}")
            mavenBom("com.fasterxml.jackson:jackson-bom:${Versions.JACKSON_BOM_VERSION}")
            mavenBom("io.opentelemetry:opentelemetry-bom:${Versions.OPENTELEMETRY_BOM_VERSION}")
            mavenBom("io.opentelemetry:opentelemetry-bom-alpha:${Versions.OPENTELEMETRY_ALPHA_BOM_VERSION}")
        }
        dependencies {
//            dependency("org.apache.dubbo:dubbo-serialization-kryo:2.7.16")
            dependency("org.apache.dubbo:dubbo-serialization-protobuf:2.7.16")
            dependency("org.javassist:javassist:3.23.1-GA")
        }
    }

    configurations {
        all {
            resolutionStrategy {
                eachDependency { ->
                    if (this.requested.group == "org.springframework") {
                        this.useVersion("5.3.18")
                    }

                    if (this.requested.group == "org.apache.tomcat.embed" && this.requested.module.name == "tomcat-embed-core") {
                        this.useVersion("9.0.62")
                    }
                }
                // 不使用缓存，使用仓库中最新的包 check for updates every build
                cacheChangingModulesFor(0, "seconds")
                cacheDynamicVersionsFor(0, "seconds")
            }
        }
    }
}



subprojects {
    apply {
        plugin("java-library")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.kapt")
        plugin("org.jetbrains.kotlin.plugin.spring")
        plugin("org.jetbrains.kotlin.plugin.noarg")
        plugin("org.jetbrains.kotlin.plugin.allopen")
    }

    val springBootVersion = extra["sb.version"] as String

    dependencies {
        // Speed up processing of AutoConfig's produced by Spring Boot
        annotationProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")
        // Produce Config Metadata for properties used in Spring Boot
        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
        // Speed up processing of AutoConfig's produced by Spring Boot for Kotlin
        kapt("org.springframework.boot:spring-boot-autoconfigure-processor:${springBootVersion}")
        // Produce Config Metadata for properties used in Spring Boot for Kotlin
        kapt("org.springframework.boot:spring-boot-configuration-processor:${springBootVersion}")

        testImplementation("org.springframework.boot:spring-boot-starter-test") {
            exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        }
        testImplementation("io.mockk:mockk:1.12.4")

        implementation("com.google.guava:guava:${Versions.GUAVA_VERSION}")

        //region opentelemetry
        implementation("io.opentelemetry:opentelemetry-api")
        //implementation("io.opentelemetry:opentelemetry-sdk")
        implementation("io.opentelemetry:opentelemetry-extension-annotations")
        // javaagent 方式不自定义就不需要了
        //implementation("io.opentelemetry:opentelemetry-exporter-otlp")
        //implementation("io.opentelemetry:opentelemetry-exporter-zipkin")
        implementation("io.opentelemetry:opentelemetry-extension-kotlin")

        //alpha module begin
        implementation("io.opentelemetry:opentelemetry-semconv")
        //暂时注释掉，因不使用javaagent时，也会开启，需要找个配置关闭选项 ，测试使用javaagent不使用也可以
        //implementation("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure")
        //alpha module end
        //endregion
    }

    java {
        withSourcesJar()
        withJavadocJar()
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kapt {
        useBuildCache = false
        val additionalMetadataLocations = "org.springframework.boot.configurationprocessor.additionalMetadataLocations"
        arguments {
            arg(additionalMetadataLocations,"$projectDir/src/main/resources")
        }
    }

    /*想要配置此功能需要删掉plugins的 apply false
    noArg {
        annotation("com.yzdzy.kotlin.chapter4.annotations.PoKo")
    }
    */

//    allOpen {
//        annotation("org.apache.dubbo.config.annotation.DubboService")
//        annotation("org.apache.dubbo.config.annotation.DubboReference")
//    }

    tasks {

        withType<Jar>().configureEach {
            // jar包才开启,springboot不开启
            enabled = jarModules.contains(this.project)
            //if(enabled) {
            //println("jar = ${rootProject.name}-${this.project.name}")
            //archiveBaseName.set("${rootProject.name}-${this.project.name}")
            //}
            duplicatesStrategy = DuplicatesStrategy.WARN

            manifest {
                attributes(mapOf(
                    "Implementation-Title" to project.name,
                    "Implementation-Version" to rootProject.version,
                    "Created-By" to "${System.getProperty("java.version")} (${System.getProperty("java.specification.vendor")})"
                ))
            }
        }

        withType<BootJar>().configureEach {
            /*if(jarModules.contains(project)) {
                // todo slw 关闭后,因为apply springboot插件还是会生成plain后缀jar包,所以改到apply判断
                //另一种写法,待测试 ， 跳过某个人物,
                // whenTaskAdded {
                //    if (jarModules.contains(project)) {
                //         task.enabled = false
                //    }
                // }
                enabled = false
                archiveClassifier.set("")
            }else {
                enabled = true
                mainClass.set("${rootProject.group}.${this.project.name.substringAfter("-")}.ApplicationKt")
            }*/
            enabled = true
            mainClass.set("${rootProject.group}.${this.project.name.substringAfter("-")}.ApplicationKt")
        }

        withType<JavaCompile>().configureEach {
            options.encoding = "UTF-8"
            options.compilerArgs.addAll(listOf("-parameters", "-deprecation"))
            sourceCompatibility = "17"
            targetCompatibility = "17"
        }

        withType<KotlinCompile>().configureEach {
            kotlinOptions {
                languageVersion = "1.7"
                apiVersion = "1.7"
                freeCompilerArgs = freeCompilerArgs + listOf("-Xjvm-default=all-compatibility","-Xjsr305=strict")
                jvmTarget = "17"
                allWarningsAsErrors = true
            }
        }

        withType<Test>() {
            useJUnitPlatform()
//            jvmArgs("--add-opens java.base/sun.net.util=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.math=ALL-UNNAMED")
//            useTestNG()
        }
    }
}