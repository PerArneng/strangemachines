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


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class AsyncConsole implements Runnable {

    private final BlockingQueue<String> queue = new ArrayBlockingQueue<String>(100);
    private Thread internalThread;

    public void start() {
        internalThread = new Thread(this);
        internalThread.start();
    }

    @Override
    public void run() {
        try {
            String command;
            do{

               command = this.queue.take();
               if (command.equals("stop")) break;

               if ((new Random(System.currentTimeMillis())).nextInt(Integer.MAX_VALUE) % 3 == 0) {
                   for (AsyncConsoleListener l : listeners) {
                       l.onError("io exception occurred");
                   }
                   continue;
               }


               String line = null;
                if (command.equals("line")) {
                    line = System.console().readLine();
                } else if (command.equals("password")) {
                    line = new String(System.console().readPassword());
                }

               for (AsyncConsoleListener l : listeners) {
                    l.onReadLine(line);
               }

            } while (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void requestPassword() {
        putCommand("password");
    }

    public interface AsyncConsoleListener {
        void onReadLine(String line);
        void onError(String message);
    }

    private List<AsyncConsoleListener> listeners = new ArrayList<AsyncConsoleListener>();

    public void addListener(AsyncConsoleListener l) {
        listeners.add(l);
    }

    public void requestLine() {
        putCommand("line");
    }

    public void stop() {
        putCommand("stop");
    }


    private void putCommand(String cmd) {
        try {
            this.queue.put(cmd);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
