plugins {
    java
    `java-library`
    id("com.github.hierynomus.license-base") version "0.16.1"
    id ("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "io.github.pulsebeat02"
version = "v1.0.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

sourceSets {
    main {
        java {
            srcDir("src/main/java")
        }
        resources {
            srcDir("src/main/resources")
        }
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.17.1-R0.1-SNAPSHOT")
    testImplementation("com.google.code.gson:gson:2.8.9")
    setOf(
        "net.kyori:adventure-api:4.9.3",
        "net.kyori:adventure-platform-bukkit:4.0.0",
        "com.github.ben-manes.caffeine:caffeine:3.0.4").forEach {
        implementation(it)
        testImplementation(it)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
    shadowJar {
        relocate("net.kyori", "io.github.pulsebeat02.urbandictionaryplugin.lib.kyori")
    }
    build {
        finalizedBy(shadowJar)
    }
}

license {
    header = rootProject.file("header.txt")
    encoding = "UTF-8"
    mapping("java", "SLASHSTAR_STYLE")
    includes(setOf("**/*.java", "**/*.kts"))
}