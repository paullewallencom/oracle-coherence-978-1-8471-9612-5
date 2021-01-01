#include "coherence/lang.ns"

#include "coherence/io/pof/PofWriter.hpp"
#include "coherence/io/pof/PofReader.hpp"
#include "coherence/io/pof/SystemPofContext.hpp"

#include "TransactionType.hpp"
#include "PofConfig.hpp"

#include <string>

using namespace coherence::lang;

using coherence::io::pof::PofReader;
using coherence::io::pof::PofWriter;


COH_REGISTER_MANAGED_CLASS(POF_TYPE_TRANSACTION_TYPE, TransactionType);

template<> void serialize<TransactionType>(PofWriter::Handle hOut, const TransactionType& type)
    {
    hOut->writeString(0, type.getType());
    }

template<> TransactionType deserialize<TransactionType>(PofReader::Handle hIn)
    {
    std::string sType = hIn->readString(0);
    return TransactionType(sType);
    }
