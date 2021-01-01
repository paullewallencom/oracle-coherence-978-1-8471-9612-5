package com.seovic.coherence.cachemanager;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.seovic.coherence.cachemanager.commands.Command;
import com.seovic.coherence.cachemanager.commands.CommandFactory;

import java.io.*;
import java.util.ArrayList;

public class CacheManager {

    private NamedCache cache = null;

    public static void main(String[]args) {

        CacheManager cacheManager = new CacheManager();
        cacheManager.processCommands();

        System.out.println("Bye!");
    }

    public void processCommands() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintStream out = System.out;

        while (true) {

            String cmdLine = acceptCommand(in);

            if (cmdLine != null && cmdLine.trim().length() > 0) {
                out.println();

                String cmd = parseCommandName(cmdLine);
                String[] args = parseArguments(cmdLine);


                if (cmd.equals("quit") || cmd.equals("bye")) {
                    CacheFactory.shutdown();
                    break;
                }
                else if (cmd.equals("cache")) {
                    if (args.length < 1) {
                        out.println("cache <cacheName>");
                    }
                    else
                    {
                        cache = CacheFactory.getCache(args[0]);
                    }
                }
                else {
                    Command command = CommandFactory.getCommand(cmd);
                    if (command != null) {
                        try {
                            command.execute(cache, args);
                        }
                        catch (Throwable e) {
                            // log the exception and continue
                            out.println(">> exception during cache operation:");
                            out.println(e);
                        }
                    }
                    else {
                        out.println(">> unknown command: " + cmd);
                        out.println(">> try \"help\"");
                    }

                }
            }
        }
    }

    private String acceptCommand(BufferedReader in) {
        printCommandPrompt();

        String cmdLine = null;
        try { cmdLine = in.readLine(); } catch (IOException e) {}
        return cmdLine;
    }

    private void printCommandPrompt() {
        PrintStream out = System.out;

        String prompt = (cache == null ? "> " : cache.getCacheName() + "> ");
        out.println();
        out.print(prompt);
        out.flush();
    }

    private String parseCommandName(String cmdLine) {
        int idx = cmdLine.indexOf(" ");
        if (idx > 0) {
            return cmdLine.substring(0, idx);
        }
        else {
            return cmdLine;
        }
    }

    private String[] parseArguments(String cmdLine) {
        ArrayList<String> argList = new ArrayList<String>();

        int idx = cmdLine.indexOf(" ");
        if (idx > 0) {
            String arguments = cmdLine.substring(idx).trim();
            char separator = ' ';
            StringBuffer arg = new StringBuffer();
            for (int i = 0; i < arguments.length(); i++) {
                char c = arguments.charAt(i);

                if (c == separator && arg.length() > 0) {
                    argList.add(arg.toString());
                    arg = new StringBuffer();
                    separator = ' ';
                }
                else if (c == '"') {
                    separator = '"';
                }
                else if (c == ' ' && separator == ' ') {
                    // skip
                }
                else {
                    arg.append(c);
                }

            }

            if (arg.length() > 0) {
                argList.add(arg.toString());
            }
        }

        return argList.toArray(new String[argList.size()]);
    }
}