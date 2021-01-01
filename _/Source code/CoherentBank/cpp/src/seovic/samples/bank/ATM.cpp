#include <iostream>
#include <string>

#include "BankManager.hpp"
#include "Money.hpp"
#include "NumberHelper.hpp"

using coherence::lang::Exception;

/**
* This simple CLI that imitates ATM operations: deposit and withdraw
*
* @argc  the number of command line arguments (including the process name)
* @argv  <deposit|withdraw> <account number> <amount> <currency code>
*/
int main(int argc, char** argv)
    {
    try
        {
		if (argc<5) 
			{
			std::cout << "Usage: atm <deposit|withdraw> <account number> <amount> <currency code>" << std::endl;
			return 1;
			}
		std::string sOperation = argv[1];
		std::string sAccount   = argv[2];
		std::string sAmount    = argv[3];
		std::string sCurrency  = argv[4];

		// convert account number to int
		long long lAccount = NumberHelper::toLong(sAccount);
				
		Money       oMoney(sAmount, sCurrency);
		Transaction oResult;
		BankManager oManager;
			
		if (sOperation.compare("deposit") == 0) 
			{
			oManager.deposit(lAccount, oMoney, oResult);
			}
		else if (sOperation.compare("withdraw") == 0)
			{
			oManager.withdraw(lAccount, oMoney, oResult);
			}
		else
			{
			std::cerr << "Unsupported operation: " << sOperation << std::endl;
			return 1;
			}
		std::cout << "Transaction Details\n" 
				  << "----------------------------------\n" 
				  << "Type:        " << oResult.getType().getType() << "\n"
				  << "Description: " << oResult.getDescription() << "\n"
				  << "Amount:      " << oResult.getAmount().toShortRepresentation() << "\n"
				  << "Balance:     " << oResult.getBalance().toShortRepresentation() << std::endl;
		}
	catch (Exception::View vEx)
		{
		std::cerr << vEx->getCause()->getName() << ": " << vEx->getCause()->getMessage() << std::endl;
		}
    catch (const std::exception& ex)
        {
        std::cerr << "Error: " << ex.what() << std::endl;
        return 1;
        }
    return 0;
    }
