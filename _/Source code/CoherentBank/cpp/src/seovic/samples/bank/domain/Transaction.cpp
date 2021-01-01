#include "Transaction.hpp"


// ----- constructors -------------------------------------------------------

Transaction::Transaction()
	: m_id(-1, -1), m_type("")
    {
    }

Transaction::Transaction(const TransactionId& id, const TransactionType& type, 
				const struct tm& time, const std::string& desc, 
				const Money& amount, const Money& balance)
	: m_id(id), m_type(type), m_time(time), m_description(desc), 
		m_amount(amount), m_balance(balance)
	{
    }

// ----- accessors ----------------------------------------------------------

TransactionId Transaction::getId() const
	{
	return m_id;
	}

TransactionType Transaction::getType() const
	{
	return m_type;
	}
		
struct tm Transaction::getTime() const
	{
	return m_time;
	}
		
std::string Transaction::getDescription() const
	{
	return m_description;
	}

Money Transaction::getAmount() const
	{
	return m_amount;
	}

Money Transaction::getBalance() const
	{
	return m_balance;
	}


// ----- free functions -----------------------------------------------------

std::ostream& operator<<(std::ostream& out, const Transaction& t)
    {
    out << "Transaction("
            << "Id=" << t.getId()
			<< ", Type=" << t.getType()
            << ", Description=" << t.getDescription()
            << ", Amount="  << t.getAmount()
            << ", Balance=" << t.getBalance()
            << ')';
    return out;
    }

bool operator==(const struct tm& tmA, const struct tm& tmB)
	{
	return memcmp(&tmA, &tmB, sizeof(tm)) == 0;
	}
	
bool operator==(const Transaction& tA, const Transaction& tB)
    {
    return 	tA.getId() == tB.getId() &&
			tA.getType() == tB.getType() &&
			tA.getTime() == tB.getTime() &&
			tA.getDescription() == tB.getDescription() &&
			tA.getAmount()   == tB.getAmount()   &&
			tA.getBalance()  == tB.getBalance();  
    }

size_t hash_value(const Transaction& t)
    {
    return size_t(&t); // identity hash (note: not suitable for cache keys)
    }
