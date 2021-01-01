using Tangosol.IO.Pof;

namespace Seovic.Samples.Bank.Domain
{
    public class Currency : IPortableObject 
    {
        #region Constructors

        public Currency()
        {
        }

        public Currency(string currencyCode)
        {
            this.currencyCode = currencyCode;
        }

        #endregion

        #region Properties

        public string CurrencyCode
        {
            get { return currencyCode; }
            set { currencyCode = value; }
        }

        #endregion

        #region Public Methods
        public int GetDefaultFractionDigits()
        {
            return DEFAULT_FRACTION_DIGITS;
        }
        #endregion

        #region IPortableObject implementation

        public void WriteExternal(IPofWriter writer)
        {
            writer.WriteString(0, currencyCode);
        }

        public void ReadExternal(IPofReader reader)
        {
            currencyCode = reader.ReadString(0);
        }

        #endregion

        #region Object methods overrides

        public bool Equals(Currency other)
        {
            if (ReferenceEquals(null, other)) return false;
            if (ReferenceEquals(this, other)) return true;
            return Equals(other.currencyCode, currencyCode);
        }

        /// <summary>
        /// Determines whether the specified object is equal to the current 
        /// object.
        /// </summary>
        /// <returns>
        /// true if the specified object is equal to the current object;
        /// otherwise, false.
        /// </returns>
        /// <param name="obj">The object to compare with the current object.</param>
        /// <exception cref="T:System.NullReferenceException">
        /// The <paramref name="obj"/> parameter is null.
        /// </exception>
        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != typeof (Currency)) return false;
            return Equals((Currency) obj);
        }

        /// <summary>
        /// Serves as a hash function for a particular type. 
        /// </summary>
        /// <returns>
        /// A hash code for the current <see cref="T:System.Object"/>.
        /// </returns>
        /// <filterpriority>2</filterpriority>
        public override int GetHashCode()
        {
            return currencyCode.GetHashCode();
        }

        /// <summary>
        /// Returns a string representation of the current object.
        /// </summary>
        /// <returns>
        /// A string that represents the current object.
        /// </returns>
        public override string ToString()
        {
            return string.Format("CurrencyCode: {0}", currencyCode);
        }

        #endregion

        #region Data members

        private const int DEFAULT_FRACTION_DIGITS = 2;
        private string currencyCode;

        #endregion
    }
}
