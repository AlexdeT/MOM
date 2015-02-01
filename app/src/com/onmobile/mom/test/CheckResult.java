package com.onmobile.mom.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;

import com.onmobile.mom.app.Config;
import com.onmobile.mom.database.DatabaseManager;
import com.onmobile.mom.database.MySMS;
import com.onmobile.mom.httprequest.PIMRequest;
import com.onmobile.mom.xmlparser.XmlParserManager;
import com.onmobile.mom.xmlparser.contact.Address;
import com.onmobile.mom.xmlparser.contact.Contact;
import com.onmobile.mom.xmlparser.contact.Device;
import com.onmobile.mom.xmlparser.sms.Message;

/**
 * This class makes the ckeckResult at the end of a synchronization
 *
 * @author adetalouet
 */

public abstract class CheckResult {

    /**
     * Tag to debug
     */
    private static final String TAG = "CheckResult - ";

    /**
     * Use to make the httpRequest to get the summary from the PIM
     */
    private static PIMRequest mSummary;

    /**
     * This function checks the results after a backup. It will get the PIM info
     * and compare them with the values in the {@link Config} class.
     *
     * @param isAfterUpdate - specify if the function is call after databases' update
     * @param context       - the test case context
     */
    public static void checkBackupResult(boolean isAfterUpdate, Context context) {

        mSummary = new PIMRequest();

        Log.d(Config.TAG_APP, TAG
                + "----- checkBackupResult -----");
        Log.d(Config.TAG_APP, TAG
                + "              PIM " + " | " + "DEVICE    ");
        if (Config.SYNC_CALL_LOG) { // TODO change the condition once the backup of the call log is corrected. In backup, the deleted event on the phone are not deleted on the PIM...
            Test.myAssert("Call Log - Backup - Device - PIM", (isAfterUpdate ? Config.NUMBER_OF_CALL_LOG_FINAL : Config.NUMBER_OF_CALL_LOG), mSummary.getCallLogNb());
            Log.d(Config.TAG_APP, TAG
                    + "Call log Nb : " + mSummary.getCallLogNb() + "    |    " + (isAfterUpdate ? Config.NUMBER_OF_CALL_LOG_FINAL : Config.NUMBER_OF_CALL_LOG));
        }
        if (Config.SYNC_CONTACT) {
            Test.myAssert("Contact - Backup - Device - PIM", (isAfterUpdate ? Config.NUMBER_OF_CONTACT_FINAL : Config.NUMBER_OF_CONTACT), mSummary.getContactNb());
            Log.d(Config.TAG_APP, TAG
                    + "Contact Nb  : " + mSummary.getContactNb() + "    |    " + (isAfterUpdate ? Config.NUMBER_OF_CONTACT_FINAL : Config.NUMBER_OF_CONTACT));
        }
        if (Config.SYNC_SMS) {
            Test.myAssert("SMS - Backup - Device - PIM", (isAfterUpdate ? Config.NUMBER_OF_SMS_FINAL : Config.NUMBER_OF_SMS), mSummary.getSmsNb());
            Log.d(Config.TAG_APP, TAG
                    + "Sms Nb      : " + mSummary.getSmsNb() + "    |    " + (isAfterUpdate ? Config.NUMBER_OF_SMS_FINAL : Config.NUMBER_OF_SMS));
        }
        if (Config.SYNC_PHOTO) {
            Test.myAssert("Image - Backup - Device - PIM", Config.NUMBER_OF_PHOTO, mSummary.getImageNb());
            Log.d(Config.TAG_APP, TAG
                    + "Image Nb    : " + mSummary.getImageNb() + "    |    " + Config.NUMBER_OF_PHOTO);
        }
        if (Config.SYNC_VIDEO) {
            Test.myAssert("Video - Backup - Device - PIM", Config.NUMBER_OF_VIDEO, mSummary.getVideoNb());
            Log.d(Config.TAG_APP, TAG
                    + "Video Nb    : " + mSummary.getVideoNb() + "    |    " + Config.NUMBER_OF_VIDEO);
        }
        if (Config.SYNC_EVENT) {
            Test.myAssert("Event - Backup - Device - PIM", Config.NUMBER_OF_EVENT, mSummary.getEventNb());
            Log.d(Config.TAG_APP, TAG
                    + "Event Nb    : " + mSummary.getEventNb() + "    |    " + Config.NUMBER_OF_EVENT);
        }


        checkResultV2(context);

        Log.d(Config.TAG_APP, TAG
                + "----- checkBackupResult : OK -----");
    }

    /**
     * This function checks the results after a restore. It will get the Android
     * native databases count and compare them with the values present on the
     * PIM.
     *
     * @param context - the test case context
     */
    public static void checkRestoreResult(Context context) {

        mSummary = new PIMRequest();

        Log.d(Config.TAG_APP, TAG
                + "------- checkRestoreResult -------");
        Log.d(Config.TAG_APP, TAG
                + "              PIM " + " | " + "DEVICE    ");
        if (Config.SYNC_CALL_LOG) {
            Test.myAssert("Call Log - Restore - PIM - Device", mSummary.getCallLogNb(), DatabaseManager.getCallLogDataSet().getCount());
            Log.d(Config.TAG_APP, TAG
                    + "Call log Nb : " + mSummary.getCallLogNb() + "    |    " + DatabaseManager.getCallLogDataSet().getCount());
        }
        if (Config.SYNC_CONTACT) {
            Test.myAssert("Contact - Restore - PIM - Device", mSummary.getContactNb(), DatabaseManager.getContactDataSet().getCount());
            Log.d(Config.TAG_APP, TAG
                    + "Contact Nb  : " + mSummary.getContactNb() + "    |    " + DatabaseManager.getContactDataSet().getCount());
        }
//		if (Config.SYNC_SMS) {
//			Test.myAssert("SMS - Restore - PIM - Device", mSummary.getSmsNb(),  DatabaseManager.getSmsDataSet().getCount());
//			Log.d(Config.TAG_APP, TAG 
//					+ "Sms Nb      : " + mSummary.getSmsNb() + "    |    " +  DatabaseManager.getSmsDataSet().getCount());
//		}
        if (Config.SYNC_PHOTO) {
            Test.myAssert("Image - Restore - PIM - Device", mSummary.getImageNb(), DatabaseManager.getPhotoDataSet().getCount());
            Log.d(Config.TAG_APP, TAG
                    + "Image Nb    : " + mSummary.getImageNb() + "    |    " + DatabaseManager.getPhotoDataSet().getCount());
        }
        if (Config.SYNC_VIDEO) {
            Test.myAssert("Video - Restore - PIM - Device", mSummary.getVideoNb(), DatabaseManager.getVideoDataSet().getCount());
            Log.d(Config.TAG_APP, TAG
                    + "Video Nb    : " + mSummary.getVideoNb() + "    |    " + DatabaseManager.getVideoDataSet().getCount());
        }
        if (Config.SYNC_EVENT) {
            Test.myAssert("Event - Restore - PIM - Device", mSummary.getEventNb(), DatabaseManager.getEventDataSet().getCount());
            Log.d(Config.TAG_APP, TAG
                    + "Event Nb    : " + mSummary.getEventNb() + "    |    " + DatabaseManager.getEventDataSet().getCount());
        }

        checkResultV2(context);

        Log.d(Config.TAG_APP, TAG
                + "----- checkRestoreResult : OK -----");
    }

    /**
     * This function check the value one by one once the first verification has
     * been made and validated. For all the data set, it will get the device
     * values and compare them with the PIM values.
     *
     * @param context - the test case context
     */
    private static void checkResultV2(Context context) {

        if (Config.SYNC_CONTACT) {
            XmlParserManager.parseListXML(
                    PIMRequest.getListXML(Config.CONTACT_LIST_RESTAPI_URL, XmlParserManager.TYPE_CONTACT),
                    XmlParserManager.TYPE_CONTACT);

            CheckResult.getAndCheckDeviceContacts(context);
        }
        if (Config.SYNC_CALL_LOG) {
            XmlParserManager.parseListXML(
                    PIMRequest.getListXML(Config.CALL_LOG_LIST_RESTAPI_URL, XmlParserManager.TYPE_CALL_LOG),
                    XmlParserManager.TYPE_CALL_LOG);

            CheckResult.getAndCheckDeviceCallLog(context);
        }
        if (Config.SYNC_SMS) {
            XmlParserManager.parseListXML(
                    PIMRequest.getListXML(Config.SMS_LIST_RESTAPI_URL, XmlParserManager.TYPE_SMS),
                    XmlParserManager.TYPE_SMS);

            CheckResult.getAndCheckDeviceSMS(context);
        }
    }


    //---------------------------------- Contacts

    /**
     * This function goes through all the contacts contains in the device
     * Address Book. For each contact it gets its key, defined in the first 4
     * characters of each fields, from the name and compare it with the other
     * fields. If a field doesn't have the same key, the test stop.
     * <p/>
     * Then each contact is compare with the one contains in the contact list
     * retrieved from the PIM. {@code PIMRequest.getListXML()}
     *
     * @param context - the context of the test case
     */
    private static void getAndCheckDeviceContacts(Context context) {

        Log.d(Config.TAG_APP, TAG + "-- getAndCheckDeviceContacts ");

        // Value to store the contact key
        int contactKey = -1;

        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);

        // for all the contact in the device address book
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {

                // get the id
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                // get the name
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                // get the contact key
                contactKey = Integer.parseInt(name.substring(0, 3));

                // Names
                Cursor nameCursor = cr.query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + " = " + id + " AND ContactsContract.Data.MIMETYPE = '"
                        + ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                        + "'", null, null);

                while (nameCursor.moveToNext()) {
                    String famillyName = nameCursor.getString(nameCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
                    String prefix = nameCursor.getString(nameCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PREFIX));
                    String suffix = nameCursor.getString(nameCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.SUFFIX));
                    String middleName = nameCursor.getString(nameCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME));

                    if (famillyName != null)
                        Test.myAssert("Familly Name", contactKey, Integer.parseInt(famillyName.substring(0, 3)));

                    if (prefix != null)
                        Test.myAssert("Prefix", contactKey, Integer.parseInt(prefix.substring(0, 3)));

                    if (suffix != null)
                        Test.myAssert("Suffix", contactKey, Integer.parseInt(suffix.substring(0, 3)));

                    if (middleName != null)
                        Test.myAssert("Middle Name", contactKey, Integer.parseInt(middleName.substring(0, 3)));
                }
                nameCursor.close();

                // Phone numbers
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

                    Cursor phoneCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (phoneCursor.moveToNext()) {

                        String phoneNo = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        // TODO compare type once device dir is OK
                        //	String phoneType = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        //	CharSequence pht = ContactsContract.CommonDataKinds.Phone.getTypeLabel(context.getResources(), Integer.parseInt(phoneType), null);

                        Test.myAssert("Phone number", contactKey, Integer.parseInt(phoneNo.substring(0, 3)));


                    }
                    phoneCursor.close();
                }

                // Addresses
                Cursor addressCursor = cr.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, null, ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = ?", new String[]{id}, null);
                while (addressCursor.moveToNext()) {
                    String user_home_address = addressCursor.getString(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.DATA));

                    // TODO compare type once device dir is OK
                    // String user_address_type = addressCursor.getString(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
                    // CharSequence at = ContactsContract.CommonDataKinds.StructuredPostal.getTypeLabel(context.getResources(), Integer.parseInt(user_address_type), null);
                    // assertEquals(contactKey, Integer.parseInt(user_address_type.substring(0, 3)));

                    Test.myAssert("Address", contactKey, Integer.parseInt(user_home_address.substring(0, 3)));
                }
                addressCursor.close();

                //Website
                Cursor websiteNameCursor = cr.query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + " = " + id + " AND ContactsContract.Data.MIMETYPE = '"
                        + ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE
                        + "'", null, null);

                while (websiteNameCursor.moveToNext()) {
                    String user_website = (websiteNameCursor.getString(websiteNameCursor.getColumnIndex(ContactsContract.CommonDataKinds.Website.URL)));
                    String user_website_type = (websiteNameCursor.getString(websiteNameCursor.getColumnIndex(ContactsContract.CommonDataKinds.Website.TYPE)));

                    Test.myAssert("Website, " + user_website_type, contactKey, Integer.parseInt(user_website.substring(0, 3)));
                }
                websiteNameCursor.close();

                //Organization
                Cursor organizationNameCursor = cr.query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + " = " + id + " AND ContactsContract.Data.MIMETYPE = '"
                        + ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE
                        + "'", null, null);

                while (organizationNameCursor.moveToNext()) {
                    String user_company = organizationNameCursor.getString(organizationNameCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY));
                    String user_job_title = organizationNameCursor.getString(organizationNameCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));

                    Test.myAssert("Organization - company", contactKey, Integer.parseInt(user_company.substring(0, 3)));
                    Test.myAssert("Oragnization - job title", contactKey, Integer.parseInt(user_job_title.substring(0, 3)));
                }
                organizationNameCursor.close();

                //Email
                Cursor emailCursor = cr.query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + " = " + id + " AND ContactsContract.Data.MIMETYPE = '"
                        + ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
                        + "'", null, null);

                while (emailCursor.moveToNext()) {
                    String email_user = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    String email_user_type = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

                    CharSequence et = ContactsContract.CommonDataKinds.Email.getTypeLabel(context.getResources(), Integer.parseInt(email_user_type), null);

                    Test.myAssert("Email", contactKey, Integer.parseInt(email_user.substring(0, 3)));

                    if (email_user_type != "Mobile")
                        checkPIMContactsEmail(name, email_user, et.toString());
                }
                emailCursor.close();

                //IM
                Cursor imCursor = cr.query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + " = " + id + " AND ContactsContract.Data.MIMETYPE = '"
                        + ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE
                        + "'", null, null);

                while (imCursor.moveToNext()) {
                    String im_data = imCursor.getString(imCursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));
                    String im_type = imCursor.getString(imCursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE));
                    String im_protocol = imCursor.getString(imCursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.PROTOCOL));

                    CharSequence tp = ContactsContract.CommonDataKinds.Im.getProtocolLabel(context.getResources(), Integer.parseInt(im_protocol),
                            null);
                    CharSequence tp1 = ContactsContract.CommonDataKinds.Im.getTypeLabel(context.getResources(), Integer.parseInt(im_type),
                            null);

                    Test.myAssert("Instant Messaging", contactKey, Integer.parseInt(im_data.substring(0, 3)));

                    if (im_type == "Other")
                        checkPIMContactsIM(name, tp.toString(), tp1.toString(), im_data);

                }
                imCursor.close();

                checkPIMContacts(name, contactKey);
            }
        }

        Log.d(Config.TAG_APP, TAG + "-- getAndCheckDeviceContacts : OK ");
    }

    /**
     * This function compares each email address defines for a contact in the
     * device Address Book with each email address present for a contact in the
     * retrieved contact list ({@code PIMRequest.getContactListXML()}).
     *
     * @param name  - the device contact name
     * @param email - the device contact email address
     * @param type  - the device contact email address type
     */
    private static void checkPIMContactsEmail(String name, String email, String type) {
        for (Contact a_Contact : XmlParserManager.getAddressBook().getContact()) {
            if (a_Contact.getFirstname().equals(name)) {
                for (Device a_Device : a_Contact.getDevices()) {
                    if (a_Device.getValue().equals(email)) {
                        Test.myAssert("Compare Email Device - PIM", type, a_Device.getDeviceType());
                    }
                }
            }
        }
    }

    /**
     * This function compares each IM numbers defines for a contact in the
     * device address book. For each IM we compare the number, the type, and the
     * protocol with those provide in the retrieved contact list (
     * {@code PIMRequest.getContactListXML()}.
     *
     * @param name     - the device contact name
     * @param protocol - the device IM protocol
     * @param type     - the device IM type
     * @param data     - the device IM number
     */
    private static void checkPIMContactsIM(String name, String protocol, String type, String data) {
        for (Contact a_Contact : XmlParserManager.getAddressBook().getContact()) {
            if (a_Contact.getFirstname().equals(name)) {
                for (Device a_Device : a_Contact.getDevices()) {
                    if (a_Device.getValue().equals(data)) {
                        Test.myAssert("Compare IM type Device - PIM", type, a_Device.getDeviceType());
                        Test.myAssert("Compare IM protocol Device - PIM", protocol, a_Device.getSubType());
                    }
                }
            }
        }
    }

    /**
     * This function compares each field of a contact define in the device
     * address book. We seek the contact in the retrieved contact list with his
     * name, and then we compare the contactKey of each field (4 first digit)
     * we the contactKey of the device
     *
     * @param name       - the device contact name
     * @param contactKey - the device contactKey
     */
    private static void checkPIMContacts(String name, int contactKey) {

        for (Contact a_Contact : XmlParserManager.getAddressBook().getContact()) {
            if (a_Contact.getFirstname().equals(name)) {

                if (a_Contact.comments != null)
                    Test.myAssert("Compare Comments Device - PIM", contactKey, Integer.parseInt(a_Contact.comments.substring(0, 3)));
                if (a_Contact.company != null)
                    Test.myAssert("Compare Company Device - PIM", contactKey, Integer.parseInt(a_Contact.company.substring(0, 3)));
                if (a_Contact.function != null)
                    Test.myAssert("Compare Function Device - PIM", contactKey, Integer.parseInt(a_Contact.function.substring(0, 3)));
                if (a_Contact.nickname != null)
                    Test.myAssert("Compare NickName Device - PIM", contactKey, Integer.parseInt(a_Contact.nickname.substring(0, 3)));

                for (Address a_Address : a_Contact.getAddresses()) {

//					assertEquals(contactKey, Integer.parseInt(a_Address.addressType.substring(0, 3)));
                    if (a_Address.city != null)
                        Test.myAssert("Compare City Device - PIM", contactKey, Integer.parseInt(a_Address.city.substring(0, 3)));
                    if (a_Address.country != null)
                        Test.myAssert("Compare Country Device - PIM", contactKey, Integer.parseInt(a_Address.country.substring(0, 3)));
                    if (a_Address.extAdd != null)
                        Test.myAssert("Compare Nei[...]hood Device - PIM", contactKey, Integer.parseInt(a_Address.extAdd.substring(0, 3)));
                    if (a_Address.state != null)
                        Test.myAssert("Compare State Device - PIM", contactKey, Integer.parseInt(a_Address.state.substring(0, 3)));
                    if (a_Address.street1 != null)
                        Test.myAssert("Compare Street Device - PIM", contactKey, Integer.parseInt(a_Address.street1.substring(0, 3)));
                    if (a_Address.zipCode != null)
                        Test.myAssert("Compare Zip Code Device - PIM", contactKey, Integer.parseInt(a_Address.zipCode.substring(0, 3)));
                }

                for (Device a_Device : a_Contact.getDevices()) {
                    if (a_Device.value != null)
                        Test.myAssert("Compare Number Device - PIM", contactKey, Integer.parseInt(a_Device.value.substring(0, 3)));
//					assertEquals(contactKey, Integer.parseInt(a_Device.deviceType.substring(0, 3)));
//					assertEquals(contactKey, Integer.parseInt(a_Device.subType.substring(0, 3)));
                }
            }
        }
    }


    //---------------------------------- Call log

    /**
     * This function goes through the device call log dairy. For each one of
     * them, it will compare its values with those retrieved from the PIM thanks
     * to {@code PIMRequest.getListXml()}.
     *
     * @param context - the test case context
     */
    private static void getAndCheckDeviceCallLog(Context context) {

        Log.d(Config.TAG_APP, TAG + "-- getAndCheckDeviceCallLog ");

        Uri allCalls = Uri.parse("content://call_log/calls"); //CallLog.Calls.CONTENT_URI

        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(allCalls, null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur.moveToNext()) {
                String num = cur.getString(cur.getColumnIndex(CallLog.Calls.NUMBER));
                String date = cur.getString(cur.getColumnIndex(CallLog.Calls.DATE));
                String duration = cur.getString(cur.getColumnIndex(CallLog.Calls.DURATION));
                String type = cur.getString(cur.getColumnIndex(CallLog.Calls.TYPE));

                checkPIMCallLog(num, date, duration, type);
            }
            cur.close();
        }


        Log.d(Config.TAG_APP, TAG + "-- getAndCheckDeviceCallLog : OK ");
    }

    /**
     * This function check the values one by one for each item.
     *
     * @param num      - the call number
     * @param date     - the date
     * @param duration - the call duration
     * @param type     - the kind of call log entry : Missed, Outgoing, Incoming
     */
    private static void checkPIMCallLog(String num, String date, String duration, String type) {
        for (com.onmobile.mom.xmlparser.calllog.CallLog a_CallLog : XmlParserManager.getCallLogDataSet().getCallLogs()) {
            if (a_CallLog.duration.equals(duration)) {
                Test.myAssert("Compare Call Log Msisdn Device - PIM", num, a_CallLog.msisdn);
                Test.myAssert("Compare Call Log duration Device - PIM", duration, a_CallLog.duration);
                Test.myAssert("Compare Call Log type Device - PIM", type, a_CallLog.getDirection());
                
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(date));
                
                String datePim = a_CallLog.begin.substring(0, 10) + " " + a_CallLog.begin.subSequence(11, 19);
                String dateDevice = formatter.format(calendar.getTime());
                
                Test.myAssert("Compare Call Log date Device - PIM", dateDevice, datePim);
            }
        }
    }


    //---------------------------------- SMS

    /**
     * This function goes through all the SMS contains in the device. For each
     * one of them, it will check the values and compare them with the list
     * retrieved from the {@code PIMRequest.getListXml()}.
     *
     * @param context - the test case context
     */
    private static void getAndCheckDeviceSMS(Context context) {
        Log.d(Config.TAG_APP, TAG + "-- getAndCheckDeviceSMS ");

        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(Uri.parse(MySMS.SMS_URI + MySMS.SMS_INBOX_FOLDER), null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur.moveToNext()) {
                String num = cur.getString(cur.getColumnIndex(MySMS.SMS_ADDESS));
                String body = cur.getString(cur.getColumnIndex(MySMS.SMS_BODY));
                String date = cur.getString(cur.getColumnIndex(MySMS.SMS_DATE));

                checkPIMSMS(num, body, date, MySMS.SMS_INBOX_FOLDER);
            }
            cur.close();
        }


        Cursor cur1 = cr.query(Uri.parse(MySMS.SMS_URI + MySMS.SMS_SENT_FOLDER), null, null, null, null);
        if ((cur1 != null ? cur1.getCount() : 0) > 0) {
            while (cur1.moveToNext()) {
                String num = cur1.getString(cur1.getColumnIndex(MySMS.SMS_ADDESS));
                String body = cur1.getString(cur1.getColumnIndex(MySMS.SMS_BODY));
                String date = cur1.getString(cur1.getColumnIndex(MySMS.SMS_DATE));

                checkPIMSMS(num, body, date, MySMS.SMS_SENT_FOLDER);
            }
            cur1.close();
        }


        Log.d(Config.TAG_APP, TAG + "-- getAndCheckDeviceSMS : OK ");
    }

    /**
     * This function checks the values one by one for each item.
     *
     * @param num    - the sms number
     * @param body   - the sms content
     * @param date   - the date when the sms was send, receive
     * @param folder - inbox or outbox
     */
    private static void checkPIMSMS(String num, String body, String date, String folder) {
        for (Message a_SMS : XmlParserManager.getSMSdataSet().getSMSList()) {

            if (a_SMS.text.substring(0, 4).equals(body.substring(0, 4))) {
                Test.myAssert("Compare SMS Msisdn Device - PIM", num, a_SMS.msisdn);
                Test.myAssert("Compare SMS Body Device - PIM", body, a_SMS.text);
                Test.myAssert("Compare SMS Folder Device - PIM", folder, a_SMS.getFolder());

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(date));
                
                String datePim = a_SMS.received.substring(0, 10) + " " + a_SMS.received.subSequence(11, 19);
                String dateDevice = formatter.format(calendar.getTime());
                
                Test.myAssert("Compare SMS Log date Device - PIM", dateDevice, datePim);
            }
        }
    }
}
