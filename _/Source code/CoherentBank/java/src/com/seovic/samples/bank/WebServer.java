/*
Copyright 2009 Aleksandar Seovic

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.seovic.samples.bank;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;

import org.eclipse.jetty.server.nio.SelectChannelConnector;

import org.eclipse.jetty.webapp.WebAppContext;


/**
 * @author Ivan Cikic  2010.01.26
 */
public class WebServer
    {

    public static String getHost()
        {
        return System.getProperty("jetty.host", "127.0.0.1");
        }

    public static int getPort()
        {
        return Integer.parseInt(System.getProperty("jetty.port", "8080"));
        }

    public static String getWarLocation()
        {
        return System.getProperty("war.dir", "war");
        }

    // ---- inner class: Start ----------------------------------------------

    public static class Start
        {

        public static void main(String[] args)
                throws Exception
            {
            server = new Server();

            Connector connector = new SelectChannelConnector();
            connector.setPort(getPort());
            connector.setHost(getHost());
            server.addConnector(connector);

            WebAppContext context = new WebAppContext();
            context.setServer(server);
            context.setContextPath("/");
            context.setWar(getWarLocation());
            server.setHandler(context);

            Thread monitor = new MonitorThread();
            monitor.start();

            server.setStopAtShutdown(true);
            server.start();
            server.join();
            }

        private static class MonitorThread
                extends Thread
            {

            private final ServerSocket socket;

            public MonitorThread()
                {
                setDaemon(true);
                setName("StopMonitor");
                try
                    {
                    final InetAddress addr = InetAddress.getByName(getHost());
                    socket = new ServerSocket(8079, 1, addr);
                    }
                catch (Exception e)
                    {
                    throw new RuntimeException(e);
                    }
                }

            @Override
            public void run()
                {
                System.out.println("*** running jetty 'stop' thread");
                Socket accept;
                try
                    {
                    accept = socket.accept();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(accept.getInputStream()));
                    reader.readLine();
                    System.out.println("*** stopping jetty embedded server");
                    server.stop();
                    accept.close();
                    socket.close();
                    }
                catch (Exception e)
                    {
                    throw new RuntimeException(e);
                    }
                }
            }

        private static Server server;
        }

    // ---- inner class: Stop -----------------------------------------------

    public static class Stop
        {

        public static void main(String[] args)
                throws Exception
            {
            final Socket s = new Socket(InetAddress.getByName(getHost()), 8079);
            final OutputStream out = s.getOutputStream();
            System.out.println("*** sending jetty stop request");
            out.write(("\r\n").getBytes());
            out.flush();
            s.close();
            }
        }
    }
