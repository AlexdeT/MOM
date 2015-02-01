package com.onmobile.mom.xmlparser.calllog;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;

import com.onmobile.mom.xmlparser.Mapping;

/**
 * This class use the simple XML library in order to parse the retrieved XML. It
 * parses the call log content.
 *
 * @author adetalouet
 */
@Element
public class CallLog {

    /**
     * Mobile number
     */
    @Path("Correspondent")
    @Element
    public String msisdn;

    /**
     * Defines if it is a incoming, outgoing, or missed call
     */
    @Element
    public String direction;

    /**
     * Defines the beginning of the call
     */
    @Element
    public String begin;

    /**
     * Defines the duration of the call
     */
    @Element
    public String duration;

    @Override
    public String toString() {

        return "Call log [" + ", Msisdn=" + msisdn + ", Direction=" + direction + ", Begin=" + begin + ", Duration= " + duration + "]";
    }

    /**
     * Get the map phone call direction. Instead of giving the PIM key, it will
     * give the Android key to make the check. {@link Mapping}.
     *
     * @return the corresponding call direction
     */
    public String getDirection() {
        return Mapping.PIMtoAndroid(direction);
    }
}
