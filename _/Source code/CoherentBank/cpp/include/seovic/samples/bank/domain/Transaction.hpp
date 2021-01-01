#ifndef BANK_TRANSACTION_HPP
#define BANK_TRANSACTION_HPP

#include "TransactionId.hpp"
#include "TransactionType.hpp"
#include "Money.hpp"

#include <iostream>
#include <string>

class Transaction
    {
    // ----- constructors ---------------------------------------------------

    public:
        Transaction();
   
        Transaction(const TransactionId& id, const TransactionType& type, 
				const struct tm& time, const std::string& desc, 
				const Money& amount, const Money& balance);


    // ----- accessors ------------------------------------------------------

    public:
     
        TransactionId getId() const;

		TransactionType getType() const;
		
		struct tm getTime() const;
		
        std::string getDescription() const;

        Money getAmount() const;

        Money getBalance() const;

        
    // ----- data members ---------------------------------------------------

    private:

		TransactionId m_id;
		
		TransactionType m_type;
		
		struct tm m_time;
		
        std::string m_description;

        Money m_amount;

        Money m_balance;

    };


// ----- free functions -----------------------------------------------------

/**
* Output this Transaction to the stream
*
* @param out  the stream to output to
*
* @return the stream
*/
std::ostream& operator<<(std::ostream& out, const Transaction& t);

/**
* Perform an equality test on two Transaction objects
*
* @param tA  the first Transaction
* @param tB  the second Transaction
*
* @return true if the objects are equal
*/
bool operator==(const Transaction& tA, const Transaction& tB);

/**
* Return the has for the Transaction.
*
* @param t  the Transaction to hash
*
* @return the hash for the Transaction
*/
size_t hash_value(const Transaction& t);

#endif // BANK_TRANSACTION_HPP
