package com.onmobile.mom.database;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;

import com.onmobile.mom.R;
import com.onmobile.mom.app.Config;
import com.onmobile.mom.app.Utils;

/**
 * This class manage the Android address book database. It has a function to
 * create contact and fill the address book with a configurable number of
 * contact ({@link Config}). Third of the contact has a picture.
 *
 * @author adetalouet
 */
public class MyContact implements IDatabase {

    /**
     * Tag to debug
     */
    private static final String TAG = "MyContact - ";

    /**
     * Raw contact id defines once and for all
     */
    private final int RAW_CONTACT_ID = 0;

    /**
     * Defines the unique key per contact
     */
    private String MY_CONTACT_DESERIALIZE_KEY;
    /**
     * Counter to define a unique key
     */
    private Integer cpt = 0;

    /**
     * Test cas context
     */
    private Context mContext;
    /**
     * Map to save the contact id once they are added
     */
    private Map<Long, String> mMapContactId;

    /**
     * Constructor
     *
     * @param context - the context of the test case
     */
    public MyContact(Context context) {
        mContext = context;
        mMapContactId = new HashMap<Long, String>();
    }

    /**
     * This function creates a contact and insert it in the Android native
     * database
     *
     * @param thumbnail - if true, the contact will have a picture
     * @param aType     - the ACCOUNT_TYPE (put <code>null</code> for default
     *                  rawcontact)
     * @param aName     - the ACCOUNT_NAME (put <code>null</code> for default
     *                  rawcontact)
     */
    public void addPhoneContact(boolean thumbnail, String aType, String aName) {

        MY_CONTACT_DESERIALIZE_KEY = String.format("%04d", cpt);

        String name = MY_CONTACT_DESERIALIZE_KEY + Utils.randomName();
        String mobile = MY_CONTACT_DESERIALIZE_KEY + Utils.randomNumber();
        String home = MY_CONTACT_DESERIALIZE_KEY + Utils.randomNumber();
        String work = MY_CONTACT_DESERIALIZE_KEY + Utils.randomNumber();

        String familyName = name + "familly";
        String prefix = MY_CONTACT_DESERIALIZE_KEY + name.substring(3, 5);
        String suffix = MY_CONTACT_DESERIALIZE_KEY + name.substring(name.length() - 2, name.length());
        String middleName = MY_CONTACT_DESERIALIZE_KEY + name.substring(5, 7);

        String main = MY_CONTACT_DESERIALIZE_KEY + "0100000000";
        String isdn = MY_CONTACT_DESERIALIZE_KEY + "0200000000";
        String companyMain = MY_CONTACT_DESERIALIZE_KEY + "0300000000";
        String otherFax = MY_CONTACT_DESERIALIZE_KEY + "0400000000";
        String radio = MY_CONTACT_DESERIALIZE_KEY + "0500000000";
        String telex = MY_CONTACT_DESERIALIZE_KEY + "0600000000";
        String ttyTdd = MY_CONTACT_DESERIALIZE_KEY + "0700000000";
        String assistant = MY_CONTACT_DESERIALIZE_KEY + "0800000000";
        String mms = MY_CONTACT_DESERIALIZE_KEY + "0900000000";
        String car = MY_CONTACT_DESERIALIZE_KEY + "1000000000";
        String callback = MY_CONTACT_DESERIALIZE_KEY + "1100000000";
        String other = MY_CONTACT_DESERIALIZE_KEY + "1200000000";
        String pager = MY_CONTACT_DESERIALIZE_KEY + "1300000000";
        String faxWork = MY_CONTACT_DESERIALIZE_KEY + "1400000000";
        String faxHome = MY_CONTACT_DESERIALIZE_KEY + "1500000000";
        // String workMobile = MY_CONTACT_ID+"1600000000";
        // String workPager = MY_CONTACT_ID+"1700000000";

        String emailHome = name + "@home.com";
        String emailWork = name + "@work.com";
        String emailOther = name + "@other.com";
        String emailMobile = name + "@mobile.com";

        String websiteHome = MY_CONTACT_DESERIALIZE_KEY + "website@home.fr";
        String websiteWork = MY_CONTACT_DESERIALIZE_KEY + "websote@work.fr";
        String websiteOther = MY_CONTACT_DESERIALIZE_KEY + "websote@other.fr";

        String anniversary = "24/06/2011";
        String birthday = "01/01/1801";

        String company = MY_CONTACT_DESERIALIZE_KEY + "Voxmobili";
        String jobTitle = MY_CONTACT_DESERIALIZE_KEY + "QA tester";

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, aType)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, aName)
                .build());

        // ______________________________________________________________________________________________________________________________________________________________________________

        // ------------------------------------------------------ Display Name
        if (name != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            name).build());
        }

        // ------------------------------------------------------ Family Name
        if (familyName != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,
                            familyName).build());
        }

        // ------------------------------------------------------ Prefix
        if (prefix != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.PREFIX,
                            prefix).build());
        }

        // ------------------------------------------------------ Suffix
        if (suffix != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.SUFFIX,
                            suffix).build());
        }

        // ------------------------------------------------------ Middle Name
        if (middleName != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME,
                            middleName).build());
        }

        // ______________________________________________________________________________________________________________________________________________________________________________
        // ------------------------------------------------------ Mobile Number
        if (mobile != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                            mobile)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }

        // ------------------------------------------------------ Home Number
        if (home != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                            home)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                    .build());
        }

        // ------------------------------------------------------ Work Number
        if (work != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                            work)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                    .build());
        }

        // ------------------------------------------------------ Work Fax Number
        if (faxWork != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                            faxWork)
                    .withValue(
                            ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK)
                    .build());
        }

        // ------------------------------------------------------ Home Fax Number
        if (faxHome != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                            faxHome)
                    .withValue(
                            ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME)
                    .build());
        }

        // ------------------------------------------------------ Pager Number
        if (pager != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                            pager)
                    .withValue(
                            ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_PAGER)
                    .build());
        }

        // ------------------------------------------------------ Other Number
        if (other != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                            other)
                    .withValue(
                            ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_OTHER)
                    .build());
        }

        // ------------------------------------------------------ Callback Number
        if (callback != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                            callback)
                    .withValue(
                            ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK)
                    .build());
        }

        // ------------------------------------------------------ Car Number
        if (car != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                            car)
                    .withValue(
                            ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_CAR)
                    .build());
        }

        // ------------------------------------------------------ Company Main Number
        if (companyMain != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                            companyMain)
                    .withValue(
                            ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN)
                    .build());
        }

        // ------------------------------------------------------ ISDN Number
        if (isdn != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                            isdn)
                    .withValue(
                            ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_ISDN)
                    .build());
        }

        // ------------------------------------------------------ Main Number
        if (main != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                            main)
                    .withValue(
                            ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MAIN)
                    .build());
        }

        // ------------------------------------------------------ Other Fax Number
        if (otherFax != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                            otherFax)
                    .withValue(
                            ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_OTHER_FAX)
                    .build());
        }

        // ------------------------------------------------------ Radio Number
        if (radio != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                            radio)
                    .withValue(
                            ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_RADIO)
                    .build());
        }

        // ------------------------------------------------------ telex Number
        if (telex != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                            telex)
                    .withValue(
                            ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_TELEX)
                    .build());
        }

        // ------------------------------------------------------ TTY TDD Number
        if (ttyTdd != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ttyTdd)
                    .withValue(
                            ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD)
                    .build());
        }

//		// ------------------------------------------------------ Work Mobile Number
//		if (workMobile != null) {
//			ops.add(ContentProviderOperation
//					.newInsert(ContactsContract.Data.CONTENT_URI)
//					.withValueBackReference(
//							ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
//					.withValue(
//							ContactsContract.Data.MIMETYPE,
//							ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
//					.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
//							workMobile)
//					.withValue(
//							ContactsContract.CommonDataKinds.Phone.TYPE,
//							ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE)
//					.build());
//		}
//		
//		// ------------------------------------------------------ Work Pager Number
//		if (workPager != null) {
//			ops.add(ContentProviderOperation
//					.newInsert(ContactsContract.Data.CONTENT_URI)
//					.withValueBackReference(
//							ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
//					.withValue(
//							ContactsContract.Data.MIMETYPE,
//							ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
//					.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
//							workPager)
//					.withValue(
//							ContactsContract.CommonDataKinds.Phone.TYPE,
//							ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER)
//					.build());
//		}		

        // ------------------------------------------------------ Assistant Number
        if (assistant != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                            assistant)
                    .withValue(
                            ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT)
                    .build());
        }

        // ------------------------------------------------------ MMS Number
        if (mms != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                            mms)
                    .withValue(
                            ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MMS)
                    .build());
        }


        // ______________________________________________________________________________________________________________________________________________________________________________
        // ------------------------------------------------------ Email Home
        if (emailHome != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA,
                            emailHome)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE,
                            ContactsContract.CommonDataKinds.Email.TYPE_HOME)
                    .build());
        }

        // ------------------------------------------------------ Email Work
        if (emailWork != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA,
                            emailWork)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE,
                            ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());
        }

        // ------------------------------------------------------ Email Other
        if (emailOther != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA,
                            emailOther)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE,
                            ContactsContract.CommonDataKinds.Email.TYPE_OTHER)
                    .build());
        }

        // ------------------------------------------------------ Email Mobile
        if (emailMobile != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA,
                            emailMobile)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE,
                            ContactsContract.CommonDataKinds.Email.TYPE_MOBILE)
                    .build());
        }

        // ______________________________________________________________________________________________________________________________________________________________________________
        // ------------------------------------------------------ Website home
        if (websiteHome != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Website.DATA,
                            websiteHome)
                    .withValue(ContactsContract.CommonDataKinds.Website.TYPE,
                            ContactsContract.CommonDataKinds.Website.TYPE_HOME)
                    .build());
        }

        // ------------------------------------------------------ Website work
        if (websiteWork != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Website.DATA,
                            websiteWork)
                    .withValue(ContactsContract.CommonDataKinds.Website.TYPE,
                            ContactsContract.CommonDataKinds.Website.TYPE_WORK)
                    .build());
        }

        // ------------------------------------------------------ Website other
        if (websiteOther != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Website.DATA,
                            websiteOther)
                    .withValue(ContactsContract.CommonDataKinds.Website.TYPE,
                            ContactsContract.CommonDataKinds.Website.TYPE_OTHER)
                    .build());
        }


        // ______________________________________________________________________________________________________________________________________________________________________________
        // ------------------------------------------------------ Company
        if (!company.equals("") && !jobTitle.equals("")) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.Organization.COMPANY,
                            company)
                    .withValue(
                            ContactsContract.CommonDataKinds.Organization.TYPE,
                            ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .withValue(
                            ContactsContract.CommonDataKinds.Organization.TITLE,
                            jobTitle)
                    .withValue(
                            ContactsContract.CommonDataKinds.Organization.TYPE,
                            ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .build());
        }


        // ______________________________________________________________________________________________________________________________________________________________________________
        // ------------------------------------------------------ Anniversary
        if (anniversary != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Event.DATA,
                            anniversary)
                    .withValue(ContactsContract.CommonDataKinds.Event.TYPE,
                            ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY)
                    .build());
        }

        // ------------------------------------------------------ Birthday
        if (birthday != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Event.DATA,
                            birthday)
                    .withValue(ContactsContract.CommonDataKinds.Event.TYPE,
                            ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY)
                    .build());
        }

        // ______________________________________________________________________________________________________________________________________________________________________________
        // ------------------------------------------------------ Postal home
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.TYPE,
                        ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.STREET,
                        MY_CONTACT_DESERIALIZE_KEY + "rue de la maison")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.POBOX,
                        MY_CONTACT_DESERIALIZE_KEY + "pobox")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.NEIGHBORHOOD,
                        MY_CONTACT_DESERIALIZE_KEY + "neighborhood")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.CITY,
                        MY_CONTACT_DESERIALIZE_KEY + "Paris")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.REGION,
                        MY_CONTACT_DESERIALIZE_KEY + "Ile de France")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE,
                        MY_CONTACT_DESERIALIZE_KEY + "75000")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY,
                        MY_CONTACT_DESERIALIZE_KEY + "France").build());

        // ------------------------------------------------------ Work home
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.TYPE,
                        ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.STREET,
                        MY_CONTACT_DESERIALIZE_KEY + "rue du boulot")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.POBOX,
                        MY_CONTACT_DESERIALIZE_KEY + "pobox")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.NEIGHBORHOOD,
                        MY_CONTACT_DESERIALIZE_KEY + "neighborhood")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.CITY,
                        MY_CONTACT_DESERIALIZE_KEY + "Paris")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.REGION,
                        MY_CONTACT_DESERIALIZE_KEY + "Ile de France")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE,
                        MY_CONTACT_DESERIALIZE_KEY + "75000")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY,
                        MY_CONTACT_DESERIALIZE_KEY + "France").build());

        // ------------------------------------------------------ Postal other
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.TYPE,
                        ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.STREET,
                        MY_CONTACT_DESERIALIZE_KEY + "rue des autres")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.POBOX,
                        MY_CONTACT_DESERIALIZE_KEY + "pobox")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.NEIGHBORHOOD,
                        MY_CONTACT_DESERIALIZE_KEY + "neighborhood")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.CITY,
                        MY_CONTACT_DESERIALIZE_KEY + "Paris")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.REGION,
                        MY_CONTACT_DESERIALIZE_KEY + "Ile de France")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE,
                        MY_CONTACT_DESERIALIZE_KEY + "75000")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY,
                        MY_CONTACT_DESERIALIZE_KEY + "France").build());


        // ______________________________________________________________________________________________________________________________________________________________________________
        // ------------------------------------------------------ Photo
        if (thumbnail) {
            Bitmap bmImage = BitmapFactory.decodeResource(
                    mContext.getResources(), R.drawable.ic_launcher);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);

            byte[] b = baos.toByteArray();

            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Photo.DATA15, b)
                    .build());
        }

        // ------------------------------------------------------ Note
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Note.DATA1, MY_CONTACT_DESERIALIZE_KEY + "note")
                .build());

        // ------------------------------------------------------ Nickname
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Nickname.TYPE,
                        ContactsContract.CommonDataKinds.Nickname.TYPE_DEFAULT)
                .withValue(ContactsContract.CommonDataKinds.Nickname.DATA1,
                        MY_CONTACT_DESERIALIZE_KEY + "note").build());

        try {
            mContext.getContentResolver().applyBatch(
                    ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            Log.e(Config.TAG_APP, TAG + "createContact : " + e);
            e.printStackTrace();
        }


        cpt++;

    }

    /**
     * This function creates a contact with only IM's fields and insert it in the Android native
     * database
     *
     * @param thumbnail - if true, the contact will have a picture
     * @param aType     - the ACCOUNT_TYPE (put <code>null</code> for default
     *                  rawcontact)
     * @param aName     - the ACCOUNT_NAME (put <code>null</code> for default
     *                  rawcontact)
     */
    public void addImContact(boolean thumbnail, String aType, String aName) {

        MY_CONTACT_DESERIALIZE_KEY = String.format("%04d", cpt);

        String name = MY_CONTACT_DESERIALIZE_KEY + Utils.randomName();

        String familyName = name + "familly";

        String imHomeAim = MY_CONTACT_DESERIALIZE_KEY + "ihAim";
        String imHomeGoogleTalk = MY_CONTACT_DESERIALIZE_KEY + "ihGoogleTalk";
        String imHomeIcq = MY_CONTACT_DESERIALIZE_KEY + "ihIcq";
        String imHomeJabber = MY_CONTACT_DESERIALIZE_KEY + "ihJabber";
        String imHomeMsn = MY_CONTACT_DESERIALIZE_KEY + "ihMsn";
        String imHomeNetMeeting = MY_CONTACT_DESERIALIZE_KEY + "ihNetMeeting";
        // String imHomeQq = MY_CONTACT_ID+"ihQq";
        // String imHomeSkype = MY_CONTACT_ID+"ihSkype";
        // String imHomeYahoo = MY_CONTACT_ID+"ihYahoo";

        // String imWorkAim = MY_CONTACT_ID+"iwAim";
        // String imWorkGoogleTalk = MY_CONTACT_ID+"iwGoogleTalk";
        // String imWorkIcq = MY_CONTACT_ID+"iwIcq";
        String imWorkJabber = MY_CONTACT_DESERIALIZE_KEY + "iwJabber";
        String imWorkMsn = MY_CONTACT_DESERIALIZE_KEY + "iwMsn";
        String imWorkNetMeetin = MY_CONTACT_DESERIALIZE_KEY + "iwNetMeeting";
        String imWorkQq = MY_CONTACT_DESERIALIZE_KEY + "iwQq";
        String imWorkSkype = MY_CONTACT_DESERIALIZE_KEY + "iwSkype";
        String imWorkYahoo = MY_CONTACT_DESERIALIZE_KEY + "iwYahho";

        String imOtherAim = MY_CONTACT_DESERIALIZE_KEY + "ioAim";
        String imOtherGoogleTalk = MY_CONTACT_DESERIALIZE_KEY + "ioGoogleTalk";
        String imOtherIcq = MY_CONTACT_DESERIALIZE_KEY + "ioIcq";
        String imOtherJabber = MY_CONTACT_DESERIALIZE_KEY + "ioJabber";
        String imOtherMsn = MY_CONTACT_DESERIALIZE_KEY + "ioMsn";
        String imOtherNetMeeting = MY_CONTACT_DESERIALIZE_KEY + "ioNetMeeting";
        String imOtherQq = MY_CONTACT_DESERIALIZE_KEY + "ioQq";
        String imOtherSkype = MY_CONTACT_DESERIALIZE_KEY + "ioSkype";
        String imOtherYahoo = MY_CONTACT_DESERIALIZE_KEY + "ioYahoo";

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, aType)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, aName)
                .build());

        // ______________________________________________________________________________________________________________________________________________________________________________

        // ------------------------------------------------------ Display Name
        if (name != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            name).build());
        }

        // ------------------------------------------------------ Family Name
        if (familyName != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,
                            familyName).build());
        }


        // ______________________________________________________________________________________________________________________________________________________________________________
        // ------------------------------------------------------ IM Home
        if (imHomeAim != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Im.DATA,
                            imHomeAim)
                    .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
                            ContactsContract.CommonDataKinds.Im.TYPE_HOME)
                    .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,
                            ContactsContract.CommonDataKinds.Im.PROTOCOL_AIM)
                    .build());
        }

        // ------------------------------------------------------ IM Home
        if (imHomeGoogleTalk != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Im.DATA,
                            imHomeGoogleTalk)
                    .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
                            ContactsContract.CommonDataKinds.Im.TYPE_HOME)
                    .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,
                            ContactsContract.CommonDataKinds.Im.PROTOCOL_GOOGLE_TALK)
                    .build());
        }

        // ------------------------------------------------------ IM Home
        if (imHomeIcq != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Im.DATA,
                            imHomeIcq)
                    .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
                            ContactsContract.CommonDataKinds.Im.TYPE_HOME)
                    .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,
                            ContactsContract.CommonDataKinds.Im.PROTOCOL_ICQ)
                    .build());
        }

        // ------------------------------------------------------ IM Home
        if (imHomeJabber != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Im.DATA,
                            imHomeJabber)
                    .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
                            ContactsContract.CommonDataKinds.Im.TYPE_HOME)
                    .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,
                            ContactsContract.CommonDataKinds.Im.PROTOCOL_JABBER)
                    .build());
        }

        // ------------------------------------------------------ IM Home
        if (imHomeMsn != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Im.DATA,
                            imHomeMsn)
                    .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
                            ContactsContract.CommonDataKinds.Im.TYPE_HOME)
                    .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,
                            ContactsContract.CommonDataKinds.Im.PROTOCOL_MSN)
                    .build());
        }

        // ------------------------------------------------------ IM Home
        if (imHomeNetMeeting != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Im.DATA,
                            imHomeNetMeeting)
                    .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
                            ContactsContract.CommonDataKinds.Im.TYPE_HOME)
                    .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,
                            ContactsContract.CommonDataKinds.Im.PROTOCOL_NETMEETING)
                    .build());
        }

        // // ------------------------------------------------------ IM Home
        // if (imHomeQq != null) {
        // ops.add(ContentProviderOperation
        // .newInsert(ContactsContract.Data.CONTENT_URI)
        // .withValueBackReference(
        // ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
        // .withValue(
        // ContactsContract.Data.MIMETYPE,
        // ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
        // .withValue(ContactsContract.CommonDataKinds.Im.DATA,
        // imHomeQq)
        // .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
        // ContactsContract.CommonDataKinds.Im.TYPE_HOME)
        // .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,
        // ContactsContract.CommonDataKinds.Im.PROTOCOL_QQ)
        // .build());
        // }
        //
        // // ------------------------------------------------------ IM Home
        // if (imHomeSkype != null) {
        // ops.add(ContentProviderOperation
        // .newInsert(ContactsContract.Data.CONTENT_URI)
        // .withValueBackReference(
        // ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
        // .withValue(
        // ContactsContract.Data.MIMETYPE,
        // ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
        // .withValue(ContactsContract.CommonDataKinds.Im.DATA,
        // imHomeSkype)
        // .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
        // ContactsContract.CommonDataKinds.Im.TYPE_HOME)
        // .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,
        // ContactsContract.CommonDataKinds.Im.PROTOCOL_SKYPE)
        // .build());
        // }
        //
        // // ------------------------------------------------------ IM Home
        // if (imHomeYahoo != null) {
        // ops.add(ContentProviderOperation
        // .newInsert(ContactsContract.Data.CONTENT_URI)
        // .withValueBackReference(
        // ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
        // .withValue(
        // ContactsContract.Data.MIMETYPE,
        // ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
        // .withValue(ContactsContract.CommonDataKinds.Im.DATA,
        // imHomeYahoo)
        // .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
        // ContactsContract.CommonDataKinds.Im.TYPE_HOME)
        // .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,
        // ContactsContract.CommonDataKinds.Im.PROTOCOL_YAHOO)
        // .build());
        // }
        //
        // // ------------------------------------------------------ IM Work
        // if (imWorkAim != null) {
        // ops.add(ContentProviderOperation
        // .newInsert(ContactsContract.Data.CONTENT_URI)
        // .withValueBackReference(
        // ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
        // .withValue(
        // ContactsContract.Data.MIMETYPE,
        // ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
        // .withValue(ContactsContract.CommonDataKinds.Im.DATA,
        // imWorkAim)
        // .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
        // ContactsContract.CommonDataKinds.Im.TYPE_WORK)
        // .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,
        // ContactsContract.CommonDataKinds.Im.PROTOCOL_AIM)
        // .build());
        // }
        //
        // // ------------------------------------------------------ IM Work
        // if (imWorkGoogleTalk != null) {
        // ops.add(ContentProviderOperation
        // .newInsert(ContactsContract.Data.CONTENT_URI)
        // .withValueBackReference(
        // ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
        // .withValue(
        // ContactsContract.Data.MIMETYPE,
        // ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
        // .withValue(ContactsContract.CommonDataKinds.Im.DATA,
        // imWorkGoogleTalk)
        // .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
        // ContactsContract.CommonDataKinds.Im.TYPE_WORK)
        // .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,
        // ContactsContract.CommonDataKinds.Im.PROTOCOL_GOOGLE_TALK)
        // .build());
        // }
        //
        // // ------------------------------------------------------ IM Work
        // if (imWorkIcq != null) {
        // ops.add(ContentProviderOperation
        // .newInsert(ContactsContract.Data.CONTENT_URI)
        // .withValueBackReference(
        // ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
        // .withValue(
        // ContactsContract.Data.MIMETYPE,
        // ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
        // .withValue(ContactsContract.CommonDataKinds.Im.DATA,
        // imWorkIcq)
        // .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
        // ContactsContract.CommonDataKinds.Im.TYPE_WORK)
        // .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,
        // ContactsContract.CommonDataKinds.Im.PROTOCOL_ICQ)
        // .build());
        // }
        //
        // ------------------------------------------------------ IM Work
        if (imWorkJabber != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID,
                            RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Im.DATA,
                            imWorkJabber)
                    .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
                            ContactsContract.CommonDataKinds.Im.TYPE_WORK)
                    .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,
                            ContactsContract.CommonDataKinds.Im.PROTOCOL_JABBER)
                    .build());
        }

        // ------------------------------------------------------ IM Work
        if (imWorkMsn != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID,
                            RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Im.DATA,
                            imWorkMsn)
                    .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
                            ContactsContract.CommonDataKinds.Im.TYPE_WORK)
                    .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,
                            ContactsContract.CommonDataKinds.Im.PROTOCOL_MSN)
                    .build());
        }

        // ------------------------------------------------------ IM Work
        if (imWorkNetMeetin != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID,
                            RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Im.DATA,
                            imWorkNetMeetin)
                    .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
                            ContactsContract.CommonDataKinds.Im.TYPE_WORK)
                    .withValue(
                            ContactsContract.CommonDataKinds.Im.PROTOCOL,
                            ContactsContract.CommonDataKinds.Im.PROTOCOL_NETMEETING)
                    .build());
        }

        // ------------------------------------------------------ IM Work
        if (imWorkQq != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID,
                            RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Im.DATA,
                            imWorkQq)
                    .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
                            ContactsContract.CommonDataKinds.Im.TYPE_WORK)
                    .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,
                            ContactsContract.CommonDataKinds.Im.PROTOCOL_QQ)
                    .build());
        }

        // ------------------------------------------------------ IM Work
        if (imWorkSkype != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID,
                            RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Im.DATA,
                            imWorkSkype)
                    .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
                            ContactsContract.CommonDataKinds.Im.TYPE_WORK)
                    .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,
                            ContactsContract.CommonDataKinds.Im.PROTOCOL_SKYPE)
                    .build());
        }

        // ------------------------------------------------------ IM Work
        if (imWorkYahoo != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID,
                            RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Im.DATA,
                            imWorkYahoo)
                    .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
                            ContactsContract.CommonDataKinds.Im.TYPE_WORK)
                    .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,
                            ContactsContract.CommonDataKinds.Im.PROTOCOL_YAHOO)
                    .build());
        }

        // ------------------------------------------------------ IM Other
        if (imOtherAim != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID,
                            RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Im.DATA,
                            imOtherAim)
                    .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
                            ContactsContract.CommonDataKinds.Im.TYPE_OTHER)
                    .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,
                            ContactsContract.CommonDataKinds.Im.PROTOCOL_AIM)
                    .build());
        }

        // ------------------------------------------------------ IM Other
        if (imOtherGoogleTalk != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID,
                            RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Im.DATA,
                            imOtherGoogleTalk)
                    .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
                            ContactsContract.CommonDataKinds.Im.TYPE_OTHER)
                    .withValue(
                            ContactsContract.CommonDataKinds.Im.PROTOCOL,
                            ContactsContract.CommonDataKinds.Im.PROTOCOL_GOOGLE_TALK)
                    .build());
        }

        // ------------------------------------------------------ IM Other
        if (imOtherIcq != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID,
                            RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Im.DATA,
                            imOtherIcq)
                    .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
                            ContactsContract.CommonDataKinds.Im.TYPE_OTHER)
                    .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,
                            ContactsContract.CommonDataKinds.Im.PROTOCOL_ICQ)
                    .build());
        }

        // ------------------------------------------------------ IM Other
        if (imOtherJabber != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Im.DATA,
                            imOtherJabber)
                    .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
                            ContactsContract.CommonDataKinds.Im.TYPE_OTHER)
                    .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,
                            ContactsContract.CommonDataKinds.Im.PROTOCOL_JABBER)
                    .build());
        }

        // ------------------------------------------------------ IM Other
        if (imOtherMsn != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Im.DATA,
                            imOtherMsn)
                    .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
                            ContactsContract.CommonDataKinds.Im.TYPE_OTHER)
                    .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,
                            ContactsContract.CommonDataKinds.Im.PROTOCOL_MSN)
                    .build());
        }

        // ------------------------------------------------------ IM Other
        if (imOtherNetMeeting != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Im.DATA,
                            imOtherNetMeeting)
                    .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
                            ContactsContract.CommonDataKinds.Im.TYPE_OTHER)
                    .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,
                            ContactsContract.CommonDataKinds.Im.PROTOCOL_NETMEETING)
                    .build());
        }

        // ------------------------------------------------------ IM Other
        if (imOtherQq != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Im.DATA,
                            imOtherQq)
                    .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
                            ContactsContract.CommonDataKinds.Im.TYPE_OTHER)
                    .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,
                            ContactsContract.CommonDataKinds.Im.PROTOCOL_NETMEETING)
                    .build());
        }

        // ------------------------------------------------------ IM Other
        if (imOtherSkype != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Im.DATA,
                            imOtherSkype)
                    .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
                            ContactsContract.CommonDataKinds.Im.TYPE_OTHER)
                    .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,
                            ContactsContract.CommonDataKinds.Im.PROTOCOL_SKYPE)
                    .build());
        }

        // ------------------------------------------------------ IM Other
        if (imOtherYahoo != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Im.DATA,
                            imOtherYahoo)
                    .withValue(ContactsContract.CommonDataKinds.Im.TYPE,
                            ContactsContract.CommonDataKinds.Im.TYPE_OTHER)
                    .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,
                            ContactsContract.CommonDataKinds.Im.PROTOCOL_YAHOO)
                    .build());
        }

        // ______________________________________________________________________________________________________________________________________________________________________________
        // ------------------------------------------------------ Postal home
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.TYPE,
                        ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.STREET,
                        MY_CONTACT_DESERIALIZE_KEY + "rue de la maison")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.POBOX,
                        MY_CONTACT_DESERIALIZE_KEY + "pobox")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.NEIGHBORHOOD,
                        MY_CONTACT_DESERIALIZE_KEY + "neighborhood")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.CITY,
                        MY_CONTACT_DESERIALIZE_KEY + "Paris")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.REGION,
                        MY_CONTACT_DESERIALIZE_KEY + "Ile de France")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE,
                        MY_CONTACT_DESERIALIZE_KEY + "75000")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY,
                        MY_CONTACT_DESERIALIZE_KEY + "France").build());

        // ------------------------------------------------------ Work home
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.TYPE,
                        ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.STREET,
                        MY_CONTACT_DESERIALIZE_KEY + "rue du boulot")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.POBOX,
                        MY_CONTACT_DESERIALIZE_KEY + "pobox")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.NEIGHBORHOOD,
                        MY_CONTACT_DESERIALIZE_KEY + "neighborhood")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.CITY,
                        MY_CONTACT_DESERIALIZE_KEY + "Paris")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.REGION,
                        MY_CONTACT_DESERIALIZE_KEY + "Ile de France")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE,
                        MY_CONTACT_DESERIALIZE_KEY + "75000")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY,
                        MY_CONTACT_DESERIALIZE_KEY + "France").build());

        // ------------------------------------------------------ Postal other
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.TYPE,
                        ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.STREET,
                        MY_CONTACT_DESERIALIZE_KEY + "rue des autres")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.POBOX,
                        MY_CONTACT_DESERIALIZE_KEY + "pobox")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.NEIGHBORHOOD,
                        MY_CONTACT_DESERIALIZE_KEY + "neighborhood")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.CITY,
                        MY_CONTACT_DESERIALIZE_KEY + "Paris")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.REGION,
                        MY_CONTACT_DESERIALIZE_KEY + "Ile de France")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE,
                        MY_CONTACT_DESERIALIZE_KEY + "75000")
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY,
                        MY_CONTACT_DESERIALIZE_KEY + "France").build());


        // ______________________________________________________________________________________________________________________________________________________________________________
        // ------------------------------------------------------ Photo
        if (thumbnail) {
            Bitmap bmImage = BitmapFactory.decodeResource(
                    mContext.getResources(), R.drawable.ic_launcher);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);

            byte[] b = baos.toByteArray();

            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Photo.DATA15, b)
                    .build());
        }

        // ------------------------------------------------------ Note
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Note.DATA1, MY_CONTACT_DESERIALIZE_KEY + "note")
                .build());

        // ------------------------------------------------------ Nickname
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, RAW_CONTACT_ID)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Nickname.TYPE,
                        ContactsContract.CommonDataKinds.Nickname.TYPE_DEFAULT)
                .withValue(ContactsContract.CommonDataKinds.Nickname.DATA1,
                        MY_CONTACT_DESERIALIZE_KEY + "Nickname").build());

        try {
            mContext.getContentResolver().applyBatch(
                    ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            Log.e(Config.TAG_APP, TAG + "createContact : " + e);
            e.printStackTrace();
        }

        cpt++;

    }

    /**
     * This function goes through the Address Book and save the contactId of each entry.
     */
    private void getContactIds() {
        ContentResolver cr = mContext.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        if (cur != null) {
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {

                    Long id = cur.getLong(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    mMapContactId.put(id, name);
                }
            }
            cur.close();
        }
    }


    /**
     * This function update information of all the contact.
     *
     * @param isRestore - true if it is before a restore
     */
    public void updateDataSet(boolean isRestore) {

        Log.d(Config.TAG_APP, TAG + " --- updateContact --- ");

        Set<Long> key = mMapContactId.keySet();
        Iterator<Long> it1 = key.iterator();

        if (!isRestore) {
            Log.d(Config.TAG_APP,
                    TAG
                            + "updateContact : modify WORK number for all contact ");

            // Modify the WORK number for each contact
            while (it1.hasNext()) {

                Long id = it1.next();
                String name = mMapContactId.get(id);

                String contactDeserializerKey = name.substring(0, 3);

                ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

                String selectPhone = ContactsContract.Data.CONTACT_ID
                        + "=? AND "
                        + ContactsContract.Data.MIMETYPE
                        + "='"
                        + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                        + "'" + " AND "
                        + ContactsContract.CommonDataKinds.Phone.TYPE + "=?";
                String[] phoneArgs = new String[]{
                        String.valueOf(id),
                        String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_WORK)};
                ops.add(ContentProviderOperation
                        .newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(selectPhone, phoneArgs)
                        .withValue(
                                ContactsContract.CommonDataKinds.Phone.NUMBER,
                                contactDeserializerKey + "9999999999").build());
                try {
                    mContext.getContentResolver().applyBatch(
                            ContactsContract.AUTHORITY, ops);
                } catch (RemoteException e) {
                    Log.e(Config.TAG_APP, TAG + "updateContact : " + e);
                    e.printStackTrace();
                } catch (OperationApplicationException e) {
                    Log.e(Config.TAG_APP, TAG + "updateContact : " + e);
                    e.printStackTrace();
                }
            }

            Log.d(Config.TAG_APP, TAG + "updateContact : add "
                    + Config.NUMBER_OF_CONTACT_TO_ADD + " contacts --- ");
            // Adding 10 contacts
            for (int j = 0; j < Config.NUMBER_OF_CONTACT_TO_ADD; j++) {
                if (j % 2 == 0)
                    addPhoneContact(true, Config.ACCOUNT_TYPE,
                            Config.ACCOUNT_NAME);
                else
                    addImContact(false, Config.ACCOUNT_TYPE,
                            Config.ACCOUNT_NAME);
            }
        }

        Log.d(Config.TAG_APP, TAG + "updateContact : delete "
                + Config.NUMBER_OF_CONTACT_TO_DELETE + " contacts --- ");
        // Delete 5 contacts
        Iterator<Long> it2 = key.iterator();
        for (int i = 0; i < Config.NUMBER_OF_CONTACT_TO_DELETE; i++) {
            it2.hasNext();
            Long id = it2.next();
            deleteOne(id.toString());

        }
        Log.d(Config.TAG_APP, TAG + " --- updateContact: ended --- ");
    }

    /**
     * This function is use to delete a contact by id
     *
     * @param id - the contact id
     */
    private void deleteOne(String id) {

        String where = String.format("%s = ?", ContactsContract.RawContacts.CONTACT_ID);
        String[] whereParams = new String[]{id};


        mContext.getContentResolver().delete(
                ContactsContract.RawContacts.CONTENT_URI
                        .buildUpon()
                        .appendQueryParameter(
                                ContactsContract.CALLER_IS_SYNCADAPTER, "true")
                        .build(), where, whereParams);
    }

    @Override
    public void createDataSet() {

        Log.d(Config.TAG_APP, TAG + "createDataSet started");

        for (int i = 0; i < Config.NUMBER_OF_CONTACT; i++) {
            // The aim here is to create contact from 2 different RawContact. Actually not working.
            if (i % 2 == 0) {
                if (i % 3 == 0)
                    addPhoneContact(true, Config.ACCOUNT_TYPE, Config.ACCOUNT_NAME);
                else
                    addPhoneContact(false, Config.ACCOUNT_TYPE, Config.ACCOUNT_NAME);
                // TODO replace the account type and name with an other one when the sdk will be corrected
                // JIRA SYNCSDKAND-51
            } else {
                if (i % 3 == 0)
                    addImContact(true, Config.ACCOUNT_TYPE, Config.ACCOUNT_NAME);
                else
                    addImContact(false, Config.ACCOUNT_TYPE, Config.ACCOUNT_NAME);
                // TODO replace the account type and name with an other one when the sdk will be corrected
                // JIRA SYNCSDKAND-51
            }
        }

        getContactIds();

        Log.d(Config.TAG_APP, TAG + "create " + Config.NUMBER_OF_CONTACT
                + " Contacts ");
    }

    @Override
    public void deleteAll() {
        int d = mContext.getContentResolver().delete(
                ContactsContract.RawContacts.CONTENT_URI
                        .buildUpon()
                        .appendQueryParameter(
                                ContactsContract.CALLER_IS_SYNCADAPTER, "true")
                        .build(), null, null);

        Log.d(Config.TAG_APP, TAG + " delete " + d + " Contacts");
    }

    @Override
    public int getCount() {
        int nb = -1;
        Cursor phones = mContext.getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI, null, null, null,
                null);
        nb = phones != null ? phones.getCount() : 0;
        phones.close();

        return nb;
    }

}
