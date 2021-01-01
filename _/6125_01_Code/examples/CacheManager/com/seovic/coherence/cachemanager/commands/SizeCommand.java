package com.seovic.coherence.cachemanager.commands;

import com.tangosol.net.NamedCache;

public class SizeCommand extends AbstractCommand {

    public SizeCommand(String usage, String description) {
        super(usage, description);
    }

    public void execute(NamedCache cache, String[] args) throws Exception {
        System.out.println(">> " + cache.size() + " items");
    }
}                            