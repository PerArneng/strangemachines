package com.scalebit.strangemachines.export;


import com.scalebit.strangemachines.StateMachine;

import java.io.OutputStream;

public interface Exporter<E extends Enum<E>> {

    void export(StateMachine<E> stateMachine, OutputStream outputStream);

}
