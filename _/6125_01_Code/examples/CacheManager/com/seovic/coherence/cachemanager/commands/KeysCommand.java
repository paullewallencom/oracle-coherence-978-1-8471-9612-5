package com.seovic.coherence.cachemanager.commands;

import com.tangosol.net.NamedCache;

import java.util.Iterator;

public class KeysCommand extends AbstractCommand {

    public KeysCommand(String usage, String description) {
        super(usage, description);
    }

    public void execute(NamedCache cache, String[] args) throws Exception {
        for (Iterator iter = cache.keySet().iterator(); iter.hasNext(); ) {
            System.out.println(">> " + iter.next());
        }
    }
}