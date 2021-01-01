package ch02.loader;

import java.util.Map;

public interface Source extends Iterable<Map<String, ?>> {
    void beginExport();
    void endExport();
}
