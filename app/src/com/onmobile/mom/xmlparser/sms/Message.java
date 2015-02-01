package com.onmobile.mom.xmlparser.sms;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;

import com.onmobile.mom.xmlparser.Mapping;

/**
 * This class use the simple XML library in order to parse the retrieved XML. It
 * defines all the keys needed to parse the SMS into the XML.
 *
 * @author adetalouet
 */
@Element
public class Message {

    /**
     * Defines the SMS folder (inbox, outbox)
     */
    @Element
    public String folder;

    /**
     * Defines the mobile number associated with the SMS
     */
    @Path("correspondent")
    @Element
    public String msisdn;

    /**
     * Defines the date when the SMS was received
     */
    @Element
    public String received;

    /**
     * Defines the content of the SMS
     */
    @Path("content/part")
    @Element
    public String text;

    /**
     * Defines whether the SMS is in the trash
     */
    @Element
    public String inTrash;

    @Override
    public String toString() {

        return "Message [" + "Folder=" + folder + ", Msisdn=" + msisdn + ", Received=" + received + ", Text=" + text + ", In Trash=" + inTrash + "]";
    }

    /**
     * Get the SMS folder. Instead of giving the PIM key, it will give the
     * Android key to make the check. {@link Mapping}.
     *
     * @return the Android Sms folder key
     */
    public String getFolder() {
        return Mapping.PIMtoAndroid(folder);
    }
}
