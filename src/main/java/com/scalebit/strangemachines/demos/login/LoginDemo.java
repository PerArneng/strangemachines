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
package com.scalebit.strangemachines.demos.login;


import com.scalebit.strangemachines.StateHandler;
import com.scalebit.strangemachines.StateMachine;
import com.scalebit.strangemachines.StateMachineFactory;
import com.scalebit.strangemachines.export.ExportFormat;
import com.scalebit.strangemachines.export.StateMachineExport;

public class LoginDemo implements DemoAsyncConsole.AsyncConsoleListener {

    private final StateMachine<LoginState> sm;
    private final DemoAsyncConsole console = new DemoAsyncConsole();

    public LoginDemo() {
        sm = StateMachineFactory.createDefault(LoginState.class);
        sm.addState(LoginState.ASK_FOR_USERNAME, askForUsername);
        sm.addState(LoginState.INVALID_USERNAME, invalidUsername);
        sm.addState(LoginState.ASK_FOR_PASSWORD, askForPassword);
        sm.addState(LoginState.END_STATE, endState);
        sm.addState(LoginState.IO_ERROR, ioError);

        sm.addTransition(LoginState.ASK_FOR_USERNAME, LoginState.INVALID_USERNAME);
        sm.addTransition(LoginState.INVALID_USERNAME, LoginState.ASK_FOR_USERNAME);
        sm.addTransition(LoginState.ASK_FOR_USERNAME, LoginState.ASK_FOR_PASSWORD);
        sm.addTransition(LoginState.ASK_FOR_PASSWORD, LoginState.END_STATE);
        sm.addTransition(LoginState.ASK_FOR_PASSWORD, LoginState.IO_ERROR);
        sm.addTransition(LoginState.ASK_FOR_USERNAME, LoginState.IO_ERROR);
        sm.addTransition(LoginState.IO_ERROR, LoginState.END_STATE);

    }

    public static void main(String[] args) {

        LoginDemo demo = new LoginDemo();
        demo.console.start();
        demo.console.addListener(demo);

        System.out.println("printing the state machine as graphviz format first");
        StateMachineExport.export(ExportFormat.GRAPHVIZ, demo.sm, System.out);

        demo.sm.transition(LoginState.ASK_FOR_USERNAME);

    }

    public void onReadLine(String line) {
        if (sm.isCurrentState(LoginState.ASK_FOR_USERNAME)) {
            if (line.length() < 2) {
                 sm.transition(LoginState.INVALID_USERNAME);
            } else {
                 sm.transition(LoginState.ASK_FOR_PASSWORD);
            }
        } else if (sm.isCurrentState(LoginState.ASK_FOR_PASSWORD)) {
             sm.transition(LoginState.END_STATE);
        }
    }

    @Override
    public void onError(String message) {
        sm.transition(LoginState.IO_ERROR);
    }

    private StateHandler<LoginState> ioError = new StateHandler<LoginState>() {

        @Override
        public void onEnter(StateMachine<LoginState> stateMachine, LoginState sourceStateKey, LoginState currentStateKey) {
            System.out.println("an export error occurred");
            sm.transition(LoginState.END_STATE);
        }

    };

    private StateHandler<LoginState> endState = new StateHandler<LoginState>() {

        @Override
        public void onEnter(StateMachine<LoginState> stateMachine, LoginState sourceStateKey, LoginState currentStateKey) {
            console.stop();
        }

    };

    private StateHandler<LoginState> askForUsername = new StateHandler<LoginState>() {

        @Override
        public void onEnter(StateMachine<LoginState> stateMachine, LoginState sourceStateKey, LoginState currentStateKey) {
            System.out.println("Login To Wonderland (u/p: alice/grow)");
            System.out.print("username: ");
            console.requestLine();
        }

    };

    private StateHandler<LoginState> invalidUsername = new StateHandler<LoginState>() {

        @Override
        public void onEnter(StateMachine<LoginState> stateMachine, LoginState sourceStateKey, LoginState currentStateKey) {
            System.out.println("username must be atleast 1 char ");
            sm.transition(LoginState.ASK_FOR_USERNAME);
        }

    };

    private StateHandler<LoginState> askForPassword = new StateHandler<LoginState>() {

        @Override
        public void onEnter(StateMachine<LoginState> stateMachine, LoginState sourceStateKey, LoginState currentStateKey) {
            System.out.print("password: ");
            console.requestPassword();
        }

    };

}
