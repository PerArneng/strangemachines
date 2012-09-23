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


import java.util.Set;

public interface StateMachine<E extends Enum<E>> {

    void reset();

    void setStartState(E stateKey);

    void setCurrentState(E stateKey);

    boolean isCurrentState(E stateKey);

    void addState(E stateKey);

    void addState(E stateKey, StateHandler<E> stateHandler);

    void removeStateHandler(E stateKey);

    void setStateHandler(E stateKey, StateHandler<E> stateHandler);

    void addTransition(E sourceStateKey, E targetStateKey);

    void addTransitionWithGuards(E sourceStateKey, E targetStateKey, TransitionGuard<E>... guards);

    void transition(E targetStateKey);

    Set<State<E>> getAllStates();

    State<E> getStateFromKey(E stateKey);

}
