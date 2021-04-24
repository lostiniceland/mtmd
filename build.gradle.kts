plugins {
    java
    id("io.quarkus")
    groovy
//    id("org.openapi.generator") version "5.1.0"
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
    implementation("org.javamoney:moneta:1.4.2")
    implementation( "org.mapstruct:mapstruct:$mapstructVersion" )
    annotationProcessor( "org.mapstruct:mapstruct-processor:$mapstructVersion" )
//    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("org.spockframework:spock-core:2.0-M4-groovy-3.0") // Quarkus ships Groovy 3, so we have to jump
}

group = "com.example"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}


tasks {
    val ENABLE_PREVIEW = "--enable-preview"
    withType<JavaCompile> {
//        dependsOn(openApiGenerate)
//        options.compilerArgs.add(ENABLE_PREVIEW)
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
    }

    withType<GroovyCompile> {

    }
}

// Currently incompatible with Gradle 7 (and Gradle 6 doesnt work with Java 16)
//openApiGenerate{
//    validateSpec.set(false)
//    generatorName.set("jaxrs-spec")
//    inputSpec.set("$rootDir/icy-v1.0.yaml")
//}



