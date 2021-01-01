#include "coherence/lang.ns"

#include "coherence/net/CacheFactory.hpp"

#include "BankManager.hpp"
#include "AccountNotFoundException.hpp"
#include "DepositProcessor.hpp"
#include "WithdrawProcessor.hpp"

using coherence::net::CacheFactory;

#define ACCOUNTS_CACHE_NAME "accounts"

BankManager::BankManager()
	: m_hAccountsCache(System::common(),
		CacheFactory::getCache(ACCOUNTS_CACHE_NAME))
	{
    }
	
BankManager::BankManager(const std::string& sCacheName)
	: m_hAccountsCache(System::common(),
		CacheFactory::getCache(sCacheName))
	{
	}
	
BankManager::~BankManager() 
	{
	CacheFactory::shutdown();
	}

void BankManager::deposit(long long lAccountId, const Money& oMoney, Transaction& oResult) 
	{
	// std::cout << "In BankManager::deposit"<< std::endl;

	Integer64::View          vId   = Integer64::create(lAccountId);
	DepositProcessor::Handle hProc = DepositProcessor::create(
		Managed<Money>::create(oMoney), "ATM deposit");
			
	Managed<Transaction>::View vResult = cast<Managed<Transaction>::View>(
		m_hAccountsCache->invoke(vId, hProc));

	if (vResult == NULL)
		{
		throw AccountNotFoundException();
		}
	oResult = *vResult;
	}

void BankManager::withdraw(long long lAccountId, const Money& oMoney, Transaction& oResult)
	{
	// std::cout << "In BankManager::withdraw" << std::endl;

	Integer64::View           vId   = Integer64::create(lAccountId);
	WithdrawProcessor::Handle hProc = WithdrawProcessor::create(
		Managed<Money>::create(oMoney), "ATM withdrawal");
			
	Managed<Transaction>::View vResult = cast<Managed<Transaction>::View>(
		m_hAccountsCache->invoke(vId, hProc));
			
	if (vResult == NULL)
		{
		throw AccountNotFoundException();
		}
	oResult = *vResult;
    }

