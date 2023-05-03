plugins {
    scala
//    `maven-publish`
//    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
}

//gradlePlugin {
//    plugins {
//        create("simplePlugin") {
//            id = "pers.zhc.rust-build"
//            implementationClass = "pers.zhc.gradle.plugins.rust.TestPlugin"
//        }
//    }
//}

//publishing {
//    publications {
//        create<MavenPublication>("maven") {
//            groupId = "pers.zhc"
//            artifactId = "rustbuild"
//            version = "0.1"
//        }
//    }
//}

dependencies {
    implementation(gradleApi())
    implementation("org.scala-lang:scala-library:2.13.10")
    implementation("commons-io:commons-io:2.11.0")
    implementation("com.github.bczhc:java-lib:18a858c167")
}
