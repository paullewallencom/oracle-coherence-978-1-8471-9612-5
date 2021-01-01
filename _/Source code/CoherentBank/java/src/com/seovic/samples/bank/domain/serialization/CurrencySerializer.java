package com.seovic.samples.bank.domain.serialization;


import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PofReader;
import com.seovic.coherence.io.pof.AbstractPofSerializer;

import java.io.IOException;
import java.util.Currency;


/**
 * @author Aleksandar Seovic  2009.11.30
 */
public class CurrencySerializer
        extends AbstractPofSerializer<Currency> {
    protected void serializeAttributes(Currency currency, PofWriter writer)
            throws IOException {
        writer.writeString(0, currency.getCurrencyCode());
    }

    protected Currency createInstance(PofReader reader)
            throws IOException {
        return Currency.getInstance(reader.readString(0));
    }
}
