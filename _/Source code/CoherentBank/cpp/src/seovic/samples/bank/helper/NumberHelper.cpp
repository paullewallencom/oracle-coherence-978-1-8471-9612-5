#include "NumberHelper.hpp"

#include <sstream>


// ----- NumberHelper interface ---------------------------------------------

long long NumberHelper::toLong(const std::string& s) {
    long lValue;
	std::istringstream iss(s);
	iss >> lValue >> std::dec;
	return lValue;
}




	
