#include "WithdrawProcessor.hpp"
#include "PofConfig.hpp"

COH_REGISTER_PORTABLE_CLASS(POF_TYPE_WITHDRAW_PROCESSOR, WithdrawProcessor);


// ----- constructors -------------------------------------------------------

WithdrawProcessor::WithdrawProcessor()
    : m_vMoney(self()), m_vsDescription(self())
    {
    }

WithdrawProcessor::WithdrawProcessor(Managed<Money>::View vMoney, String::View vsDescription)
    : m_vMoney(self(), vMoney), m_vsDescription(self(), vsDescription)
    {
    }


// ----- InvocableMap::EntryProcessor interface -----------------------------

Object::Holder WithdrawProcessor::process(
        InvocableMap::Entry::Handle hEntry) const
    {
	COH_THROW (UnsupportedOperationException::create());
    }


// ----- PortableObject interface -------------------------------------------

void WithdrawProcessor::readExternal(PofReader::Handle hIn)
    {
    initialize(m_vMoney, cast<Managed<Money>::View>(hIn->readObject(0))); 
	initialize(m_vsDescription, hIn->readString(1)); 
    }

void WithdrawProcessor::writeExternal(PofWriter::Handle hOut) const
    {
    hOut->writeObject(0, m_vMoney);
	hOut->writeString(1, m_vsDescription);
    }

