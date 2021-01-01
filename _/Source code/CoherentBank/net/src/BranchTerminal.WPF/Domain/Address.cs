using System;
using Tangosol.IO.Pof;

namespace Seovic.Samples.Bank.Domain
{
    public class Address : IPortableObject
    {
        #region Constructor
        
        public Address()
            : this(string.Empty, string.Empty, string.Empty, string.Empty, string.Empty)
        { }

        public Address(String street, String city, String state, String postalCode, String country)
        {
            this.street     = street     ?? string.Empty;
            this.city       = city       ?? string.Empty;
            this.state      = state      ?? string.Empty;
            this.postalCode = postalCode ?? string.Empty;
            this.country    = country    ?? string.Empty;
        }

        #endregion

        #region Properties

        public string Street
        {
            get { return street; }
        }

        public string City
        {
            get { return city; }
        }

        public string State
        {
            get { return state; }
        }

        public string PostalCode
        {
            get { return postalCode; }
        }

        public string Country
        {
            get { return country; }
        }

        #endregion

        #region IPortableObject implementation

        public void ReadExternal(IPofReader reader)
        {
            street = reader.ReadString(0);
            city = reader.ReadString(1);
            state = reader.ReadString(2);
            postalCode = reader.ReadString(3);
            country = reader.ReadString(4);
        }

        public void WriteExternal(IPofWriter writer)
        {
            writer.WriteString(0, street);
            writer.WriteString(1, city);
            writer.WriteString(2, state);
            writer.WriteString(3, postalCode);
            writer.WriteString(4, country);
        }

        #endregion

        #region Object methods overrides

        public bool Equals(Address obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            return Equals(obj.street, street) && 
                Equals(obj.city, city) && 
                Equals(obj.state, state) && 
                Equals(obj.postalCode, postalCode) && 
                Equals(obj.country, country);
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != typeof (Address)) return false;
            return Equals((Address) obj);
        }

        public override int GetHashCode()
        {
            unchecked
            {
                int result = street.GetHashCode();
                result = (result*397) ^ city.GetHashCode();
                result = (result*397) ^ state.GetHashCode();
                result = (result*397) ^ postalCode.GetHashCode();
                result = (result*397) ^ country.GetHashCode();
                return result;
            }
        }

        public override string ToString()
        {
            return string.Format("Address{{street='{0}'" +
               ", city='{1}'" +
               ", state='{2}'" +
               ", postalCode='{3}'" +
               ", country='{4}'}}", street, city, state, postalCode, country);
        }

        #endregion

        #region Data Members

        private string street;
        private string city;
        private string state;
        private string postalCode;
        private string country;

        #endregion
    }
}