Angular has to manage the signal creation lifecycle.

If the kotlin superclass creates a signal (using Angular's API), it doesn't recognize the signal creation and doesn't seem to track it. Likewise, in the template, calling a kotlin function which calls the signal doesn't seem to register the listener. The angular signal must be called directly.

That's why the auth service (which maintains a signal) is maintained in Angular and injected into the kotlin code…