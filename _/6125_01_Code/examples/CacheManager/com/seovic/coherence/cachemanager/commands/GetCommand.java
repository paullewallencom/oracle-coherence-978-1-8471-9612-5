package com.seovic.coherence.cachemanager.commands;

import com.tangosol.net.NamedCache;

public class GetCommand extends AbstractCommand {

    public GetCommand(String usage, String description) {
        super(usage, description);
    }

    public void execute(NamedCache cache, String[] args) throws Exception {
        if (checkArgCount(args, 1)) {
            System.out.println(">> " + cache.get(args[0]));
        }
    }
}