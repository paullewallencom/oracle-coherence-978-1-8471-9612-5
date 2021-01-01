#ifndef BANK_TRANSACTION_ID_HPP
#define BANK_TRANSACTION_ID_HPP

#include <iostream>

class TransactionId
    {
    // ----- constructors ---------------------------------------------------

    public:
   
        TransactionId(long long lAccountId, long long lTransactionNumber);

    protected:
        /**
        * Default constructor.
        */
        TransactionId();


    // ----- accessors ------------------------------------------------------

    public:
     
        long long getAccountId() const;

        long long getTransactionNumber() const;

        
    // ----- data members ---------------------------------------------------

    private:

		long long m_lAccountId;

        long long m_lTransactionNumber;

    };


// ----- free functions -----------------------------------------------------

/**
* Output this TransactionId to the stream
*
* @param out  the stream to output to
*
* @return the stream
*/
std::ostream& operator<<(std::ostream& out, const TransactionId& id);

/**
* Perform an equality test on two TransactionId objects
*
* @param idA  the first TransactionId
* @param idB  the second TransactionId
*
* @return true if the objects are equal
*/
bool operator==(const TransactionId& idA, const TransactionId& idB);

/**
* Return the has for the TransactionId.
*
* @param id  the TransactionId to hash
*
* @return the hash for the TransactionId
*/
size_t hash_value(const TransactionId& id);

#endif // BANK_TRANSACTION_ID_HPP
