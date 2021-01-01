#ifndef BANK_MANAGER_HPP
#define BANK_MANAGER_HPP

#include "Transaction.hpp"

#include "coherence/lang.ns"
#include "coherence/net/NamedCache.hpp"

using namespace coherence::lang;
using coherence::net::NamedCache;

class BankManager
    {
	public:
	// ----- constructor ----------------------------------------------------
	
		BankManager();
		
		BankManager(const std::string& accountsCache);
		
	
	// ----- destructor ------------------------------------------------------
	
		~BankManager();
	
    // ----- public API ------------------------------------------------------

		void deposit(long long accountId, const Money& oAmount, Transaction& oResult);
		void withdraw(long long accountId, const Money& oMoney, Transaction& oResult);
		
    // ---- data members -----------------------------------------------------

	protected:
		FinalHandle<NamedCache> m_hAccountsCache;
	};

#endif 
