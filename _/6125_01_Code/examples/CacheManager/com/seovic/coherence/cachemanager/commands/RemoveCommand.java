package com.seovic.coherence.cachemanager.commands;

import com.tangosol.net.NamedCache;

public class RemoveCommand extends AbstractCommand {

    public RemoveCommand(String usage, String description) {
        super(usage, description);
    }

    public void execute(NamedCache cache, String[] args) throws Exception {
        if (checkArgCount(args, 1)) {
            System.out.println(">> item removed: " + cache.remove(args[0]));
        }
    }
}