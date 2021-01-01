#include "coherence/lang.ns"

#include "coherence/io/pof/PofWriter.hpp"
#include "coherence/io/pof/PofReader.hpp"
#include "coherence/io/pof/SystemPofContext.hpp"

#include "TransactionId.hpp"
#include "PofConfig.hpp"

using namespace coherence::lang;

using coherence::io::pof::PofReader;
using coherence::io::pof::PofWriter;


COH_REGISTER_MANAGED_CLASS(POF_TYPE_TRANSACTION_ID, TransactionId);

template<> void serialize<TransactionId>(PofWriter::Handle hOut, const TransactionId& id)
    {
    hOut->writeInt64(0, id.getAccountId());
	hOut->writeInt64(1, id.getTransactionNumber());
    }

template<> TransactionId deserialize<TransactionId>(PofReader::Handle hIn)
    {
    long long lAccountId         = hIn->readInt64(0);
	long long lTransactionNumber = hIn->readInt64(1);
	
    return TransactionId(lAccountId, lTransactionNumber);
    }
