package com.scalebit.strangemachines.export;

import com.scalebit.strangemachines.StateMachine;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.OutputStream;

public class StateMachineExport {


    public static <E extends Enum<E>> void export(ExportFormat format,
                                                    StateMachine<E> stateMachine, OutputStream outputStream) {

        Exporter<E> exporter;

        switch (format) {
            case GRAPHVIZ:
                exporter = new GraphVizExporter<E>();
                break;
            default:
                throw new IllegalArgumentException("" + format + " is not a supported export format");
        }

        exporter.export(stateMachine, outputStream);
    }

}
