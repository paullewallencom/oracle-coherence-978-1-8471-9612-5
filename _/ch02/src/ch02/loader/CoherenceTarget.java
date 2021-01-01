package ch02.loader;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

@SuppressWarnings("unchecked")
public class CoherenceTarget implements Target {
    public static final int DEFAULT_BATCH_SIZE = 1000;

    private NamedCache cache;
    private Class      itemClass;
    private String     idProperty;

    private Map batch;
    private int batchSize = DEFAULT_BATCH_SIZE;
    private int batchCount;

    public CoherenceTarget(String cacheName, Class itemClass,
                           String idProperty) {
        this.cache      = CacheFactory.getCache(cacheName);
        this.itemClass  = itemClass;
        this.idProperty = idProperty;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public void beginImport() {
        batch = new HashMap();
    }

    @SuppressWarnings("unchecked")
    public void importItem(Map<String, ?> sourceItem) {
        BeanWrapper targetItem = new BeanWrapperImpl(itemClass);
        for (Map.Entry<String, ?> property : sourceItem.entrySet()) {
            targetItem.setPropertyValue(property.getKey(),
                                        property.getValue());
        }
        Object id = targetItem.getPropertyValue(idProperty);

        batch.put(id, targetItem.getWrappedInstance());
        if (batch.size() % batchSize == 0) {
            ++batchCount;
            System.out.println("batch #" + batchCount
                               + ": " + batch.size() + " items");
            cache.putAll(batch);
            batch.clear();
        }
    }

    @SuppressWarnings("unchecked")
    public void endImport() {
        if (!batch.isEmpty()) {
            ++batchCount;
            System.out.println("batch #" + batchCount
                               + ": " + batch.size() + " items");
            cache.putAll(batch);
            System.out.println("Cache size: " + cache.size());
        }
    }
}
