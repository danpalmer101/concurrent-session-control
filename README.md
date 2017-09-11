# concurrent-session-control

Java filter for controlling concurrent sessions to a web application.

This has a similar function to [Spring Security's concurrency control](https://docs.spring.io/spring-security/site/docs/current/reference/html/session-mgmt.html), but does not rely on Spring or assume the use of HTTP sessions.

## Usage

1. Include the `io.danpalmer101.csc.ConcurrentSessionFilter` class in your web application and apply to the relevant request paths.
2. Set the required `io.danpalmer101.csc.provider.SessionRepositoryProvider` and `io.danpalmer101.csc.provider.SessionStateProvider` implementations in the class either using dependency injection or by overriding the `init` method of the filter
    * This module contains a default implementation which uses assumes a session is represented by a standard `HttpSession` and an identifier for the user is stored as an attribute of the session

Note that this library does not rely on any frameworks (such as Spring) so that it can be used in as many web application implementations as possible.
