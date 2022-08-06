plugins {
    id("com.conventions.jib")
}

dependencies {
    implementation(project(":dubbo-api"))
    implementation("org.apache.dubbo:dubbo-spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.apache.dubbo:dubbo-rpc-triple")
//    implementation("org.apache.dubbo:dubbo-serialization-kryo")
    implementation("org.apache.dubbo:dubbo-serialization-protobuf")

    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.javassist:javassist")
}

description = "dubbo-demo-provider"

jib {
    container {
        ports = listOf("8080","20880")
    }
}