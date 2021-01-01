#include "Money.hpp"

#include "NumberHelper.hpp"
#include "StringHelper.hpp"

#include "MoneyFormatException.hpp"

#include <sstream>
#include <string>
#include <vector>

#define DELIMITER "."

double pow(double dfl, int nScale);

Money::Money(const std::string& sAmount, const std::string& sCurrency)
	: m_oCurrency(sCurrency)
    {
	init(sAmount);	
    }
	
Money::Money(const std::string& sAmount, const Currency& oCurrency)
	: m_oCurrency(oCurrency)
    {
	init(sAmount);
    }
	
Money::Money(long long lUnscaledValue, int iScale, const std::string& sCurrencyCode) 
	: m_lUnscaledValue(lUnscaledValue), m_iScale(iScale), m_oCurrency(sCurrencyCode)
	{	
	}
		
Money::Money(long long lUnscaledValue, int iScale, const Currency& sCurrencyCode)
	: m_lUnscaledValue(lUnscaledValue), m_iScale(iScale), m_oCurrency(sCurrencyCode)
	{
	}

Money::Money()
	: m_lUnscaledValue(0), m_iScale(0), m_oCurrency("")
    {
    }

// ----- accessors ----------------------------------------------------------

long long Money::getUnscaledValue() const
    {
    return m_lUnscaledValue;
    }

void Money::setUnscaledValue(long long lUnscaledValue)
    {
    m_lUnscaledValue = lUnscaledValue;
    }

int Money::getScale() const
    {
    return m_iScale;
    }

void Money::setScale(int iScale)
    {
    m_iScale = iScale;
    }

	
Currency Money::getCurrency() const
    {
    return m_oCurrency;
    }

void Money::setCurrency(const Currency& oCurrency)
    {
    m_oCurrency = oCurrency;
    }

std::string Money::toShortRepresentation()
	{
	std::stringstream buf;
	buf << (m_lUnscaledValue / pow(10.0, m_iScale)) 
		<< " " << m_oCurrency.getCurrencyCode();
	return buf.str();
	}
	
// ----- helper functions --------------------------------------------------

void Money::init(const std::string& sAmount)
	{
	std::vector<std::string> tokens = StringHelper::split(sAmount, DELIMITER);
	if (tokens.size() == 1) 
		{
		if (!StringHelper::isNumber(tokens[0]))
			{
			throw MoneyFormatException(sAmount);
			}
		setUnscaledValue(NumberHelper::toLong(tokens[0]));
		setScale(0);
		}
	else if (tokens.size() == 2) 
		{
		std::string sUnscaledValue = tokens[0] + tokens[1];
		if (!StringHelper::isNumber(sUnscaledValue))
			{
			throw MoneyFormatException(sAmount);
			}
		setUnscaledValue(NumberHelper::toLong(sUnscaledValue));
		setScale(tokens[1].length());
		}
	else 
		{
		throw MoneyFormatException(sAmount);
		}
	}

// ----- free functions -----------------------------------------------------

double pow(double dfl, int nScale)
	{
	double dflResult = dfl;
	while (--nScale > 0)
		{
		dflResult *= dfl;
		}
	return dflResult;	
	}
	
std::ostream& operator<<(std::ostream& out, const Money& money)
    {
    out << "Money("
            << "Amount=" << (money.getUnscaledValue() / pow(10.0, money.getScale()))
            << ", Currency=" << money.getCurrency().getCurrencyCode()
            << ')';
    return out;
    }

bool operator==(const Money& moneyA, const Money& moneyB)
    {
    return moneyA.getUnscaledValue() == moneyB.getUnscaledValue()  &&
           moneyA.getScale()         == moneyB.getScale() &&
           moneyA.getCurrency()      == moneyB.getCurrency();  
    }

size_t hash_value(const Money& money)
    {
    return size_t(&money); // identity hash (note: not suitable for cache keys)
    }

