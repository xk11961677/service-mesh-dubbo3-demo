plugins {
    `kotlin-dsl`
}

repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
}

// 设置编译版本
kotlinDslPluginOptions{
    jvmTarget.set("17")
}

/* 与build.gradle.kts的plugins标签冲突 */
//dependencies {
//  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
//  implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.7.0")
//}
dependencies {
    implementation("gradle.plugin.com.google.cloud.tools:jib-gradle-plugin:3.2.1")
}