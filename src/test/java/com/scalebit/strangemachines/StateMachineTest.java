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

import org.junit.Test;

public class StateMachineTest {

   private static enum Test1Enum {
        STATE1, STATE2
   }

   @Test
   public void test() {
       final StateMachine<Test1Enum> sm = StateMachineFactory.createDefault(Test1Enum.class);
       sm.addState(Test1Enum.STATE1, new StateHandler<Test1Enum>() {
           @Override
           public void onEnter(StateMachine<Test1Enum> stateMachine, Test1Enum sourceState, Test1Enum targetState) {

           }
       });
       sm.addState(Test1Enum.STATE2, new StateHandler<Test1Enum>() {
           @Override
           public void onEnter(StateMachine<Test1Enum> stateMachine, Test1Enum sourceState, Test1Enum targetState) {
               sm.transition(Test1Enum.STATE1);
           }
       });

       sm.addTransition(Test1Enum.STATE1, Test1Enum.STATE2);
       sm.addTransition(Test1Enum.STATE2, Test1Enum.STATE1);

       sm.transition(Test1Enum.STATE1);
       sm.transition(Test1Enum.STATE2);

   }
}
