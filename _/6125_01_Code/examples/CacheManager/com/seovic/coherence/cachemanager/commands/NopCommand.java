package com.seovic.coherence.cachemanager.commands;

import com.tangosol.net.NamedCache;

public class NopCommand extends AbstractCommand {

    public NopCommand(String usage, String description) {
        super(usage, description);
    }

    public void execute(NamedCache cache, String[] args) throws Exception {
    }
}
