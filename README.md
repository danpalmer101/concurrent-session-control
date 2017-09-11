# concurrent-session-control

Java filter for controlling concurrent sessions to a web application

## Usage

1. Include the `io.danpalmer101.csc.ConcurrentSessionFilter` class in your web application and apply to the relevant request paths.
2. Set the required `io.danpalmer101.csc.provider.SessionRepositoryProvider` and `io.danpalmer101.csc.provider.SessionStateProvider` implementations in the class either using the dependency injection or by overriding the `init` method
    * This module contains a default implementation which uses assumes a session is represented by a standard `HttpSession` and an identifier for the user is stored attribute of the session

Optionally, you can extend the `io.danpalmer101.csc.ConcurrentSessionFilter` class to further customise it's behaviour.

Note that this library does not rely on any frameworks (such as Spring) such that it can be used in as many web application implementations as possible.
