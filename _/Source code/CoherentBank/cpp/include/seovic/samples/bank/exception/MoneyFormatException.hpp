#ifndef BANK_MONEY_FORMAT_EXCEPTION_HPP
#define BANK_MONEY_FORMAT_EXCEPTION_HPP

#include <stdexcept>

class MoneyFormatException : public std::runtime_error
	{
	public:
		MoneyFormatException::MoneyFormatException(const std::string& format) 
			: runtime_error("Money format used \"" + format +
			  "\" is not valid. \nValid format is (\\d)*((\\.)(\\d)+)?") {}
	};

#endif // BANK_MONEY_FORMAT_EXCEPTION_HPP