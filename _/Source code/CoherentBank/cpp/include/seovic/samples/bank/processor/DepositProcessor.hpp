#ifndef BANK_DEPOSIT_PROCESSOR_HPP
#define BANK_DEPOSIT_PROCESSOR_HPP

#include "coherence/lang.ns"

#include "coherence/io/pof/PofReader.hpp"
#include "coherence/io/pof/PofWriter.hpp"
#include "coherence/io/pof/PortableObject.hpp"
#include "coherence/util/InvocableMap.hpp"
#include "coherence/util/processor/AbstractProcessor.hpp"

#include "Money.hpp"

using namespace coherence::lang;

using coherence::io::pof::PofReader;
using coherence::io::pof::PofWriter;
using coherence::io::pof::PortableObject;
using coherence::util::InvocableMap;
using coherence::util::processor::AbstractProcessor;


/**
* 
*/
class DepositProcessor
    : public class_spec<DepositProcessor,
        extends<AbstractProcessor>,
        implements<PortableObject> >
    {
    friend class factory<DepositProcessor>;

    // ----- constructors ---------------------------------------------------

    protected:
        /**
        * Construct a DepositProcessor.
        */
        DepositProcessor();

        /**
        * Construct an DepositProcessor.
        *
        * @param hMoney amount to deposit
        */
        DepositProcessor(Managed<Money>::View vMoney, String::View vsDescription);


    // ----- InvocableMap::EntryProcessor interface -------------------------

    public:
        /**
        * {@inheritDoc}
        */
        virtual Object::Holder process(InvocableMap::Entry::Handle hEntry) const;


    // ----- PortableObject interface ---------------------------------------

    public:
        /**
        * {@inheritDoc}
        */
        virtual void readExternal(PofReader::Handle hIn);

        /**
        * {@inheritDoc}
        */
        virtual void writeExternal(PofWriter::Handle hOut) const;


    // ----- data members ---------------------------------------------------

    protected:
        /**
        * The amount to deposit.
        */
        FinalView<Managed<Money> > m_vMoney;
		FinalView<String>          m_vsDescription;
    };


#endif 
