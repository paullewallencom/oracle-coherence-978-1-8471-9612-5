#include "TransactionId.hpp"


// ----- constructors -------------------------------------------------------

TransactionId::TransactionId(long long lAccountId, long long lTransactionNumber)
	: m_lAccountId(lAccountId), m_lTransactionNumber(lTransactionNumber)
    {
    }

TransactionId::TransactionId()
    {
    }

// ----- accessors ----------------------------------------------------------

long long TransactionId::getAccountId() const
    {
    return m_lAccountId;
    }

long long TransactionId::getTransactionNumber() const
    {
    return m_lTransactionNumber;
    }


	// ----- free functions -----------------------------------------------------

std::ostream& operator<<(std::ostream& out, const TransactionId& id)
    {
    out << "TransactionId("
            << "AccountId="     << id.getAccountId()
            << ", TransactionNumber=" << id.getTransactionNumber()
            << ')';
    return out;
    }

bool operator==(const TransactionId& idA, const TransactionId& idB)
    {
    return idA.getAccountId()         == idB.getAccountId()   &&
           idA.getTransactionNumber() == idB.getTransactionNumber();  
    }

size_t hash_value(const TransactionId& id)
    {
    return size_t(&id); // identity hash (note: not suitable for cache keys)
    }
