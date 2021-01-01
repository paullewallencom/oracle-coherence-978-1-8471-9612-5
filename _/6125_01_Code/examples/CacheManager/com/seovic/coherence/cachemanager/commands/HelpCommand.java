package com.seovic.coherence.cachemanager.commands;

import com.tangosol.net.NamedCache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HelpCommand extends AbstractCommand {

    private Map<String, Command> commands;

    public HelpCommand(String usage, String description, Map<String, Command> commands) {
        super(usage, description);
        this.commands = commands;
    }

    public void execute(NamedCache cache, String[] args) throws Exception {
        List<String> keys = new ArrayList<String>(commands.keySet());
        Collections.sort(keys);

        for (int i = 0; i < keys.size(); i++) {
            Command command = commands.get(keys.get(i));
            System.out.println(String.format("%-30s %s", command.getUsage(), command.getDescription()));
        }

    }
}
