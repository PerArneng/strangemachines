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
        this.startState = this.states.get(state);
    }

    @Override
    public void setCurrentState(E state) {
        this.currentState = this.states.get(state);
    }

    @Override
    public boolean isCurrentState(E state) {
        return this.currentState == this.states.get(state);
    }

    @Override
    public void addState(E state) {
        this.addState(state, null);
    }

    @Override
    public void addState(E state, StateHandler<E> stateHandler) {

        BasicState<E> basicState = new BasicState<E>(state, stateHandler);

        this.states.put(state, basicState);

        if (this.startState == null) {
            this.setStartState(state);
        }
    }

    @Override
    public void removeStateHandler(E state) {
        this.setStateHandler(state, null);
    }

    @Override
    public void setStateHandler(E state, StateHandler<E> stateHandler) {
        BasicState<E> basicState = (BasicState<E>) this.states.get(state);
        basicState.setStateHandler(null);
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
            this.ensureTransitionExists(this.currentState.getState(), targetState.getState());
            this.ensureGuardsAllowTransition(this.currentState.getState(), targetState.getState());

            State<E> sourceState = this.currentState;
            this.currentState = targetState;
            if (targetState.hasStateHandler()) {
                targetState.getStateHandler().onEnter(this, sourceState.getState());
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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void ensureTransitionExists(E sourceState, E targetState) {
        long transitionId = generateTransitionID(sourceState, targetState);
        if (!this.transitions.containsKey(transitionId)) {
            throw new TransitionException(this.currentState.getState(), targetState, "the state " + currentState +
                    " does not have a transition to the state " + targetState);
        }
    }

    private void ensureGuardsAllowTransition(E sourceState, E targetState) {
        List<TransitionGuard<E>> transitionGuards =
                                   this.transitions.get(generateTransitionID(sourceState, targetState));
        for (TransitionGuard guard : transitionGuards) {
            if (!guard.transitionValid(currentState.getState(), targetState)) {
                throw new TransitionException(currentState.getState(),
                                                targetState, "The guard " + guard + " prevented it");
            }
        }
    }


}
