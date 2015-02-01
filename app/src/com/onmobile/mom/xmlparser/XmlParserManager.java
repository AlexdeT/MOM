package com.onmobile.mom.xmlparser;

import java.io.Reader;
import java.io.StringReader;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.util.Log;

import com.onmobile.mom.app.Config;
import com.onmobile.mom.httprequest.PIMRequest;
import com.onmobile.mom.xmlparser.calllog.ListCallLog;
import com.onmobile.mom.xmlparser.contact.AddressBook;
import com.onmobile.mom.xmlparser.sms.ListSMS;

/**
 * This class use the simpleXML library to parse XMLs. It contains all the
 * parsed data.
 *
 * @author adetalouet
 */
public abstract class XmlParserManager {

    /**
     * Tag to debug
     */
    private static final String TAG = "XmlParserManager - ";

    /**
     * Address book as define in the xml
     */
    private static AddressBook mAddressBook;
    /**
     * SMS list as define in the xml
     */
    private static ListSMS mSMSList;
    /**
     * Call log list as define in the xml
     */
    private static ListCallLog mCallLogList;

    /**
     * Key used to specify that we want to parse the contact
     */
    public static final String TYPE_CONTACT = "Contact";
    /**
     * Key used to specify that we want to parse the call log
     */
    public static final String TYPE_CALL_LOG = "CallLog";
    /**
     * Key used to specify that we want to parse the sms
     */
    public static final String TYPE_SMS = "SMS";

    /**
     * This function parse the list XML retrieved from the web. </br></br>
     * <p/>
     * I surrounded the retrieved XML with {@code <AddressBook> </AddressBook>}
     * in order to have a root element. In fact the XML retrieved provide a
     * collection of Contact following this pattern :
     * {@code <collection> <Contact></Contact> <Contact></Contact> <Contact></Contact> </collection>}
     * , but with simpleXML we cannot parse at this level, we need to have a
     * upper level in order to declare the list of Contact named collection.
     * </br>Same behavior for the others data set. </br></br>
     * <p/>
     * TODO This workaround can be set properly with a wrapper
     *
     * @param list - the PIM list returned by the httpRequest. (
     *             {@link PIMRequest})
     * @param type - the type of data to parse
     */
    public static void parseListXML(String list, String type) {

        Log.d(Config.TAG_APP, TAG + "parse" + type + "XML");

        String xmlData = null;

        try {
            if (type.equals(TYPE_CALL_LOG))
                xmlData = "<ListCallLog>" + list + "</ListCallLog>";
            if (type.equals(TYPE_SMS))
                xmlData = "<ListSMS>" + list + "</ListSMS>";
            if (type.equals(TYPE_CONTACT))
                xmlData = "<AddressBook>" + list + "</AddressBook>";

            Serializer serializer = new Persister();
            Reader reader = new StringReader(xmlData);

            if (type.equals(TYPE_CALL_LOG)) {
                mCallLogList = serializer
                        .read(ListCallLog.class, reader, false);
//				Log.d(Config.TAG_APP, TAG + mCallLogList.toString());
            }
            if (type.equals(TYPE_SMS)) {
                mSMSList = serializer.read(ListSMS.class, reader, false);
//				Log.d(Config.TAG_APP, TAG + mSMSList.toString());
            }
            if (type.equals(TYPE_CONTACT)) {
                mAddressBook = serializer
                        .read(AddressBook.class, reader, false);
//				Log.d(Config.TAG_APP, TAG + mAddressBook.toString());
            }


        } catch (Exception e) {
            Log.e(Config.TAG_APP, TAG + "parse" + type + "XML : " + e);
            e.printStackTrace();
        }
    }

    /**
     * Get the address book
     *
     * @return the address book
     */
    public static AddressBook getAddressBook() {
        return mAddressBook;
    }

    /**
     * Get the SMS list
     *
     * @return the mSMSList
     */
    public static ListSMS getSMSdataSet() {
        return mSMSList;
    }

    /**
     * Get the call log list
     *
     * @return the mCallLogList
     */
    public static ListCallLog getCallLogDataSet() {
        return mCallLogList;
    }

}
