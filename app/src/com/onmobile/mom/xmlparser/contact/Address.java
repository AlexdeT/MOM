package com.onmobile.mom.xmlparser.contact;

import org.simpleframework.xml.Element;

import com.onmobile.mom.xmlparser.Mapping;

/**
 * This class use the simpleXML library to parse an XML. It contains all the
 * keys define for a Address, as the PIM return it.
 *
 * @author adetalouet
 */
@Element
public class Address {

    /**
     * The type of address
     */
    @Element(required = false)
    public String addressType;

    /**
     * The street name
     */
    @Element(required = false)
    public String street1;

    /**
     * The neig[...]hood name
     */
    @Element(required = false)
    public String extAdd;

    /**
     * The zip code
     */
    @Element(required = false)
    public String zipCode;

    /**
     * The city
     */
    @Element(required = false)
    public String city;

    /**
     * The state
     */
    @Element(required = false)
    public String state;

    /**
     * The country
     */
    @Element(required = false)
    public String country;

    @Override
    public String toString() {
        return "Address [Type=" + addressType + ", Street=" + street1 + ", ExtAdd=" + extAdd + ", ZIP=" + zipCode + ", City=" + city + ", State=" + state + ", Country=" + country + "]";
    }

    /**
     * Get the address type after {@link Mapping}
     *
     * @return the addressType
     */
    public String getAddressType() {
        return Mapping.PIMtoAndroid(addressType);
    }

    /**
     * Get the street name
     *
     * @return the street1
     */
    public String getStreet1() {
        return street1;
    }

    /**
     * Get the neig[...]hood
     *
     * @return the extAdd
     */
    public String getExtAdd() {
        return extAdd;
    }

    /**
     * Get the zip code
     *
     * @return the zipCode
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Get the city
     *
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Get the state
     *
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * Get the country
     *
     * @return the country
     */
    public String getCountry() {
        return country;
    }

}
