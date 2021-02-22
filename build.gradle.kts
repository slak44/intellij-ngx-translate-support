plugins {
  id("org.jetbrains.intellij") version "0.6.5"
  kotlin("jvm") version "1.4.30"
}

group = "slak"
version = "1.1-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib"))
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
  version = "2020.3.2"
  type = "IU"
  setPlugins("JavaScript", "IntelliLang", "AngularJS")
}

tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
  changeNotes("""
      Add change notes here.<br>
      <em>most HTML tags may be used</em>""")
}

sourceSets["main"].java.srcDirs("src/main/gen")

