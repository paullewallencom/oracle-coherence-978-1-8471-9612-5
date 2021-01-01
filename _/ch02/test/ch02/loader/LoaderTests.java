package ch02.loader;


import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

import ch02.Country;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;


public class LoaderTests {
    public static final NamedCache countries =
            CacheFactory.getCache("countries");

    @Before
    public void clearCache() {
        countries.clear();
    }

    @Test
    public void testCsvToCoherenceLoader() {
        Source source = new CsvSource("countries.csv");
        CoherenceTarget target =
                new CoherenceTarget("countries", Country.class, "code");
        target.setBatchSize(50);
        Loader loader = new Loader(source, target);
        loader.load();

        assertEquals(193, countries.size());

        Country srb = (Country) countries.get("SRB");
        assertEquals("Serbia", srb.getName());
        assertEquals("Belgrade", srb.getCapital());
        assertEquals("RSD", srb.getCurrencySymbol());
        assertEquals("Dinar", srb.getCurrencyName());
    }
}
