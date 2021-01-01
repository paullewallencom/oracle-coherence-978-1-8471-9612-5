#ifndef BANK_ACCOUNT_HPP
#define BANK_ACCOUNT_HPP

#include "Money.hpp"

#include <iostream>
#include <string>

class Account
    {
    // ----- constructors ---------------------------------------------------

    public:   
        Account(long long lId, const std::string& sDesc, 
		        const Money& balance, long long lLastTransId,
		        long long lCustomerId);

        Account();


    // ----- accessors ------------------------------------------------------

    public:
        long long getId() const;

        std::string getDescription() const;

        void setDescription(const std::string& sDesc);

        Money getBalance() const;

		long long getLastTransactionId() const;
		
        long long getCustomerId() const;

        
    // ----- data members ---------------------------------------------------

    private:
		const long long m_lId;
        std::string m_sDescription;
		Money m_balance;
    	long long m_lLastTransactionId;
        const long long m_lCustomerId;
    };


// ----- free functions -----------------------------------------------------

/**
* Output this Account to the stream
*
* @param out  the stream to output to
*
* @return the stream
*/
std::ostream& operator<<(std::ostream& out, const Account& account);

/**
* Perform an equality test on two Account objects
*
* @param accountA  the first Account
* @param accountB  the second Account
*
* @return true if the objects are equal
*/
bool operator==(const Account& accountA, const Account& accountB);

/**
* Return the hash for the Account.
*
* @param account  the Account to hash
*
* @return the hash for the Account
*/
size_t hash_value(const Account& account);

#endif // BAND_ACCOUNT_HPP
