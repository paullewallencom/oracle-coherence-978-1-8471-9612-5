package com.seovic.coherence.cachemanager.commands;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private static final CommandFactory instance = new CommandFactory();

    private Map<String, Command> commands;

    private CommandFactory() {
        commands = new HashMap<String, Command>();

        commands.put("help",   new HelpCommand("help", "Shows information about all available commands", commands));
        commands.put("cache",  new NopCommand("cache <cacheName>", "Switches to the specified cache"));
        commands.put("quit",   new NopCommand("quit", "Terminates the application"));

        commands.put("clear",  new ClearCommand("clear", "Clears the cache"));
        commands.put("get",    new GetCommand("get <key>", "Retrieves item for the specified key"));
        commands.put("put",    new PutCommand("put <key> <value>", "Puts specified value into the cache"));
        commands.put("remove", new RemoveCommand("remove <key>", "Removes specified item from the cache"));
        commands.put("keys",   new KeysCommand("keys", "Returns a list of all the keys that exist in the cache"));
        commands.put("list",   new ListCommand("list", "Returns a list of all the items that exist in the cache"));
        commands.put("size",   new SizeCommand("size", "Returns the number of items in the cache"));
        commands.put("load",   new LoadCommand("load <fileName>", "Loads items from a CSV file into the cache"));
    }

    public static Command getCommand(String cmd) {
        return instance.commands.get(cmd);
    }
}
