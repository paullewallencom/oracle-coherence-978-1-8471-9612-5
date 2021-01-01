/**
* This file defines serializers for the ContactInfo class and registers its
* managed form (Managed<ContactInfo>) with Coherence.
*/
#include "coherence/lang.ns"

#include "coherence/io/pof/PofWriter.hpp"
#include "coherence/io/pof/PofReader.hpp"
#include "coherence/io/pof/SystemPofContext.hpp"

#include "Money.hpp"
#include "PofConfig.hpp"

#include <string>

using namespace coherence::lang;

using coherence::io::pof::PofReader;
using coherence::io::pof::PofWriter;


COH_REGISTER_MANAGED_CLASS(POF_TYPE_MONEY, Money);

template<> void serialize<Money>(PofWriter::Handle hOut, const Money& money)
    {
    hOut->writeInt64(0, money.getUnscaledValue());
    hOut->writeInt32(1, money.getScale());
    hOut->writeObject(2, Managed<Currency>::create(money.getCurrency()));
    }

template<> Money deserialize<Money>(PofReader::Handle hIn)
    {
    long long lUnscaledValue = hIn->readInt64(0);
    int       iScale         = hIn->readInt32(1);
	Managed<Currency>::View currency = cast<Managed<Currency>::View>(hIn->readObject(2));
    return Money(lUnscaledValue, iScale, *currency);
    }
