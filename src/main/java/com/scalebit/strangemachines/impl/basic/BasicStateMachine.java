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
package com.scalebit.strangemachines.impl.basic;

import com.scalebit.strangemachines.*;

import java.util.*;

public class BasicStateMachine<E extends Enum<E>> implements StateMachine<E> {

    private State<E> startState;
    private State<E> currentState;
    private Map<E, State<E>> states = new HashMap<E, State<E>>();

    @Override
    public void reset() {
        this.currentState = startState;
    }

    @Override
    public void setStartState(E stateKey) {
        this.startState = this.states.get(stateKey);
    }

    @Override
    public void setCurrentState(E stateKey) {
        this.currentState = this.states.get(stateKey);
    }

    @Override
    public boolean isCurrentState(E stateKey) {
        return this.currentState == this.states.get(stateKey);
    }

    @Override
    public void addState(E stateKey) {
        this.addState(stateKey, null);
    }

    @Override
    public void addState(E stateKey, StateHandler<E> stateHandler) {

        BasicState<E> basicState = new BasicState<E>(stateKey, stateHandler);

        this.states.put(stateKey, basicState);

        if (this.startState == null) {
            this.setStartState(stateKey);
        }
    }

    @Override
    public void removeStateHandler(E stateKey) {
        this.setStateHandler(stateKey, null);
    }

    @Override
    public void setStateHandler(E stateKey, StateHandler<E> stateHandler) {
        BasicState<E> basicState = (BasicState<E>) this.states.get(stateKey);
        basicState.setStateHandler(null);
    }

    @Override
    public void addTransition(E sourceStateKey, E targetStateKey, TransitionGuard<E>... guards) {

        if (guards == null) {
            throw new IllegalArgumentException("guards can not be null");
        }

        BasicState<E> sourceState = (BasicState<E>) this.getStateFromKey(sourceStateKey);
        State<E> targetState = this.getStateFromKey(targetStateKey);

        Transition<E> transition = new BasicTransition<E>(sourceState, targetState, Arrays.asList(guards));
        sourceState.putTransition(targetState, transition);
    }

    @Override
    public void transition(E targetStateKey) {

        State<E> targetState = this.getStateFromKey(targetStateKey);

        if (currentState == null) {
            // first time we start the machine

            if (targetState != this.startState) {
                // first time but we are not transitioning to the start state
                throw new IllegalStateException("the first transition must be to the start state '" +
                                                 this.startState + "' not '" + targetStateKey + "'");
            } else {
               // set the current state to the start state and then
               // execute the state handler if it has one
               this.currentState = this.startState;
               if (this.startState.hasStateHandler()) {
                   this.startState.getStateHandler().onEnter(this, null);
               }
            }

        } else {
            Transition<E> transition = ((BasicState) this.currentState).getTransition(targetState);
            this.ensureGuardsAllowTransition(transition);

            State<E> sourceState = this.currentState;
            this.currentState = targetState;
            if (targetState.hasStateHandler()) {
                targetState.getStateHandler().onEnter(this, sourceState.getStateKey());
            }
        }
    }

    private State<E> getStateFromKey(E stateKey) {
        if (stateKey == null) {
            throw new IllegalArgumentException("stateKey can not be null");
        }

        State<E> state = this.states.get(stateKey);

        if (state == null) {
            throw new IllegalStateException("The state is not a part of this state machine: " + stateKey);
        }

        return state;
    }

    @Override
    public Set<State<E>> inspect() {
        return new HashSet<State<E>>(this.states.values());
    }

    private void ensureGuardsAllowTransition(Transition<E> transition) {
        List<TransitionGuard<E>> transitionGuards = transition.getGuards();
        for (TransitionGuard guard : transitionGuards) {
            if (!guard.transitionValid(transition.getSource().getStateKey(), transition.getTarget().getStateKey())) {
                throw new TransitionException(currentState.getStateKey(), transition.getTarget().getStateKey(),
                                                "The guard " + guard + " prevented it");
            }
        }
    }


}
