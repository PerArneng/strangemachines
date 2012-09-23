package com.scalebit.strangemachines;


import java.util.List;
import java.util.Set;

public interface Transition<E extends Enum<E>> {

    State<E> getSource();

    State<E> getTarget();

    List<TransitionGuard<E>> getGuards();
}
