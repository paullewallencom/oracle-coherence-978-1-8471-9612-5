package com.seovic.coherence.cachemanager.commands;

import com.tangosol.net.NamedCache;

import java.util.Iterator;

public class ListCommand extends AbstractCommand {

    public ListCommand(String usage, String description) {
        super(usage, description);
    }

    public void execute(NamedCache cache, String[] args) throws Exception {
        for (Iterator iter = cache.keySet().iterator(); iter.hasNext(); ) {
            Object key = iter.next();
            System.out.println(">> " + key + " = " + cache.get(key));
        }
    }
}