package com.scalebit.strangemachines.impl.basic;

import com.scalebit.strangemachines.State;
import com.scalebit.strangemachines.Transition;
import com.scalebit.strangemachines.TransitionGuard;

import java.util.List;

class BasicTransition<E extends Enum<E>> implements Transition<E> {

    private final State<E> sourceState;
    private final State<E> targetState;
    private final List<TransitionGuard<E>> transitionGuards;

    public BasicTransition(State<E> sourceState, State<E> targetState, List<TransitionGuard<E>> transitionGuards) {
        this.sourceState = sourceState;
        this.targetState = targetState;
        this.transitionGuards = transitionGuards;
    }

    @Override
    public State<E> getSource() {
        return this.sourceState;
    }

    @Override
    public State<E> getTarget() {
        return this.targetState;
    }

    @Override
    public List<TransitionGuard<E>> getGuards() {
        return transitionGuards;
    }
}
