/* Copyright (c) 2012 Per Arneng

   Permission is hereby granted, free of charge, to any person obtaining
   a copy of this software and associated documentation files (the
   "Software"), to deal in the Software without restriction, including
   without limitation the rights to use, copy, modify, merge, publish,
   distribute, sublicense, and/or sell copies of the Software, and to
   permit persons to whom the Software is furnished to do so, subject to
   the following conditions:

   The above copyright notice and this permission notice shall be
   included in all copies or substantial portions of the Software.

   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
   EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
   IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
   CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
   TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
   SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. */
package com.scalebit.strangemachines;

import com.scalebit.strangemachines.export.ExportFormat;
import com.scalebit.strangemachines.export.StateMachineExport;
import org.junit.Test;

public class StateMachineTest {

   private static enum Test1Enum {
        STATE1, STATE2
   }

   @Test
   public void test1NormalTwoStates() {
       final StateMachine<Test1Enum> sm = StateMachineFactory.createDefault(Test1Enum.class);
       sm.addState(Test1Enum.STATE1, new StateHandler<Test1Enum>() {
           @Override
           public void onEnter(StateMachine<Test1Enum> stateMachine, Test1Enum sourceStateKey, Test1Enum currentStateKey) {

           }
       });
       sm.addState(Test1Enum.STATE2, new StateHandler<Test1Enum>() {
           @Override
           public void onEnter(StateMachine<Test1Enum> stateMachine, Test1Enum sourceStateKey, Test1Enum currentStateKey) {
               sm.transition(Test1Enum.STATE1);
           }
       });

       sm.addTransition(Test1Enum.STATE1, Test1Enum.STATE2);
       sm.addTransition(Test1Enum.STATE2, Test1Enum.STATE1);

       sm.transition(Test1Enum.STATE1);
       sm.transition(Test1Enum.STATE2);

   }

    enum HelloWorldState { HELLO, WORLD};

    @Test
    public void test2HelloWorld() {

        // create the statemachine and incidate that you want to
        // use HelloWorldState enum as state keys
        StateMachine<HelloWorldState> sm =
                  StateMachineFactory.createDefault(HelloWorldState.class);

        // add the hello state with a handler
        sm.addState(HelloWorldState.HELLO, new StateHandler<HelloWorldState>() {
            public void onEnter(StateMachine<HelloWorldState> stateMachine,
                                    HelloWorldState sourceStateKey, HelloWorldState currentStateKey) {
                System.out.print("Hello, ");

                // make the state machine transition to the world state
                stateMachine.transition(HelloWorldState.WORLD);
            }
        });

        // add the second world state
        sm.addState(HelloWorldState.WORLD, new StateHandler<HelloWorldState>() {
            public void onEnter(StateMachine<HelloWorldState> stateMachine,
                                    HelloWorldState sourceStateKey, HelloWorldState currentStateKey) {
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

    }
}
