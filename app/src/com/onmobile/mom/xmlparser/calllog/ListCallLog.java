package com.onmobile.mom.xmlparser.calllog;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * This class use the simple XML library in order to parse the retrieved XML.
 * It defines the root element of the call log XML.
 *
 * @author adetalouet
 */
@Root
public class ListCallLog {

    /**
     * The call log list
     */
    @ElementList
    public List<CallLog> collection;

    /**
     * Get the call log list
     *
     * @return the call log list
     */
    public List<CallLog> getCallLogs() {
        return collection;
    }

    @Override
    public String toString() {
        return "CallLog [" + collection + "]";
    }

}
