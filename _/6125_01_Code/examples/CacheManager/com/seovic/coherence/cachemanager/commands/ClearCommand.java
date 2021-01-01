package com.seovic.coherence.cachemanager.commands;

import com.tangosol.net.NamedCache;

public class ClearCommand extends AbstractCommand {

    public ClearCommand(String usage, String description) {
        super(usage, description);
    }

    public void execute(NamedCache cache, String[] args) throws Exception {
        cache.clear();
        System.out.println(">> cache cleared");
    }
}