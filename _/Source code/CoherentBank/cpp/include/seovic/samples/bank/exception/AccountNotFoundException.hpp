#ifndef BANK_ACCOUNT_NOT_FOUND_EXCEPTION_HPP
#define BANK_ACCOUNT_NOT_FOUND_EXCEPTION_HPP

#include <stdexcept>

class AccountNotFoundException : public std::runtime_error
	{
	public:
		AccountNotFoundException::AccountNotFoundException()
			: runtime_error("Account not found" ) {}
	};

#endif // BANK_ACCOUNT_NOT_FOUND_EXCEPTION_HPP