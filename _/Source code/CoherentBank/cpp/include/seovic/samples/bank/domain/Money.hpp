#ifndef BANK_MONEY_HPP
#define BANK_MONEY_HPP

#include "Currency.hpp"

#include <iostream>
#include <string>

/**
* 
*/
class Money
    {
    // ----- constructors ---------------------------------------------------

    public:
  
        Money();

		Money(const std::string& sAmount, const std::string& sCurrencyCode);
		
		Money(const std::string& sAmount, const Currency& currencyCode);
		
		Money(long long lUnscaledValue, int iScale, const std::string& sCurrencyCode);
		
		Money(long long lUnscaledValue, int iScale, const Currency& currencyCode);


    // ----- accessors ------------------------------------------------------

    public:
   
        long long getUnscaledValue() const;

        void setUnscaledValue(long long lUnscaledValue);

        int getScale() const;

        void setScale(int iScale);

		Currency getCurrency() const;

        void setCurrency(const Currency& oCurrency);
		
		std::string toShortRepresentation();

    // ----- helper functions -----------------------------------------------

	protected:

		void init(const std::string& sAmount);
		
		
    // ----- data members ---------------------------------------------------

    private:
        /**
        * Unscaled value.
        */
        long long m_lUnscaledValue;

        /**
        * Scale.
        */
        int m_iScale;		

        /**
        * Currency
        */
        Currency m_oCurrency;
    };


// ----- free functions -----------------------------------------------------

/**
* Output this Money to the stream
*
* @param out  the stream to output to
*
* @return the stream
*/
std::ostream& operator<<(std::ostream& out, const Money& money);

/**
* Perform an equality test on two Money objects
*
* @param moneyA  the first Money
* @param moneyB  the second Money
*
* @return true if the objects are equal
*/
bool operator==(const Money& moneyA, const Money& moneyB);

/**
* Return the hash for the Money.
*
* @param money  the Money to hash
*
* @return the hash for the Money
*/
size_t hash_value(const Money& money);

#endif // BANK_MONEY_HPP
