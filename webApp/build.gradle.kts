import com.github.gradle.node.npm.task.NpxTask
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import kotlin.js.ExperimentalJsExport

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.serialization)
  alias(libs.plugins.node)
  // id("org.jetbrains.kotlinx.atomicfu") version "0.27.0"
}

// This configures the github node plugin,
// used to run the angular build tasks
node {
  version = libs.versions.nodejs
  download = true
}

version = "1.0.0-SNAPSHOT"
group = "io.rwc"

repositories {
  google()
  mavenCentral()
  mavenLocal()
}

kotlin {
  @OptIn(ExperimentalKotlinGradlePluginApi::class)
  compilerOptions {
    optIn.add("kotlin.RequiresOptIn")
  }

  @OptIn(ExperimentalJsExport::class)
  js(IR) {
    browser {
      useEsModules()
      commonWebpackConfig {
        sourceMaps = true
      }
      testTask {
        useKarma {
          useChromeHeadless()
        }
      }
    }
    binaries.executable()
    generateTypeScriptDefinitions()
    compilerOptions {
      target.set("es2015")
    }
  }

  sourceSets {
    jsMain.dependencies {
      implementation(libs.kotlinwrappers.kotlinJs)
      implementation(libs.kotlin.stdlib.js)
      implementation(libs.kotlinx.datetime)

      implementation(npm("@angular/core", libs.versions.angular.get()))
      implementation(npm("luxon", libs.versions.luxon.get()))
      implementation(npm("rxjs", libs.versions.rxjs.get()))

      // implementation(project.dependencies.platform(libs.firebase.bom))
      // implementation(libs.firebase.sdk.auth)

      implementation(libs.bignum)

      runtimeOnly("org.jetbrains.kotlin:kotlinx-atomicfu-runtime:2.1.20")
      // implementation(npm("@fortawesome/fontawesome-svg-core", "6.5.1"))
      // implementation(npm("@fortawesome/free-solid-svg-icons", "6.5.1"))
      // implementation(npm("@fortawesome/free-regular-svg-icons", "6.5.1"))
      // implementation(npm("@fortawesome/free-brands-svg-icons", "6.5.1"))

      implementation(projects.shared)
    }

    jsTest.dependencies {
      implementation(kotlin("test-js"))
    }
  }
}

tasks.npmInstall {
  nodeModulesOutputFilter {
    exclude("notExistingFile")
  }
}

val ngBuild = tasks.register<NpxTask>("buildWebapp") {
  command.set("ng")
  args.set(listOf("build", "--configuration=production"))
  dependsOn(tasks.npmInstall)
  inputs.dir(project.fileTree("src/jsMain").exclude("**/*.spec.ts"))
  inputs.dir("node_modules")
  inputs.files("angular.json", ".browserslistrc", "tsconfig.json", "tsconfig.app.json")
  // Do we need an outputs directory?
  // outputs.dir("${layout.buildDirectory}/install/webapp")

  dependsOn(tasks.build)
}

val ngTest = tasks.register<NpxTask>("testWebapp") {
  command.set("ng")
  args.set(listOf("test", "--watch=false"))
  dependsOn(tasks.npmInstall)
  inputs.dir("src/jsTest")
  inputs.dir("node_modules")
  inputs.files("angular.json", ".browserslistrc", "tsconfig.json", "tsconfig.spec.json", "karma.conf.js")
  outputs.upToDateWhen { true }
}

val ngServe = tasks.register<NpxTask>("serveWebapp") {
  command.set("ng")
  args.set(listOf("serve", "--configuration=development"))
  dependsOn(tasks.npmInstall)
  inputs.dir("src/jsMain")
  inputs.dir("node_modules")
  inputs.files("angular.json", ".browserslistrc", "tsconfig.json", "tsconfig.app.json")
  outputs.upToDateWhen { true }
}

