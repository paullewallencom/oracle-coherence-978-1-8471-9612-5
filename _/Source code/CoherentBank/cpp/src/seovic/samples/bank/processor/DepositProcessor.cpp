#include "DepositProcessor.hpp"
#include "PofConfig.hpp"

COH_REGISTER_PORTABLE_CLASS(POF_TYPE_DEPOSIT_PROCESSOR, DepositProcessor);


// ----- constructors -------------------------------------------------------

DepositProcessor::DepositProcessor()
    : m_vMoney(self()), m_vsDescription(self())
    {
    }

DepositProcessor::DepositProcessor(Managed<Money>::View vMoney, String::View vsDescription)
    : m_vMoney(self(), vMoney), m_vsDescription(self(), vsDescription)
    {
    }


// ----- InvocableMap::EntryProcessor interface -----------------------------

Object::Holder DepositProcessor::process(
        InvocableMap::Entry::Handle hEntry) const
    {
	COH_THROW (UnsupportedOperationException::create());
    }


// ----- PortableObject interface -------------------------------------------

void DepositProcessor::readExternal(PofReader::Handle hIn)
    {
    initialize(m_vMoney, cast<Managed<Money>::View>(hIn->readObject(0))); 
	initialize(m_vsDescription, hIn->readString(1)); 
	}

void DepositProcessor::writeExternal(PofWriter::Handle hOut) const
    {
    hOut->writeObject(0, m_vMoney);
	hOut->writeString(1, m_vsDescription);
    }


