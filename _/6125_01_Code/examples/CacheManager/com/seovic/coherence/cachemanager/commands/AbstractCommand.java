package com.seovic.coherence.cachemanager.commands;

public abstract class AbstractCommand implements Command {

    private String usage;
    private String description;

    public AbstractCommand(String usage, String description) {
        this.usage = usage;
        this.description = description;
    }

    public String getUsage() {
        return usage;
    }

    public String getDescription() {
        return description;
    }

    protected boolean checkArgCount(String[] args, int requiredCount) {
        if (args.length < requiredCount) {
            System.out.println(getUsage());
            return false;
        }

        return true;
    }
}
