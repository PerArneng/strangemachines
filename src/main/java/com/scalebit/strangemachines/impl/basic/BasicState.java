package com.scalebit.strangemachines.impl.basic;

import com.scalebit.strangemachines.State;
import com.scalebit.strangemachines.StateHandler;
import com.scalebit.strangemachines.Transition;

import java.util.HashSet;
import java.util.Set;


public class BasicState<E extends Enum<E>> implements State<E> {

    private final E state;
    private StateHandler<E> stateHandler;
    private Set<Transition<E>> transitions = new HashSet<Transition<E>>();

    public BasicState(E state) {
        this(state, null);
    }

    public BasicState(E state, StateHandler<E> stateHandler) {
        this.stateHandler = stateHandler;
        this.state = state;
    }

    public void setStateHandler(StateHandler<E> stateHandler) {
        this.stateHandler = stateHandler;
    }

    @Override
    public E getState() {
        return this.state;
    }

    @Override
    public Set<E> getTransitions() {
        return null;
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
