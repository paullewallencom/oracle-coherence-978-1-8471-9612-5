package com.seovic.coherence.cachemanager.commands;

import com.tangosol.net.NamedCache;

public interface Command {
    void execute(NamedCache cache, String[] args) throws Exception;

    String getUsage();
    String getDescription();
}
