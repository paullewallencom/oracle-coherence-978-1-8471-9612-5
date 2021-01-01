#include "Currency.hpp"


// ----- constructors -------------------------------------------------------

Currency::Currency(const std::string& sCode)
	: m_sCurrencyCode(sCode)
    {
    }

Currency::Currency()
    {
    }

// ----- accessors ----------------------------------------------------------

std::string Currency::getCurrencyCode() const
    {
    return m_sCurrencyCode;
    }

void Currency::setCurrencyCode(const std::string& sCode)
    {
    m_sCurrencyCode = sCode;
    }


// ----- free functions -----------------------------------------------------

std::ostream& operator<<(std::ostream& out, const Currency& currency)
    {
    out << "Currency("
            << "Code=" << currency.getCurrencyCode()
            << ')';
    return out;
    }

bool operator==(const Currency& currencyA, const Currency& currencyB)
    {
    return currencyA.getCurrencyCode() == currencyB.getCurrencyCode();
    }

size_t hash_value(const Currency& currency)
    {
    return size_t(&currency); // identity hash (note: not suitable for cache keys)
    }
