#ifndef BANK_STRING_HELPER_HPP
#define BANK_STRING_HELPER_HPP

#include <string>
#include <vector>

class StringHelper
    {
    // ----- static helper methods ------------------------------------------

    public:
  
		static std::vector<std::string> split(const std::string& s, 
									const std::string& delim, 
									bool keep_empty = true);

		static bool isNumber(const std::string& s);
    };


#endif // BANK_STRING_HELPER_HPP
