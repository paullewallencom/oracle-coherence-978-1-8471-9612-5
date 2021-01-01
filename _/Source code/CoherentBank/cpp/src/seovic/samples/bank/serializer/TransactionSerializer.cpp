#include "coherence/lang.ns"

#include "coherence/io/pof/PofWriter.hpp"
#include "coherence/io/pof/PofReader.hpp"
#include "coherence/io/pof/RawDateTime.hpp"
#include "coherence/io/pof/SystemPofContext.hpp"

#include "Transaction.hpp"
#include "PofConfig.hpp"

#include <string>

using namespace coherence::lang;

using coherence::io::pof::PofReader;
using coherence::io::pof::PofWriter;
using coherence::io::pof::RawDateTime;


COH_REGISTER_MANAGED_CLASS(POF_TYPE_TRANSACTION, Transaction);

template<> void serialize<Transaction>(PofWriter::Handle hOut, const Transaction& t)
    {
    hOut->writeObject(0, Managed<TransactionId>::create(t.getId()));
	hOut->writeObject(1, Managed<TransactionType>::create(t.getType()));
	hOut->writeRawDateTime(2, RawDateTime::create(t.getTime()));
    hOut->writeString(3, t.getDescription());
    hOut->writeObject(4, Managed<Money>::create(t.getAmount()));
    hOut->writeObject(5, Managed<Money>::create(t.getBalance()));
    }

template<> Transaction deserialize<Transaction>(PofReader::Handle hIn)
    {
	Managed<TransactionId>::View   vId      = cast<Managed<TransactionId>::View>(hIn->readObject(0));
	Managed<TransactionType>::View vType    = cast<Managed<TransactionType>::View>(hIn->readObject(1));
    struct tm                      time     = *(hIn->readRawDateTime(2));
    std::string                    sDesc    = hIn->readString(3);
    Managed<Money>::View           vAmount  = cast<Managed<Money>::View>(hIn->readObject(4));
	Managed<Money>::View           vBalance = cast<Managed<Money>::View>(hIn->readObject(5));
	
    return Transaction(*vId, *vType, time, sDesc, *vAmount, *vBalance);
    }
