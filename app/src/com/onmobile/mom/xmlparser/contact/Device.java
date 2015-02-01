package com.onmobile.mom.xmlparser.contact;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import com.onmobile.mom.xmlparser.Mapping;

/**
 * This class use the simpleXML library to parse an XML. It contains all the
 * keys define for a Device, as the PIM return it.
 *
 * @author adetalouet
 */
@Element
public class Device {

    /**
     * Number. Can either be a phone, IM or email number.
     */
    @Element
    public String value;

    /**
     * Type. Can either be Work, Home, Other or nothing.
     */
    @Attribute
    public String deviceType;

    /**
     * Sub type. Can either be Mobile, Fax, Pager, Voice, ... ; AIM, ICQ, Yahoo,
     * Skype, ...
     */
    @Attribute
    public String subType;

    /**
     * Get the device number
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the device type after {@link Mapping}.
     *
     * @return the deviceType
     */
    public String getDeviceType() {
        return Mapping.PIMtoAndroid(deviceType);
    }

    /**
     * Get the device sub type after {@link Mapping}.
     *
     * @return the subType
     */
    public String getSubType() {
        return Mapping.PIMtoAndroid(subType);
    }

    @Override
    public String toString() {
        return "Address [Number=" + value + ", deviceType=" + deviceType + ", subType=" + subType + "]";
    }

}
