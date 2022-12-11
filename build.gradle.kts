plugins {
  id("org.jetbrains.intellij") version "1.10.1"
  kotlin("jvm") version "1.7.20"
}

group = "slak"
version = "1.1.1"

repositories {
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib"))
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
  version.set("2022.3")
  type.set("IU")
  plugins.set(listOf("JavaScript", "org.intellij.intelliLang", "AngularJS"))
}

tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
  changeNotes.set("""
      Add change notes here.<br>
      <em>most HTML tags may be used</em>""")
}

sourceSets["main"].java.srcDirs("src/main/gen")

tasks {

  java {
    toolchain {
      languageVersion.set(JavaLanguageVersion.of(17))
    }
  }
  compileJava {
    sourceCompatibility = JavaVersion.VERSION_17.toString()
    targetCompatibility = JavaVersion.VERSION_17.toString()
  }

  compileKotlin {
    kotlinOptions {
      jvmTarget = "17"
    }
  }
}
