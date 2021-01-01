#include "coherence/lang.ns"

#include "coherence/io/pof/PofWriter.hpp"
#include "coherence/io/pof/PofReader.hpp"
#include "coherence/io/pof/SystemPofContext.hpp"

#include "Account.hpp"
#include "PofConfig.hpp"

#include <string>

using namespace coherence::lang;

using coherence::io::pof::PofReader;
using coherence::io::pof::PofWriter;


COH_REGISTER_MANAGED_CLASS(POF_TYPE_ACCOUNT, Account);

template<> void serialize<Account>(PofWriter::Handle hOut, const Account& account)
    {
    hOut->writeInt64(0, account.getId());
    hOut->writeString(1, account.getDescription());
	hOut->writeObject(2, Managed<Money>::create(account.getBalance()));
    hOut->writeInt64(3, account.getLastTransactionId());
    hOut->writeInt64(4, account.getCustomerId());
    }

template<> Account deserialize<Account>(PofReader::Handle hIn)
    {
    long long            lId         = hIn->readInt64(0);
    std::string          sDesc       = hIn->readString(1);
    Managed<Money>::View vBalance    = cast<Managed<Money>::View>(hIn->readObject(2));
    long long            lTransId    = hIn->readInt64(3);
    long long            lCustomerId = hIn->readInt64(4);
    return Account(lId, sDesc, *vBalance, lTransId, lCustomerId);
    }
