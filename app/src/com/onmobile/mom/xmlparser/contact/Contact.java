package com.onmobile.mom.xmlparser.contact;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

/**
 * This class use the simpleXML library to parse an XML. It contains all the
 * keys define for a Contact, as the PIM return it.
 *
 * @author adetalouet
 */
@Element
public class Contact {

    /**
     * The list of devices
     */
    @ElementList(required = false)
    public List<Device> devices;

    /**
     * The list of the addresses
     */
    @ElementList(required = false)
    public List<Address> addresses;

    /**
     * The contact firstname
     */
    @Element(required = false)
    public String firstname;

    /**
     * The contact nickname
     */
    @Element(required = false)
    public String nickname;

    /**
     * The contact function in his company
     */
    @Element(required = false)
    public String function;

    /**
     * The contact company
     */
    @Element(required = false)
    public String company;

    /**
     * The contact comments
     */
    @Element(required = false)
    public String comments;

    @Override
    public String toString() {
        return "Contact [Firstname=" + firstname + ", NickName=" + nickname + ", Function=" + function + ", Company=" + company + ", Comments=" + comments + ", Addresses=" + addresses + ", Numbers=" + devices + "]";
    }

    /**
     * Get the list of devices
     *
     * @return the devices
     */
    public List<Device> getDevices() {
        return devices;
    }

    /**
     * Get the list of addresses
     *
     * @return the addresses
     */
    public List<Address> getAddresses() {
        return addresses;
    }

    /**
     * Get the firstname
     *
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Get the nickname
     *
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Get the function
     *
     * @return the function
     */
    public String getFunction() {
        return function;
    }

    /**
     * Get the company
     *
     * @return the company
     */
    public String getCompany() {
        return company;
    }

    /**
     * Get the comments
     *
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

}
