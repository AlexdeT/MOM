package com.onmobile.mom.xmlparser.contact;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * This class use the simpleXML library to parse an XML. It contains the contact
 * list.
 *
 * @author adetalouet
 */
@Root
public class AddressBook {

    /**
     * The contact list
     */
    @ElementList
    public List<Contact> collection;

    /**
     * Get the contact list
     *
     * @return the contact list
     */
    public List<Contact> getContact() {
        return collection;
    }

    @Override
    public String toString() {
        return "Contacts [" + collection + "]";
    }
}
