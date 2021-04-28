plugins{
    id("base")
    id("org.asciidoctor.jvm.pdf") version "3.3.2"
}

group = "com.mtmd"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}


tasks {
    withType<org.asciidoctor.gradle.jvm.pdf.AsciidoctorPdfTask>{
        sourceDir(layout.projectDirectory)
        sources{
            include("README.adoc")
        }
        setOutputDir(layout.projectDirectory)
    }
}

