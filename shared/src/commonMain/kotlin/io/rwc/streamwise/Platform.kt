package io.rwc.streamwise

interface Platform {
  val name: String
}

expect fun getPlatform(): Platform