package com.scalebit.strangemachines.export;


import com.scalebit.strangemachines.State;
import com.scalebit.strangemachines.StateMachine;
import com.scalebit.strangemachines.Transition;
import com.scalebit.strangemachines.export.Exporter;
import com.sun.xml.internal.stream.buffer.stax.StreamWriterBufferCreator;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Set;

class GraphVizExporter<E extends Enum<E>> implements Exporter<E> {

    private final static String DEFAULT_NODE_STYLE = "style=\"rounded, filled\", shape=box";

    @Override
    public void export(StateMachine<E> stateMachine, OutputStream outputStream) {

        Set<State<E>> states = stateMachine.getAllStates();
        PrintWriter pw = new PrintWriter(outputStream);

        pw.println("digraph G {");
        pw.println("    node [fontcolor=navajowhite4, fillcolor=lemonchiffon, color=navajowhite4, " +
                     DEFAULT_NODE_STYLE + "];");
        pw.println("    edge [color=navajowhite4];");
        for (State<E> state : states) {
            String label = state.getStateKey().name();
            String style = "";
            if (state.getTransitions().isEmpty()) {
                style = style + "label=<<i>" + label + "</i>>";
            }
            pw.printf("    %s [%s]\n", label, style);
        }

        for (State<E> state : states) {
            for (Transition<E> transition : state.getTransitions()) {
                pw.printf("    %s -> %s;\n", state.getStateKey(), transition.getTarget().getStateKey());
            }
        }

        pw.println("}");
        pw.flush();
    }

}
