package com.seovic.samples.bank.domain;


import com.seovic.coherence.io.pof.AbstractPofSerializer;

import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PofReader;

import java.io.Serializable;
import java.io.IOException;


/**
 * @author Aleksandar Seovic  2009.11.29
 */
public class Address
        implements Serializable
    {
    // ---- constructors ----------------------------------------------------

    public Address()
        {
        this("", "", "", "", "");
        }

    public Address(String street, String city, String state, String postalCode, String country)
        {
        m_street     = street == null ? "" : street;
        m_city       = city == null ? "" : city;
        m_state      = state == null ? "" : state;
        m_postalCode = postalCode == null ? "" : postalCode;
        m_country    = country == null ? "" : country;
        }


    // ---- properties ------------------------------------------------------

    public String getStreet()
        {
        return m_street;
        }

    public void setStreet(String street)
        {
        m_street = street;
        }

    public String getCity()
        {
        return m_city;
        }

    public void setCity(String city)
        {
        m_city = city;
        }

    public String getState()
        {
        return m_state;
        }

    public void setState(String state)
        {
        m_state = state;
        }

    public String getPostalCode()
        {
        return m_postalCode;
        }

    public void setPostalCode(String postalCode)
        {
        m_postalCode = postalCode;
        }

    public String getCountry()
        {
        return m_country;
        }

    public void setCountry(String country)
        {
        m_country = country;
        }


    // ---- Obect methods ---------------------------------------------------

    public boolean equals(Object o)
        {
        if (this == o)
            {
            return true;
            }
        if (o == null || getClass() != o.getClass())
            {
            return false;
            }

        Address address = (Address) o;

        return m_country.equals(address.m_country)
               && m_state.equals(address.m_state)
               && m_postalCode.equals(address.m_postalCode)
               && m_city.equals(address.m_city)
               && m_street.equals(address.m_street);
        }

    public int hashCode()
        {
        int result = m_street.hashCode();
        result = 31 * result + m_city.hashCode();
        result = 31 * result + m_state.hashCode();
        result = 31 * result + m_postalCode.hashCode();
        result = 31 * result + m_country.hashCode();
        return result;
        }

    public String toString()
        {
        return "Address{" +
               "street='" + m_street + '\'' +
               ", city='" + m_city + '\'' +
               ", state='" + m_state + '\'' +
               ", postalCode='" + m_postalCode + '\'' +
               ", country='" + m_country + '\'' +
               '}';
        }


    // ---- inner class: Serializer -----------------------------------------

    public static class Serializer extends AbstractPofSerializer<Address>
        {
        protected void serializeAttributes(Address address, PofWriter writer)
                throws IOException
            {
            writer.writeString(0, address.m_street);
            writer.writeString(1, address.m_city);
            writer.writeString(2, address.m_state);
            writer.writeString(3, address.m_postalCode);
            writer.writeString(4, address.m_country);
            }

        protected Address createInstance(PofReader reader)
                throws IOException
            {
            return new Address(
                    reader.readString(0),
                    reader.readString(1),
                    reader.readString(2),
                    reader.readString(3),
                    reader.readString(4));
            }
        }

    
    // ---- data members ----------------------------------------------------

    private String m_street;
    private String m_city;
    private String m_state;
    private String m_postalCode;
    private String m_country;
    }
