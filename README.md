# Strange Machines

Strange Machines is a Java library for working with state machines. State machines can
be useful when working with asynchronous events, parsing or games.

The state machine implementation in this library is based on Java Enums that represents
each state. Each state can have an implementation that is triggered when the state
is entered. It is also possible to have transition guards that can prevent a transition
between two states.

## Examples

### Hello World
The following example makes "Hello, World!" look very massive. But the % of boilerplate
code will be less in larger programs. You would probably not implement your state handlers
as anonymous inner classes.

        // create the statemachine and incidate that you want to
        // use HelloWorldState enum as state keys
        StateMachine<HelloWorldState> sm =
                  StateMachineFactory.createDefault(HelloWorldState.class);

        // add the hello state with a handler
        sm.addState(HelloWorldState.HELLO, new StateHandler<HelloWorldState>() {
            public void onEnter(StateMachine<HelloWorldState> stateMachine,
                                    HelloWorldState sourceState, HelloWorldState currentState) {
                System.out.print("Hello, ");

                // make the state machine transition to the world state
                stateMachine.transition(HelloWorldState.WORLD);
            }
        });

        // add the second world state
        sm.addState(HelloWorldState.WORLD, new StateHandler<HelloWorldState>() {
            public void onEnter(StateMachine<HelloWorldState> stateMachine,
                                    HelloWorldState sourceState, HelloWorldState currentState) {
                System.out.println("World!");
            }
        });

        // add a transition between HELLO and WORLD
        sm.addTransition(HelloWorldState.HELLO, HelloWorldState.WORLD);

        // start by transitioning to HELLO, HELLO is set as the start
        // state since it was added first. It is possible to set other
        // start states afterwards. Any illegal transitions will cause
        // a TransitionException to be thrown
        sm.transition(HelloWorldState.HELLO);

        // this will print Hello, World! in to the shell/console/dosprompt

        // and finaly print out the state machine as graphviz format
        StateMachineExport.export(ExportFormat.GRAPHVIZ, sm, System.out);

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


