plugins {
    id("war")
    id("com.vaadin") version "0.14.5.1"
    id("io.openliberty.tools.gradle.Liberty") version "3.1.2"
    id("org.openapi.generator") version "5.1.0"
}

defaultTasks("clean", "build")

repositories {
    mavenLocal()
    mavenCentral()
}

// example of how to configure the Gradle Vaadin Plugin
//vaadin {
//    pnpmEnable = false
//}

val vaadinPlatformGroupId: String by project
val vaadinPlatformArtifactId: String by project
val vaadinPlatformVersion: String by project


dependencies {
    implementation(enforcedPlatform("${vaadinPlatformGroupId}:${vaadinPlatformArtifactId}:${vaadinPlatformVersion}"))
    implementation("javax:javaee-api:8.0.1")

    // Vaadin 14
    implementation("com.vaadin:vaadin-core")
    // logging
    implementation("org.slf4j:slf4j-simple:1.7.30")
}

// customizing source-sets to add openapi generated files
sourceSets {
    main {
        java {
            srcDir("$buildDir/generate-resources/main/src/gen/java/")
        }
    }
}

tasks {
    withType<org.openapitools.generator.gradle.plugin.tasks.GenerateTask> {
        generatorName.set("jaxrs-cxf-client")
        inputSpec.set("$rootDir/../icy-v1.0.yaml")
        apiPackage.set("com.mtmd.ui.infrastructure.jaxrs.client.gen")
        modelPackage.set("com.mtmd.ui.infrastructure.jaxrs.client.gen.types")
        configOptions.put("dateLibrary", "java8")
    }

}

//liberty {
//    server {
//        name = 'myServer'
//        clean = true
//    }
//}
