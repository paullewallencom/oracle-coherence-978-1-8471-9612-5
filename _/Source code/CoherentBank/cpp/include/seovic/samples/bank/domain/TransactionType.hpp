#ifndef BANK_TRANSACTION_TYPE_HPP
#define BANK_TRANSACTION_TYPE_HPP

#include <iostream>
#include <string>

#define DEPOSIT    "DEPOSIT"
#define WITHDRAWAL "WITHDRAWAL"

class TransactionType
    {
    // ----- constructors ---------------------------------------------------

    public:
   
        TransactionType(const std::string& sType);

    protected:
        /**
        * Default constructor.
        */
        TransactionType();


    // ----- accessors ------------------------------------------------------

    public:
     
        std::string getType() const;


	// ----- data members ---------------------------------------------------

    private:

        std::string m_sType;
    };


// ----- free functions -----------------------------------------------------

/**
* Output this TransactionType to the stream
*
* @param out  the stream to output to
*
* @return the stream
*/
std::ostream& operator<<(std::ostream& out, const TransactionType& transactionType);

/**
* Perform an equality test on two TransactionType objects
*
* @param transactionTypeA  the first TransactionType
* @param transactionTypeB  the second TransactionType
*
* @return true if the objects are equal
*/
bool operator==(const TransactionType& transactionTypeA, const TransactionType& transactionTypeB);

/**
* Return the has for the TransactionType.
*
* @param transactionType  the TransactionType to hash
*
* @return the hash for the TransactionType
*/
size_t hash_value(const TransactionType& transactionType);

#endif // BANK_TRANSACTION_TYPE_HPP
