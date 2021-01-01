package com.seovic.coherence.cachemanager.commands;

import com.seovic.coherence.cachemanager.CacheItemSpecification;
import com.seovic.coherence.cachemanager.Country;
import com.tangosol.net.NamedCache;
import com.tangosol.util.UUID;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

public class LoadCommand extends AbstractCommand {

    public LoadCommand(String usage, String description) {
        super(usage, description);
    }

    public void execute(NamedCache cache, String[] args) throws Exception {
        if (checkArgCount(args, 1)) {

            int itemCount = 0;
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(args[0]));

                String className = reader.readLine();
                String keyGenerationStrategy = reader.readLine();
                String propertySpec = reader.readLine();

                CacheItemSpecification cis =
                        CacheItemSpecification.create(cache,
                                                      className,
                                                      keyGenerationStrategy,
                                                      propertySpec.split(","));

                String itemLine = reader.readLine();
                while (itemLine != null && itemLine.length() > 0) {
                    Object item = cis.parseItem(itemLine);
                    Object key = cis.generateKey(item);

                    cache.put(key, item);
                    
                    itemCount++;
                    itemLine = reader.readLine();
                }

            }
            catch (IOException ioe) {
                System.out.println(">> loading error: " + ioe);
            }
            finally {
                if (reader != null) try { reader.close(); } catch (Exception e) {}
            }

            System.out.println(">> loaded " + itemCount + " items");
        }
    }
}