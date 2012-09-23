# Strange Machines

![A Strange Machine](https://raw.github.com/PerArneng/strangemachines/master/docs/images/strangemachine.png)

**Table Of Contents**
* [About](https://github.com/PerArneng/strangemachines#about)
* [Interesting Features](https://github.com/PerArneng/strangemachines#interesting-features)
* [Examples](https://github.com/PerArneng/strangemachines#examples)
* [Building](https://github.com/PerArneng/strangemachines#building)
* [Demos](https://github.com/PerArneng/strangemachines#demos)
* [Author(s)](https://github.com/PerArneng/strangemachines#authors)
* [License](https://github.com/PerArneng/strangemachines#license)


## About

Strange Machines is a Java library for working with state machines. State machines can
be useful when working with asynchronous events, parsing or games. The purpose of the
state machine in this library is to enable a definition which states and transitions
are valid and then make sure that a program that uses the state machine follows these
rules.

The state machine implementation in this library is based on Java Enums that represents
each state. Each state can have an implementation that is triggered when the state
is entered. It is also possible to have transition guards that can prevent a transition
between two states.

**Note:** *This project is still under development so expect to see bugs and API changes*

## Interesting Features

### GraphViz Export
For debugging purpouse it is nice to see a visual representation of the state machine. With
Strange Machines it is possible to export the state machine to a [GrapViz](http://www.graphviz.org/)
compatible drawing. GraphViz uses a text representation of graphs to render images.

If you do not have GraphViz installed you can copy the GraphViz text and render the image
at this [place](http://sandbox.kidstrythisathome.com/erdos/index.html).

Example rendered state machine from the Login Demo:

![Hello, World! GraphViz image](https://raw.github.com/PerArneng/strangemachines/master/docs/images/login_demo.png)

## Missing Features

* Data object that is transferred between the states and transition guards
* Better documentation
* More unit tests
* More demos
* Threadsafety

## Examples

Here are some(one) example of how to use the state machine. For more examples look in the source code
for [tests](https://github.com/PerArneng/strangemachines/tree/master/src/test/java/com/scalebit/strangemachines)
and [demos](https://github.com/PerArneng/strangemachines/tree/master/src/main/java/com/scalebit/strangemachines/demos)
in this repository.

### Hello World
The following example makes "Hello, World!" look very massive. But the % of boilerplate
code will be less in larger programs. You would probably not implement your state handlers
as anonymous inner classes.

```java
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
```

The above program will also produce the following grapghviz diagram:

![Hello, World! GraphViz image](https://raw.github.com/PerArneng/strangemachines/master/docs/images/hello_world.png)

## Building

The project is built with maven so the following is used to invoke a build with maven

```bash
$ mvn install
```

## Demos

There are demos included showing you how the state machine can be used. You can use maven
to run the demos like this:

```bash
$ mvn -Dexec.mainClass=com.scalebit.strangemachines.demos.login.LoginDemo exec:java
```

## Author(s)

* Per Arneng (Scalebit AB)

## License

See the file LICENSE in the root folder of the project (hint: MIT license)


