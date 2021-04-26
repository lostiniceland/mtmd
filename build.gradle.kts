plugins{
    id("base")
    id("org.asciidoctor.jvm.pdf") version "3.3.2"
}
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

