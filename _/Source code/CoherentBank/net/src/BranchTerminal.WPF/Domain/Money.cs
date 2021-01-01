using System;
using Tangosol.IO.Pof;

namespace Seovic.Samples.Bank.Domain
{
    [Serializable]
    public class Money : IPortableObject
    {
        #region Properties

        public decimal Amount
        {
            get { return m_amount; }
        }

        public Currency Currency
        {
            get { return m_currency; }
        }

        #endregion

        #region Contructors and Factory Methods

        public Money()
            : this(0, "RSD")
        { }

        public Money(String amount, String currencyCode)
            : this(decimal.Parse(amount),  new Currency(currencyCode))
        { }

        public Money(String amount, Currency currency) : 
            this(decimal.Parse(amount), currency)
        { }

        public Money(int amount, String currencyCode)
            : this(new decimal(amount), new Currency(currencyCode))
        { }

        public Money(int amount, Currency currency)
            : this(new decimal(amount), currency)
        { }

        public Money(long amount, String currencyCode) : 
            this(new decimal(amount), new Currency(currencyCode))
        { }

        public Money(long amount, Currency currency) : 
            this(new decimal(amount), currency)
        { }

        public Money(double amount, String currencyCode)
            : this(new decimal(amount), new Currency(currencyCode))
        { }

        public Money(double amount, Currency currency)
            : this(new decimal(amount), currency)
        { }

        public Money(decimal amount, String currencyCode)
            : this(amount, new Currency(currencyCode))
        { }

        public Money(decimal amount, Currency currency)
        {
            m_amount   = decimal.Round(amount, currency.GetDefaultFractionDigits(), MidpointRounding.ToEven);
            m_amount   = amount;
            m_currency = currency;
        }

        #endregion

        #region Public Methods

        public bool IsSameCurrency(Money money)
        {
            return m_currency.Equals(money.m_currency);
        }

        public Money Add(Money money)
        {
            CheckCurrency(money);
            return new Money(m_amount + money.m_amount, m_currency);
        }

        public Money Subtract(Money money)
        {
            CheckCurrency(money);
            return new Money(m_amount - money.m_amount, m_currency);
        }

        public bool GreaterThan(Money money)
        {
            CheckCurrency(money);
            return m_amount > money.m_amount;
        }

        public bool LessThan(Money money)
        {
            CheckCurrency(money);
            return m_amount < money.m_amount;
        }

        #endregion

        #region Operators

        public static Money operator +(Money left, Money right)
        {
            return left.Add(right);
        }

        public static Money operator -(Money left, Money right)
        {
            return left.Subtract(right);
        }

        public static bool operator >(Money left, Money right)
        {
            return left.GreaterThan(right);
        }

        public static bool operator <(Money left, Money right)
        {
            return left.LessThan(right);
        }

        public static bool operator ==(Money left, Money right)
        {
            return Equals(left, right);
        }

        public static bool operator !=(Money left, Money right)
        {
            return !Equals(left, right);
        }

        #endregion

        #region Helper Methods

        protected void CheckCurrency(Money money)
        {
            if (!IsSameCurrency(money))
            {
                throw new ArgumentException("Currencies are not the same");
            }
        }

        #endregion

        #region IPortableObject implementation

        public void ReadExternal(IPofReader reader)
        {
            long unscaledValue = reader.ReadInt64(0);
            int scale          = reader.ReadInt32(1);
            m_amount             = (decimal)(unscaledValue*Math.Pow(10, (-1)*scale));
            m_currency           = (Currency)reader.ReadObject(2);
        }

        public void WriteExternal(IPofWriter writer)
        {
            int[] bits = decimal.GetBits(m_amount);
  
            int sign = ((bits[3] >> 31) & 0x1) == 1 ? -1 : 1;
            // the same as: 
            // int sign = (int)Math.Pow(-1,  ((bits[3] >> 31) & 0x1));
            int exponent = (bits[3] & 0x00ff0000) >> 16;
            // long is too small to hold all three bytes.
            long mantisa = (uint) bits[0] + (((long) (uint) bits[1]) << 32);// +(((long)(uint)bits[2]) << 64);

            writer.WriteInt64(0, sign * mantisa);
            writer.WriteInt32(1, exponent);
            writer.WriteObject(2, m_currency);
        } 

        #endregion

        #region Object methods overrides

        public bool Equals(Money obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            return obj.m_amount == m_amount && Equals(obj.m_currency, m_currency);
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != typeof (Money)) return false;
            return Equals((Money) obj);
        }

        public override int GetHashCode()
        {
            unchecked
            {
                return (m_amount.GetHashCode()*397) ^ (m_currency != null ? m_currency.GetHashCode() : 0);
            }
        }

        public override string ToString()
        {
            return m_currency.CurrencyCode + " " + m_amount;
        }

        #endregion

        #region Data Members

        private decimal  m_amount;
        private Currency m_currency;
        
        #endregion
    }
}