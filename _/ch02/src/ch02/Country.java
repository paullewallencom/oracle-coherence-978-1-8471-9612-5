package ch02;

import java.io.Serializable;

public class Country 
        implements Serializable, Comparable {

    private String code;
    private String name;
    private String capital;
    private String currencySymbol;
    private String currencyName;

    public Country() {
    }

    public Country(String code, String name, String capital,
                   String currencySymbol, String currencyName) {
        this.code           = code;
        this.name           = name;
        this.capital        = capital;
        this.currencySymbol = currencySymbol;
        this.currencyName   = currencyName;
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

    public String toString() {
        return "Country(" +
                "Code = " + code + ", " +
                "Name = " + name + ", " +
                "Capital = " + capital + ", " +
                "CurrencySymbol = " + currencySymbol + ", " +
                "CurrencyName = " + currencyName + ")";
    }

    public int compareTo(Object o) {
        Country other = (Country) o;
        return this.name.compareTo(other.name);
    }
}
