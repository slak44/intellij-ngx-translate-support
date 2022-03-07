plugins {
  id("org.jetbrains.intellij") version "1.4.0"
  kotlin("jvm") version "1.6.10"
}

group = "slak"
version = "1.1-SNAPSHOT5"

repositories {
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib"))
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
  version.set("2021.3")
  type.set("IU")
  plugins.set(listOf("JavaScript", "IntelliLang", "AngularJS"))
}

tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
  changeNotes.set("""
      Add change notes here.<br>
      <em>most HTML tags may be used</em>""")
}

sourceSets["main"].java.srcDirs("src/main/gen")

