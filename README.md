# streamwise
StreamWise helps understand and plan cash flow

This is a Kotlin Multiplatform project targeting Android, Web, Server.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
    - `commonMain` is for code that’s common for all targets.
    - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
      For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
      `iosMain` would be the right folder for such calls.

* `/server` is for the Ktor server application.

* `/shared` is for the code that will be shared between all targets in the project.
  The most important subfolder is `commonMain`. If preferred, you can add code to the platform-specific folders here
  too.

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),
[Kotlin/Wasm](https://kotl.in/wasm/)…

We would appreciate your feedback on Compose/Web and Kotlin/Wasm in the public Slack
channel [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web).
If you face any issues, please report them on [YouTrack](https://youtrack.jetbrains.com/newIssue?project=CMP).

You can open the web application by running the `:composeApp:wasmJsBrowserDevelopmentRun` Gradle task.

## Build instructions

### Environment

Copy `.envrc.template` into `.envrc` and fill in the values from your FIrebase configuration. (Make sure you have direnv installed, or set the environment variables some other way.)

Next, run `write-firebase-config.sh`, it will create config files with the values from the environment.

### Build

TBD

### Run the firebase emulator

Make sure you followed the environment steps above.

Run:

```sh
./emulate.sh
```

The emulator will maintain state in between runs (by reading/writing to a gitignore'd file).

### Serve the web app

TBD