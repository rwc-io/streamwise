package io.rwc.streamwise

class WasmPlatform : Platform {
  override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()