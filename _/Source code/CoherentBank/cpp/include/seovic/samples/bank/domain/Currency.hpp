#ifndef BANK_CURRENCY_HPP
#define BANK_CURRENCY_HPP

#include <iostream>
#include <string>

/**
* Currency representation.
*/
class Currency
    {
    // ----- constructors ---------------------------------------------------

    public:
        /**
        * Default constructor.
        */
        Currency();

        /**
        * Create a new Currency object.
        *
        * @param sId   
        * @param sDesc  
        * @param sCurrency   
        * @param sCustomerId  
        */
        Currency(const std::string& sCode);


    // ----- accessors ------------------------------------------------------

    public:
        /**
        * Determine the code of the currency
        *
        * @return the code of the currency
        */
        std::string getCurrencyCode() const;

        /**
        * Set the code of the currency
        *
        * @param sCode  code of the currency
        */
        void setCurrencyCode(const std::string& sCode);

        
    // ----- data members ---------------------------------------------------

    private:
        /**
        * The currency code.
        */
        std::string m_sCurrencyCode;
    };


// ----- free functions -----------------------------------------------------

/**
* Output this Currency to the stream
*
* @param out  the stream to output to
*
* @return the stream
*/
std::ostream& operator<<(std::ostream& out, const Currency& currency);

/**
* Perform an equality test on two Currency objects
*
* @param currencyA  the first Currency
* @param currencyB  the second Currency
*
* @return true if the objects are equal
*/
bool operator==(const Currency& currencyA, const Currency& currencyB);

/**
* Return the hash for the Currency.
*
* @param currency  the Currency to hash
*
* @return the hash for the Currency
*/
size_t hash_value(const Currency& currency);

#endif // BANK_CURRENCY_HPP
