package com.scalebit.strangemachines.impl.basic;

import com.scalebit.strangemachines.State;
import com.scalebit.strangemachines.StateHandler;
import com.scalebit.strangemachines.Transition;
import com.scalebit.strangemachines.TransitionException;

import java.util.*;


class BasicState<E extends Enum<E>> implements State<E> {

    private final E stateKey;
    private StateHandler<E> stateHandler;
    private Map<State<E>, Transition<E>> transitions = new HashMap<State<E>, Transition<E>>();

    public BasicState(E stateKey) {
        this(stateKey, null);
    }

    public BasicState(E stateKey, StateHandler<E> stateHandler) {
        this.stateHandler = stateHandler;
        this.stateKey = stateKey;
    }

    public void setStateHandler(StateHandler<E> stateHandler) {
        this.stateHandler = stateHandler;
    }

    public void putTransition(State<E> targetState, Transition<E> transition) {
        transitions.put(targetState, transition);
    }

    public Transition<E> getTransition(State<E> targetState) {
        Transition<E> transition = transitions.get(targetState);
        if (transition == null) {
            throw new TransitionException(this.getStateKey(), targetState.getStateKey(), "the state " + this +
                                            " does not have a transition to the state " + targetState);
        }
        return transition;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicState that = (BasicState) o;

        if (stateKey != null ? !stateKey.equals(that.stateKey) : that.stateKey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return stateKey != null ? stateKey.hashCode() : 0;
    }

    @Override
    public String toString() {
        return this.stateKey.name();
    }

    @Override
    public E getStateKey() {
        return this.stateKey;
    }

    @Override
    public Set<Transition<E>> getTransitions() {
        return new HashSet<Transition<E>>(this.transitions.values());
    }

    @Override
    public StateHandler<E> getStateHandler() {
        return this.stateHandler;
    }

    @Override
    public boolean hasStateHandler() {
        return this.stateHandler != null;
    }
}
