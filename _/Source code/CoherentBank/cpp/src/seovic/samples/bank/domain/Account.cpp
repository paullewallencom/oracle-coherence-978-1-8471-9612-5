#include "Account.hpp"


// ----- constructors -------------------------------------------------------

Account::Account(long long lId, const std::string& sDesc, 
				 const Money& balance, long long lLastTransId,
				 long long lCustomerId)
	: m_lId(lId),
	  m_sDescription(sDesc),
	  m_balance(balance),
	  m_lLastTransactionId(lLastTransId),
	  m_lCustomerId(lCustomerId)
    {
    }

Account::Account()
	: m_lId(0), m_lLastTransactionId(0), m_lCustomerId(0)
    {
    }

// ----- accessors ----------------------------------------------------------

long long Account::getId() const
    {
    return m_lId;
    }

std::string Account::getDescription() const
    {
    return m_sDescription;
    }

void Account::setDescription(const std::string& sDesc)
    {
    m_sDescription = sDesc;
    }

Money Account::getBalance() const
    {
	return m_balance;
    }

long long Account::getLastTransactionId() const
	{
	return m_lLastTransactionId;
	}
	
long long Account::getCustomerId() const
    {
    return m_lCustomerId;
    }


// ----- free functions -----------------------------------------------------

std::ostream& operator<<(std::ostream& out, const Account& account)
    {
    out << "Account("
            << "Id="                  << account.getId()
            << ", Description="       << account.getDescription()
			<< ", Balance="           << account.getBalance()
			<< ", LastTransactionId=" << account.getLastTransactionId()
            << ", CustomerId="        << account.getCustomerId()
            << ')';
    return out;
    }

bool operator==(const Account& accountA, const Account& accountB)
    {
    return accountA.getId()                == accountB.getId()                &&
           accountA.getDescription()       == accountB.getDescription()       &&
		   accountA.getBalance()           == accountB.getBalance()           &&
		   accountA.getLastTransactionId() == accountB.getLastTransactionId() &&
           accountA.getCustomerId()        == accountB.getCustomerId();  
    }

size_t hash_value(const Account& account)
    {
	return (size_t) account.getId();
    }
