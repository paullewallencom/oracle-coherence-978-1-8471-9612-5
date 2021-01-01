#include "coherence/lang.ns"

#include "coherence/io/pof/PofWriter.hpp"
#include "coherence/io/pof/PofReader.hpp"
#include "coherence/io/pof/SystemPofContext.hpp"

#include "Currency.hpp"
#include "PofConfig.hpp"

#include <string>

using namespace coherence::lang;

using coherence::io::pof::PofReader;
using coherence::io::pof::PofWriter;


COH_REGISTER_MANAGED_CLASS(POF_TYPE_CURRENCY, Currency);

template<> void serialize<Currency>(PofWriter::Handle hOut, const Currency& currency)
    {
    hOut->writeString(0, currency.getCurrencyCode());
    }

template<> Currency deserialize<Currency>(PofReader::Handle hIn)
    {
    std::string sCode = hIn->readString(0);
    return Currency(sCode);
    }
