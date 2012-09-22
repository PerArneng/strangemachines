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
package com.scalebit.strangemachines.impl.simple;

import com.scalebit.strangemachines.StateHandler;
import com.scalebit.strangemachines.StateMachine;
import com.scalebit.strangemachines.TransitionException;
import com.scalebit.strangemachines.TransitionGuard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleStateMachine<E extends Enum<E>> implements StateMachine<E> {

    private E startState;
    private E currentState;
    private Map<E, StateHandler<E>> states = new HashMap<E, StateHandler<E>>();
    private Map<Long, List<TransitionGuard<E>>> transitions = new HashMap<Long, List<TransitionGuard<E>>>();

    @Override
    public void reset() {
        this.currentState = startState;
    }

    private long generateTransitionID(E source, E target) {
        return (((long)source.ordinal()) << 32)  | target.ordinal();
    }

    @Override
    public void setStartState(E state) {
        this.startState = state;
    }

    @Override
    public void setCurrentState(E state) {
        this.currentState = state;
    }

    @Override
    public boolean isCurrentState(E state) {
        return this.currentState == state;
    }

    @Override
    public void addState(E state) {
        this.addState(state, EMPTY_HANDLER);
    }

    @Override
    public void addState(E state, StateHandler<E> stateHandler) {

        this.states.put(state, stateHandler);

        if (this.startState == null) {
            this.setStartState(state);
        }
    }

    @Override
    public void removeStateHandler(E state) {
        this.states.put(state, EMPTY_HANDLER);
    }

    @Override
    public void setStateHandler(E state, StateHandler<E> stateHandler) {
        this.addState(state, stateHandler);
    }

    @Override
    public void addTransition(E sourceState, E targetState, TransitionGuard<E>... guards) {

        if (sourceState == null) {
            throw new IllegalArgumentException("source state can not be null");
        }

        if (targetState == null) {
            throw new IllegalArgumentException("target state can not be null");
        }

        if (guards == null) {
            throw new IllegalArgumentException("guards can not be null");
        }

        if (!states.containsKey(sourceState)) {
            throw new IllegalStateException("source state does not exist: " + sourceState.name());
        }

        if (!states.containsKey(targetState)) {
            throw new IllegalStateException("target state does not exist: " + targetState.name());
        }

        this.transitions.put(generateTransitionID(sourceState, targetState), Arrays.asList(guards));
    }

    @Override
    public void transition(E targetState) {

        if (targetState == null) {
            throw new IllegalArgumentException("targetState can not be null");
        }

        if (!states.containsKey(targetState)) {
            throw new IllegalStateException("The state is not a part of this state machine: " + targetState);
        }

        if (currentState == null) {

            if (targetState != this.startState) {
                throw new IllegalStateException("the first transition must be to the start state '" +
                                                 this.startState + "' not '" + targetState + "'");
            } else {
               this.currentState = this.startState;
               this.states.get(this.startState).onEnter(this, null);
            }

        } else {
            this.ensureTransitionExists(this.currentState, targetState);
            this.ensureGuardsAllowTransition(this.currentState, targetState);

            E sourceState = this.currentState;
            this.currentState = targetState;
            this.states.get(targetState).onEnter(this, sourceState);
        }
    }

    private void ensureTransitionExists(E sourceState, E targetState) {
        long transitionId = generateTransitionID(sourceState, targetState);
        if (!this.transitions.containsKey(transitionId)) {
            throw new TransitionException(this.currentState, targetState, "the state " + currentState +
                    " does not have a transition to the state " + targetState);
        }
    }

    private void ensureGuardsAllowTransition(E sourceState, E targetState) {
        List<TransitionGuard<E>> transitionGuards = this.transitions.get(generateTransitionID(sourceState, targetState));
        for (TransitionGuard guard : transitionGuards) {
            if (!guard.transitionValid(currentState, targetState)) {
                throw new TransitionException(currentState, targetState, "The guard " + guard + " prevented it");
            }
        }
    }

    private final StateHandler<E> EMPTY_HANDLER = new StateHandler<E>() {
        @Override
        public void onEnter(StateMachine<E> stateMachine, E sourceState) { }
    };
}
