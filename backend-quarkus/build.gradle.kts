plugins {
    java
    id("io.quarkus")
    groovy
    id("org.openapi.generator") version "5.1.0"
}

repositories {
    mavenLocal()
    mavenCentral()
}


val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project
val mapstructVersion = "1.4.2.Final"

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
    testImplementation("org.spockframework:spock-core:2.0-M4-groovy-3.0") // Quarkus ships Groovy 3, so we have to jump
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
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
        dependsOn(openApiGenerate)
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
        finalizedBy("copyOpenapiYaml") // make sure openapi.yaml is copied for Quarkus to pick up
    }

    withType<GroovyCompile> {

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




