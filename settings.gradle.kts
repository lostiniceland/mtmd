rootProject.name = "mtmd"
includeBuild("backend-quarkus")
includeBuild("frontend-vaadin")

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
    }
}
