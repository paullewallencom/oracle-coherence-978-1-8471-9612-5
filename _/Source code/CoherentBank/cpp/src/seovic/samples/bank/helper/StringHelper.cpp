#include "StringHelper.hpp"

#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include <iterator>

using namespace std;


// ----- StringHelper interface ---------------------------------------------

vector<string> StringHelper::split(const string& s, const string& delim, const bool keep_empty) 
	{
    vector<string> result;
    if (delim.empty()) 
		{
        result.push_back(s);
        return result;
		}
    string::const_iterator substart = s.begin(), subend;
    while (true) 
		{
        subend = search(substart, s.end(), delim.begin(), delim.end());
        string temp(substart, subend);
        if (keep_empty || !temp.empty()) 
			{
            result.push_back(temp);
			}
        if (subend == s.end()) 
			{
            break;
			}
        substart = subend + delim.size();
		}
    return result;
	}

bool StringHelper::isNumber(const string& s)
	{
	int iLength = s.length();
	if (iLength == 0) 
		{
		return false;
		}
	for (int i = 0; i < iLength; i++) 
		{
		if (!isdigit(s[i]))
			{
			return false;
			}
		}
	return true;
   }





	
