package com.seovic.coherence.cachemanager;

import java.io.Serializable;

public class Country implements Serializable {

    String code;
    String name;
    String formalName;
    String capital;
    String currencySymbol;
    String currencyName;
    String telephonePrefix;
    String domain;

    public Country() {
    }

    public Country(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormalName() {
        return formalName;
    }

    public void setFormalName(String formalName) {
        this.formalName = formalName;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getTelephonePrefix() {
        return telephonePrefix;
    }

    public void setTelephonePrefix(String telephonePrefix) {
        this.telephonePrefix = telephonePrefix;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Country country = (Country) o;

        if (capital != null ? !capital.equals(country.capital) : country.capital != null) return false;
        if (!code.equals(country.code)) return false;
        if (currencyName != null ? !currencyName.equals(country.currencyName) : country.currencyName != null)
            return false;
        if (currencySymbol != null ? !currencySymbol.equals(country.currencySymbol) : country.currencySymbol != null)
            return false;
        if (domain != null ? !domain.equals(country.domain) : country.domain != null) return false;
        if (formalName != null ? !formalName.equals(country.formalName) : country.formalName != null) return false;
        if (!name.equals(country.name)) return false;
        if (telephonePrefix != null ? !telephonePrefix.equals(country.telephonePrefix) : country.telephonePrefix != null)
            return false;

        return true;
    }

    public int hashCode() {
        return code.hashCode();
    }

    public String toString() {
        return "Country(" +
                "Code = " + code + ", " +
                "Name = " + name + ", " +
                "FormalName = " + formalName + ", " +
                "Capital = " + capital + ", " +
                "CurrencySymbol = " + currencySymbol + ", " +
                "CurrencyName = " + currencyName + ", " +
                "TelephonePrefix = " + telephonePrefix + ", " +
                "Domain = " + domain + ")";
    }
}
