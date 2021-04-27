plugins {
    id("java")
    id ("org.springframework.boot") version "2.4.5"
    id("com.vaadin") version "0.14.5.1"
    id("org.openapi.generator") version "5.1.0"
    id("com.google.cloud.tools.jib") version "3.0.0"
}

defaultTasks("clean", "build")

repositories {
    mavenLocal()
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

val vaadinPlatformGroupId: String by project
val vaadinPlatformArtifactId: String by project
val vaadinPlatformVersion: String by project


dependencies {
    implementation(enforcedPlatform("${vaadinPlatformGroupId}:${vaadinPlatformArtifactId}:${vaadinPlatformVersion}"))
    compileOnly("javax:javaee-api:8.0.1")
    // Vaadin 14
    implementation("com.vaadin:vaadin-core")
    implementation("com.vaadin:vaadin-spring-boot-starter")
    // logging
//    implementation("org.slf4j:slf4j-simple:1.7.30")
    implementation("org.springframework.boot:spring-boot-starter-web:2.4.5")
    // needed due to openapi generator
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("io.gsonfire:gson-fire:1.8.5")

    compileOnly("org.apache.cxf:cxf-rt-rs-client:3.0.15")
    implementation("io.swagger:swagger-jaxrs:1.6.2")
}

// customizing source-sets to add openapi generated files
sourceSets {
    main {
        java {
            srcDir("$buildDir/generate-resources/main/src/main/java/")
        }
    }
}

tasks {
    withType<JavaCompile> {
        dependsOn(openApiGenerate)
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
    }
    withType<org.openapitools.generator.gradle.plugin.tasks.GenerateTask> {
        generatorName.set("java")
        inputSpec.set("$rootDir/../icy-v1.0.yaml")
        apiPackage.set("com.mtmd.ui.infrastructure.client.gen")
        modelPackage.set("com.mtmd.ui.infrastructure.client.gen.types")
        configOptions.put("dateLibrary", "java8")
    }
    withType<com.google.cloud.tools.jib.gradle.JibTask> {
        // make sure vaadinBuildFrontend is called before (otherwise it will not work)
        dependsOn("vaadinBuildFrontend")
    }
}

vaadin {
    // must be falso in order to work in Docker for now
    productionMode = false
}

jib {
    to.image = "mtmd/vaadin-frontend"
}
