package com.seovic.coherence.cachemanager;

import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;
import com.tangosol.util.UUID;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CacheItemSpecification implements Serializable {

    private Class itemClass;
    private String keyGenerationStrategy;
    private String[] propertyNames;

    transient private Map<String, Method> methodMap;
    transient private Method[] properties;
    transient private KeyGenerator keyGen;

    private CacheItemSpecification(Class itemClass, String keyGenerationStrategy, String[] propertyNames) {
        this.itemClass = itemClass;
        this.keyGenerationStrategy = keyGenerationStrategy;
        this.propertyNames = propertyNames;
    }

    public static CacheItemSpecification create(NamedCache cache, String className, String keyGenerationStrategy, String[] propertyNames) throws ClassNotFoundException, NoSuchMethodException {
        CacheItemSpecification cis = new CacheItemSpecification(Class.forName(className), keyGenerationStrategy, propertyNames);

        NamedCache cacheManagerConfig = CacheFactory.getCache("config-cacheManager");
        cacheManagerConfig.put(cache.getCacheName() + "-itemSpec", cis);

        return cis;
    }

    public static CacheItemSpecification get(NamedCache cache) {
        NamedCache cacheManagerConfig = CacheFactory.getCache("config-cacheManager");
        return (CacheItemSpecification) cacheManagerConfig.get(cache.getCacheName() + "-itemSpec");
    }

    public Object parseItem(String itemLine) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        String[] parts = itemLine.split(",");

        if (properties == null) {
            resolveProperties();
        }

        Object item = itemClass.newInstance();
        for (int i = 0; i < parts.length; i++) {
            properties[i].invoke(item, parts[i]);
        }

        return item;
    }

    public Object generateKey(Object item) throws Exception {
        if (keyGen == null) {
            keyGen = getKeyGenerator();
        }

        return keyGen.generateKey(item);
    }

    protected KeyGenerator getKeyGenerator() {
        if (keyGenerationStrategy.startsWith("uuid")) {
            return new UUIDKeyGenerator();
        }
        else if (keyGenerationStrategy.startsWith("incremental")) {
            String[] parts = keyGenerationStrategy.split(":");
            long nextValue = (parts.length > 1 ? Long.parseLong(parts[1]) : 1);
            return new IncrementalKeyGenerator(nextValue);
        }
        else if (keyGenerationStrategy.startsWith("property")) {
            String[] parts = keyGenerationStrategy.split(":");
            if (parts.length < 2) {
                throw new IllegalArgumentException("Property name for the property key generation strategy must be specified");
            }
            Method keyProperty = methodMap.get("get" + parts[1]);
            if (keyProperty == null) {
                throw new IllegalArgumentException("Key property with the name '" + parts[1] + "' does not exist");
            }
            return new PropertyKeyGenerator(keyProperty);
        }
        else {
            throw new IllegalArgumentException("Invalid key generation strategy specified: " + keyGenerationStrategy);
        }

    }

    protected void resolveProperties() throws NoSuchMethodException {
        methodMap = new HashMap<String, Method>();
        Method[] allMethods = itemClass.getMethods();
        for (int i = 0; i < allMethods.length; i++) {
            Method method = allMethods[i];
            methodMap.put(method.getName(), method);
        }

        properties = new Method[propertyNames.length];
        for (int i = 0; i < propertyNames.length; i++) {
            Method setter = methodMap.get("set" + propertyNames[i]);
            if (setter == null) {
                throw new NoSuchMethodException("set" + propertyNames[i]);
            }
            properties[i] = setter;
        }
    }

    interface KeyGenerator {
        Object generateKey(Object item) throws Exception;
    }

    class UUIDKeyGenerator implements KeyGenerator {
        public Object generateKey(Object item) throws Exception {
            return new UUID();
        }
    }

    class IncrementalKeyGenerator implements KeyGenerator {
        private long nextValue = 0;

        IncrementalKeyGenerator(long nextValue) {
            this.nextValue = nextValue;
        }

        public Object generateKey(Object item) throws Exception {
            return nextValue++;
        }
    }

    class PropertyKeyGenerator implements KeyGenerator {
        private Method keyProperty;

        PropertyKeyGenerator(Method keyProperty) {
            this.keyProperty = keyProperty;
        }

        public Object generateKey(Object item) throws Exception {
            return keyProperty.invoke(item, (Object[]) null);
        }
    }



}
