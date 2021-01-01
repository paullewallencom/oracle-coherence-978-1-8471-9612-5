#include "TransactionType.hpp"


// ----- constructors -------------------------------------------------------

TransactionType::TransactionType(const std::string& sType)
	: m_sType(sType)
    {
    }

TransactionType::TransactionType()
    {
    }

// ----- accessors ----------------------------------------------------------

std::string TransactionType::getType() const
    {
    return m_sType;
    }


// ----- free functions -----------------------------------------------------

std::ostream& operator<<(std::ostream& out, const TransactionType& type)
    {
    out << "TransactionType(" << type.getType() << ')';
    return out;
    }

bool operator==(const TransactionType& typeA, const TransactionType& typeB)
    {
    return typeA.getType()  == typeB.getType();  
    }

size_t hash_value(const TransactionType& type)
    {
    return size_t(&type); // identity hash (note: not suitable for cache keys)
    }
