buildscript {
    dependencies {
        // commenting this shows the original dependency-hell
        classpath("com.buschmais.jqassistant.cli:application:1.10.0") {
            exclude("org.slf4j", "slf4j-simple") // For excluding baz from bar but not if brought elsewhere
        }
    }
}

plugins {
    java
    id("io.quarkus")
    groovy
    id("org.openapi.generator") version "5.1.1"
    id("de.kontext_e.jqassistant.gradle") version "1.0.1"
}


repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project
val mapstructVersion = "1.4.2.Final"
val jqaVersion = "1.10.0"


jqassistant {
    toolVersion = jqaVersion
    plugins("com.buschmais.jqassistant.plugin:junit:${jqaVersion}")
    plugins("com.buschmais.jqassistant.plugin:jpa2:${jqaVersion}")
    plugins("de.kontext-e.jqassistant.plugin:jqassistant.plugin.git:1.8.0")
    scanDirs("../.git")
    scanDirs("java:classpath::../build/classes/java/main")
    options("-reset")
}

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-resteasy")
    implementation("io.quarkus:quarkus-smallrye-openapi")
    implementation("io.quarkus:quarkus-hibernate-orm")
    implementation("io.quarkus:quarkus-jdbc-postgresql")
    implementation("io.quarkus:quarkus-resteasy-jsonb")
    implementation("io.quarkus:quarkus-flyway")
    implementation("io.quarkus:quarkus-smallrye-health")
    implementation("org.javamoney:moneta:1.4.2")
    implementation( "org.mapstruct:mapstruct:$mapstructVersion" )
    implementation("io.swagger:swagger-annotations:1.6.2")

    annotationProcessor( "org.mapstruct:mapstruct-processor:$mapstructVersion" )
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("org.spockframework:spock-core:2.0-groovy-3.0")
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
    withType<JavaCompile> {
        options.release.set(16)
        dependsOn(openApiGenerate)
//        options.compilerArgs.add("--enable-preview")
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
        options.fork(mapOf(Pair("jvmArgs", listOf("--add-opens", "jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED"))))
        finalizedBy("copyOpenapiYaml") // make sure openapi.yaml is copied for Quarkus to pick up
    }

    withType<GroovyCompile> {
        options.release.set(16)
//        options.compilerArgs.add("--enable-preview")
    }

    withType<org.openapitools.generator.gradle.plugin.tasks.GenerateTask> {
        generatorName.set("jaxrs-spec")
        inputSpec.set("$rootDir/../icy-v1.0.yaml")
        configOptions.put("useTags", "true")
        configOptions.put("interfaceOnly", "true")
        configOptions.put("returnResponse", "true")
        apiPackage.set("com.mtmd.infrastructure.jaxrs.gen")
        modelPackage.set("com.mtmd.infrastructure.jaxrs.gen.types")
    }

    register<Copy>("copyOpenapiYaml") {
        from(layout.buildDirectory.dir("generate-resources/main/src/main/openapi/openapi.yaml"))
        into(layout.buildDirectory.dir("resources/main/META-INF/"))
    }
}
