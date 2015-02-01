package com.onmobile.mom.xmlparser.sms;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * This class use the simple XML library in order to parse the retrieved XML.
 * It defines the root element of the XML.
 *
 * @author adetalouet
 */
@Root
public class ListSMS {

    /**
     * The sms list
     */
    @ElementList
    public List<Message> collection;

    /**
     * Get the sms list
     *
     * @return the sms list
     */
    public List<Message> getSMSList() {
        return collection;
    }

    @Override
    public String toString() {
        return "SMS [" + collection + "]";
    }
}
