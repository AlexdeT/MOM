package com.onmobile.mom.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.onmobile.mom.R;
import com.onmobile.mom.app.Config;
import com.onmobile.mom.app.Utils;
import com.onmobile.mom.test.Test;

/**
 * This class manage the Android SMS database. It has a function to create SMS
 * and fill the mail box with a configurable number of SMS ({@link Config}).
 * Half of the SMS are incoming SMS, the other half are outgoing SMS.
 *
 * @author adetalouet
 */
public class MySMS implements IDatabase {

    /**
     * Tag to debug
     */
    private static final String TAG = "MySMS - ";

    /**
     * Test case context
     */
    private Context mContext;
    /**
     * Use to create random numbers
     */
    private Random mGenerator;

    private List<Long> mSMSids;

    /**
     * Number of sms created in the <code> @string/ </code>
     */
    private static final int SMS_POOL = 10;

    // Native Android SMS database keys. There is no provider for the SMS database.
    /**
     * SMS Provider - define the address key
     */
    public static final String SMS_ADDESS = "address";
    /**
     * SMS Provider - define the body key
     */
    public static final String SMS_BODY = "body";
    /**
     * SMS Provider - define the date key
     */
    public static final String SMS_DATE = "date";
    /**
     * SMS Provider - define the content uri key
     */
    public static final String SMS_URI = "content://sms/";
    /**
     * SMS Provider - define the inbox uri key
     */
    public static final String SMS_INBOX_FOLDER = "inbox";
    /**
     * SMS Provider - define the outbox uri key
     */
    public static final String SMS_SENT_FOLDER = "sent";

    /**
     * Counter to define a unique key
     */
    private Integer cpt = 0;

    /**
     * Constructor
     *
     * @param context - the context of the test case
     */
    public MySMS(Context context) {
        mContext = context;
        mGenerator = new Random();
    }

    /**
     * This function creates an SMS and insert it in the Android native database
     *
     * @param folderName - either inbox/outbox/draft ...
     * @param address    - the contact broadcast number
     */
    public void add(String folderName, String address) {

	/*
      Defines the unique key per sms
	 */
        String MY_SMS_DESERIALIZE_KEY = String.format("%04d", cpt);

        // Kind of SMS that can be sent
        ArrayList<String> smsStringPool = new ArrayList<String>();
        smsStringPool.add(mContext.getString(R.string.sms0));
        smsStringPool.add(mContext.getString(R.string.sms1));
        smsStringPool.add(mContext.getString(R.string.sms2));
        smsStringPool.add(mContext.getString(R.string.sms3));
        smsStringPool.add(mContext.getString(R.string.sms4));
        smsStringPool.add(mContext.getString(R.string.sms5));
        smsStringPool.add(mContext.getString(R.string.sms6));
        smsStringPool.add(mContext.getString(R.string.sms7));
        smsStringPool.add(mContext.getString(R.string.sms8));
        smsStringPool.add(mContext.getString(R.string.sms9));
        smsStringPool.add(mContext.getString(R.string.sms10));

        int randomSmsId = mGenerator.nextInt(MySMS.SMS_POOL);
        String sms = MY_SMS_DESERIALIZE_KEY + smsStringPool.get(randomSmsId);

        try {
            ContentValues values = new ContentValues();
            values.put(MySMS.SMS_ADDESS, address);
            values.put(MySMS.SMS_BODY, sms);
            values.put(MySMS.SMS_DATE, System.currentTimeMillis());
            mContext.getContentResolver().insert(
                    Uri.parse(MySMS.SMS_URI + folderName), values);
        } catch (Exception e) {
            Log.e(Config.TAG_APP, TAG + "addSms : " + e);
            e.printStackTrace();
        }

        cpt++;
    }

    /**
     * This function create the SMS thread.
     *
     * @param _nb          - number of SMS to create
     * @param _telNumber   - contact to who the SMS is send/received
     * @param isAdressInAB - define whether the contact is in the Address Book
     */
    protected void createSMSThread(int _nb, String _telNumber,
                                   boolean isAdressInAB) {

        String telNumber;
        telNumber = _telNumber;

        int randomUserId;

        for (int i = 0; i < _nb; i++) {

            // Randomly select a contact
            if (_telNumber == null && isAdressInAB) {
                randomUserId = mGenerator
                        .nextInt(Config.MAX_PHONE_NUMBER_IN_AB);
                telNumber = Test.mABNumbers.get(randomUserId);
            }

            // Create new contact
            if (_telNumber == null && !isAdressInAB)
                telNumber = Utils.randomNumber();

            if (i % 2 == 0)
                add(MySMS.SMS_INBOX_FOLDER, telNumber);
            else
                add(MySMS.SMS_SENT_FOLDER, telNumber);
        }
    }

    @Override
    public void createDataSet() {

        Log.d(Config.TAG_APP, TAG + "createDataSet started");

        int nbSmsToSent;
        int randomUserId;
        String telNumber;

        // Create a conversation with 40% of all the SMS -
        nbSmsToSent = (Config.NUMBER_OF_SMS * 40) / 100;
        randomUserId = mGenerator.nextInt(Config.MAX_PHONE_NUMBER_IN_AB);
        telNumber = Test.mABNumbers.get(randomUserId);
        createSMSThread(nbSmsToSent, telNumber, true);

        // Create 4 conversations with 10% of all the SMS for each
        nbSmsToSent = (Config.NUMBER_OF_SMS * 10) / 100;
        for (int j = 0; j < 4; j++) {
            randomUserId = mGenerator.nextInt(Config.MAX_PHONE_NUMBER_IN_AB);
            telNumber = Test.mABNumbers.get(randomUserId);
            createSMSThread(nbSmsToSent, telNumber, true);
        }

        // Create a conversation with 10% of all the SMS for N contact in the AB
        nbSmsToSent = (Config.NUMBER_OF_SMS * 10) / 100;
        createSMSThread(nbSmsToSent, null, true);

        // Create a conversation with 10% of all the SMS for N contact not in
        // the AB
        nbSmsToSent = (Config.NUMBER_OF_SMS * 10) / 100;
        createSMSThread(nbSmsToSent, null, false);

        Log.d(Config.TAG_APP, TAG + "create " + Config.NUMBER_OF_SMS + " SMS");

    }

    /**
     * Update the data set by adding and deleting items.
     *
     * @param isRestore - true if it is before a restore
     */
    public void updateDataSet(boolean isRestore) {

        Log.d(Config.TAG_APP, TAG + "--- updateSMS  ---");
        getSMSIds();

        // deletes SMS
        for (int i = 0; i < Config.NUMBER_OF_SMS_TO_DELETE; i++) {
            deleteOne(mSMSids.get(i));
        }
        Log.d(Config.TAG_APP, TAG + "updateSMS : delete "
                + Config.NUMBER_OF_SMS_TO_DELETE + " SMS");

        if (!isRestore) {
            // add SMS
            for (int i = 0; i < Config.NUMBER_OF_SMS_TO_ADD; i++) {
                int randomUserId = mGenerator
                        .nextInt(Config.MAX_PHONE_NUMBER_IN_AB);
                String telNumber = Test.mABNumbers.get(randomUserId);
                add(SMS_INBOX_FOLDER, telNumber);
            }
            Log.d(Config.TAG_APP, TAG + "updateSMS : add "
                    + Config.NUMBER_OF_SMS_TO_ADD + " SMS");
        }
        Log.d(Config.TAG_APP, TAG + "--- updateSMS : ended ---");
    }

    /**
     * This function is use to delete a SMS by id
     *
     * @param id - the SMS id
     */
    private void deleteOne(Long id) {

        String where = String.format("%s = ?", "_id");
        String[] whereParams = new String[]{id.toString()};

        mContext.getContentResolver().delete(Uri.parse(MySMS.SMS_URI), where,
                whereParams);
    }

    /**
     * This function goes through the SMS and save the SMSId of each entry.
     */
    public void getSMSIds() {

        mSMSids = new ArrayList<Long>();

        ContentResolver cr = mContext.getContentResolver();
        Cursor cur = cr.query(Uri.parse(MySMS.SMS_URI), null,
                null, null, null);
        if (cur != null) {
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    Long id = cur.getLong(cur.getColumnIndex("_id"));
                    mSMSids.add(id);
                }
            }
            cur.close();
        }
    }

    @Override
    public void deleteAll() {
        int d = mContext.getContentResolver().delete(Uri.parse(MySMS.SMS_URI),
                null, null);
        Log.d(Config.TAG_APP, TAG + " delete " + d + " SMS");
    }

    @Override
    public int getCount() {
        int nb = -1;
        Cursor phones = mContext.getContentResolver().query(
                Uri.parse(MySMS.SMS_URI), null, null, null, null);
        if (phones != null) {
            nb = phones.getCount();
            phones.close();
        }
        return nb;
    }
}