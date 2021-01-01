#ifndef BANK_WITHDRAW_PROCESSOR_HPP
#define BANK_WITHDRAW_PROCESSOR_HPP

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
* UpdaterProcessor is an EntryProcessor implementations that updates an
* attribute of an object cached in an InvocableMap.
*
* @author tb  2008.04.28
*/
class WithdrawProcessor
    : public class_spec<WithdrawProcessor,
        extends<AbstractProcessor>,
        implements<PortableObject> >
    {
    friend class factory<WithdrawProcessor>;

    // ----- constructors ---------------------------------------------------

    protected:
        /**
        * Construct a WithdrawProcessor.
        */
        WithdrawProcessor();

        /**
        * Construct an WithdrawProcessor. 
        *
        * @param  amount to withdraw
        */
        WithdrawProcessor(Managed<Money>::View vMoney, String::View vsDescription);


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
        * The amount to withdraw.
        */
        FinalView<Managed<Money> > m_vMoney;
		FinalView<String>          m_vsDescription;
    };

#endif 
