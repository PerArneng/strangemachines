package com.scalebit.strangemachines;


import java.util.Set;

public interface State<E extends Enum<E>> {

    E getState();

    Set<E> getTransitions();

    StateHandler<E> getStateHandler();

    boolean hasStateHandler();

}
