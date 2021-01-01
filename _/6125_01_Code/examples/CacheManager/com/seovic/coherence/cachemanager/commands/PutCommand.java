package com.seovic.coherence.cachemanager.commands;

import com.tangosol.net.NamedCache;
import com.seovic.coherence.cachemanager.CacheItemSpecification;

public class PutCommand extends AbstractCommand {

    public PutCommand(String usage, String description) {
        super(usage, description);
    }

    public void execute(NamedCache cache, String[] args) throws Exception {
        if (checkArgCount(args, 2)) {
            Object item = args[1];
            CacheItemSpecification cis = CacheItemSpecification.get(cache);
            if (cis != null) {
                item = cis.parseItem(args[1]);
            }
            System.out.println(">> old value: " + cache.put(args[0], item));
        }
    }
}