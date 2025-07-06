import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
  alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
  jvm()

  js(IR) {
    browser {
      useEsModules()
      commonWebpackConfig {
        sourceMaps = false
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
    commonMain.dependencies {
      // put your Multiplatform dependencies here
      implementation(libs.bignum)
      implementation(libs.kotlinx.datetime)
    }
    commonTest.dependencies {
      implementation(libs.kotlin.test)
    }
  }
}

