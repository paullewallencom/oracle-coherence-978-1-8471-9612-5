package ch02.loader;

import java.io.IOException;
import java.io.Reader;
import java.io.InputStreamReader;

import java.util.Iterator;
import java.util.Map;

import org.supercsv.io.ICsvMapReader;
import org.supercsv.io.CsvMapReader;
import org.supercsv.prefs.CsvPreference;

public class CsvSource implements Source {
    private ICsvMapReader reader;
    private String[]      header;

    public CsvSource(String name) {
        this(new InputStreamReader(
                CsvSource.class.getClassLoader()
                        .getResourceAsStream(name)));
    }

    public CsvSource(Reader reader) {
        this.reader = new CsvMapReader(reader, CsvPreference.STANDARD_PREFERENCE);
    }

    public void beginExport() {
        try {
            this.header = reader.getCSVHeader(false);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void endExport() {
        try {
            reader.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Iterator<Map<String, ?>> iterator() {
        return new CsvIterator();
    }

    private class CsvIterator implements Iterator<Map<String, ?>> {
        private Map<String, String> item;

        public boolean hasNext() {
            try {
                item = reader.read(header);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            return item != null;
        }

        public Map<String, ?> next() {
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException(
                    "CsvIterator does not support remove operation");
        }
    }
}
