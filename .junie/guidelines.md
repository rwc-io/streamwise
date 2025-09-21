# Project Guidelines

Project overview
- Streamwise is a Kotlin Multiplatform project focused on modeling and visualizing cash flows (e.g., Fixed, Weekly, Monthly) and exposing them to a web UI.
- Technologies: Kotlin Multiplatform (shared logic), Firebase (server), Kotlin/JS for web, TypeScript interop, Angular in TypeScript, Gradle build, Kotlinx Serialization and DateTime, ION‑Spin BigNum, optional Firebase emulation for local development.

Repository structure
- shared: Multiplatform library with core domain types and logic.
  - src/commonMain: Common Kotlin sources (e.g., io.rwc.streamwise.flows.*).
  - src/commonTest: Common unit tests (e.g., WeeklyTest.kt).
- webApp: Kotlin/JS web application packaged as an Angular application.
  - src/jsMain/kotlin: Kotlin sources compiled to JS (Angular interop classes like FlowListComponent, services).
  - src/jsMain/typescript: TypeScript/Angular components that consume the generated JS interop.
- server: Backend-related code/scripts (not required for basic flows or web UI tasks in this repo snapshot).
- Root: Gradle build files and helper scripts (e.g., emulate.sh, Firebase configs).

Build and run
- Build everything: ./gradlew build
- Run all checks: ./gradlew check
- Build web app: ./gradlew :webApp:buildWebApp
- Serve web app: ./gradlew :webApp:serveWebApp
- Optional: Local emulators (Firestore/Firebase) with project script emulate.sh if your task requires backend emulation.

Testing guidance for Junie
- When modifying shared (commonMain) logic, run: ./gradlew :shared:check
- When modifying webApp Kotlin/JS interop or TS usage impacting compilation, run: ./gradlew :webApp:buildWebApp
- Prefer running the narrowest set of tasks necessary to validate changes; fall back to ./gradlew build if unsure.

Data model
- The application uses Firestore to store application data.
- Security rules keep user data private.
- Cash flow bundles' flows reference the cash flow owner for access control.

Coding conventions
- Kotlin: Follow idiomatic Kotlin style; prefer immutable data structures and pure functions for domain logic.
- Serialization: Use kotlinx.serialization annotations already present; keep types stable across JS interop (avoid non-exportable types in @JsExport APIs).
- Numeric types: Monetary values use ION‑Spin BigDecimal; avoid Double for money.
- Tests: Add or update tests under shared/src/commonTest for domain changes.

Accessing Angular state:
- Kotlin cannot access Angular state directly. Kotlin code should not attempt to read the Angular signals.
- Signals may only be read in Angular/TypeScript code. The read value should be passed to Kotlin.
- Kotlin code may write Angular signals.
- Angular signals should be passed to the Kotlin component using the constructor. Avoid ngOnInit.
  - Sometimes this means the signal must be instantiated in the TypeScript constructor (not the main class body).

Angular component design:
- Components are implemented in TypeScript, inheriting from a Kotlin-generated base class.
- The TypeScript component must handle all state-related actions (eg. reading signals).
  - Signal reads in Kotlin don't get registered with the lifecycle hooks.
- To make writeable signals available to Kotlin, pass them in the constructor.
- To make readable signals available to Kotlin, read them in TypeScript and pass the value to a Kotlin function.
- For example, an Angular computed signal is implemented as a call to a Kotlin function, passing in dependent signal reads.

What is a cash flow?
- A cash flow represents some amount of money on some date.
- Types of cash flows include Fixed (one-time), Weekly, Monthly, etc.
- The Firebase data model organizes cash flows into *bundles*.
- A bundle has a subcollection for each type of cash flow.

What Junie should do when handling issues
- Make minimal changes required to satisfy the issue.
- Keep the User informed by updating status and the plan.
- If code changes touch shared logic, run related tests. If they touch the web app, ensure it builds for JS.
- Provide a brief summary of changes on submit.
