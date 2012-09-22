# Strange Machines

Strange Machines is a Java library for working with state machines. State machines can
be useful when working with asynchronous events, parsing or games.

The state machine implementation in this library is based on Java Enums that represents
each state. Each state can have an implementation that is triggered when the state
is entered. It is also possible to have transition guards that can prevent a transition
between two states.

## Current State

This project is under development so it is not stable enough for production use. There
are a lot of tests missing and it there are some demos that needs to be written to test
the API.

### Missing Features

* GraphViz export of a state machine to be able to visually inspect it
* Data object that is transferred between the states and transition guards
* Better documentation
* More unit tests
* More demos

## Building

The project is built with maven so the following is used to invoke a build with maven

    $ mvn install

## Demos

There are demos included showing you how the state machine can be used. You can use maven
to run the demos like this:

    $ mvn -Dexec.mainClass=com.scalebit.strangemachines.demos.login.LoginDemo exec:java

## Author(s)

* Per Arneng (Scalebit AB)

## License

See the file LICENSE in the root folder of the project (hint: MIT license)


