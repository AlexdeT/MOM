package com.onmobile.mom.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

import com.onmobile.mom.app.Config;
import com.onmobile.mom.app.Utils;
import com.onmobile.mom.test.Test;

/**
 * This class manage the call log Android database. It has a function to create
 * call log and fill the call log book with a configurable number of call log (
 * {@link Config}). Half of the call log are incoming calls, the other half are
 * outgoing calls.
 *
 * @author adetalouet
 */
public class MyCallLog implements IDatabase {

    /**
     * Tag to debug
     */
    private static String TAG = "MyCallLog - ";

    /**
     * Variable to increment the duration of the phone call. Thus, each added
     * phone call has its own duration. So when we check the result, we compare
     * the phone call per duration
     */
    private int durationIncrement = 0;

    /**
     * Test case context
     */
    private Context mContext;
    /**
     * Generator to provides random number
     */
    private Random mGenerator;
    /**
     * List containing all the call log ids
     */
    private List<Long> mCallLogIds;

    /**
     * Constructor
     *
     * @param context - the context of the test case
     */
    public MyCallLog(Context context) {
        mContext = context;
        mGenerator = new Random();
    }

    /**
     * This function creates a call log and insert it in the Android native
     * database.
     *
     * @param type   - the type of call (incoming/outgoing/missed/rejected)
     * @param number - the contact number
     */
    public void add(int type, String number) {
        ContentValues values = new ContentValues();
        values.put(CallLog.Calls.CACHED_NUMBER_TYPE, 0);
        values.put(CallLog.Calls.TYPE, type);
        values.put(CallLog.Calls.DATE, System.currentTimeMillis());
        values.put(CallLog.Calls.DURATION, 50 + durationIncrement);
        values.put(CallLog.Calls.NUMBER, number);
        mContext.getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);

        durationIncrement++;
    }

    /**
     * This function creates the call log thread
     *
     * @param _nb           - number of call log to create
     * @param _telNumber    - contact who has call / been called
     * @param _isNumberInAB - defines if the number is in the Address Book
     */
    private void createCallLogThread(int _nb, String _telNumber,
                                     boolean _isNumberInAB) {

        String number;
        number = _telNumber;
        int typeCallLog = -1;

        int randomUserId;

        for (int i = 0; i < _nb; i++) {

            // Randomly select a contact
            if (_telNumber == null && _isNumberInAB) {
                randomUserId = mGenerator
                        .nextInt(Config.MAX_PHONE_NUMBER_IN_AB);
                number = Test.mABNumbers.get(randomUserId);
            }

            // Create new contact
            if (_telNumber == null && !_isNumberInAB)
                number = Utils.randomNumber();

            if (i % 1 == 0)
                typeCallLog = CallLog.Calls.INCOMING_TYPE;
            if (i % 2 == 0)
                typeCallLog = CallLog.Calls.MISSED_TYPE;
            if (i % 3 == 0)
                typeCallLog = CallLog.Calls.OUTGOING_TYPE;


            add(typeCallLog, number);

        }
    }

    /**
     * This function update the call log data set by adding and deleting items.
     *
     * @param isRestore - true if it is before a restore
     */
    public void updateDataSet(boolean isRestore) {

        Log.d(Config.TAG_APP, TAG + "--- updateCallLog  ---");

        getCallLogIds();

        // deletes call log
        for (int i = 0; i < Config.NUMBER_OF_CALL_LOG_TO_DELETE; i++) {
            deleteOne(mCallLogIds.get(i));
        }
        Log.d(Config.TAG_APP, TAG + "updateCallLog : delete " + Config.NUMBER_OF_CALL_LOG_TO_DELETE + " call log");

        if (!isRestore) {
            // add call log
            for (int i = 0; i < Config.NUMBER_OF_CALL_LOG_TO_ADD; i++) {
                int randomUserId = mGenerator
                        .nextInt(Config.MAX_PHONE_NUMBER_IN_AB);
                String telNumber = Test.mABNumbers.get(randomUserId);
                int typeCallLog = -1;
                if (i % 1 == 0)
                    typeCallLog = CallLog.Calls.INCOMING_TYPE;
                if (i % 2 == 0)
                    typeCallLog = CallLog.Calls.MISSED_TYPE;
                if (i % 3 == 0)
                    typeCallLog = CallLog.Calls.OUTGOING_TYPE;

                add(typeCallLog, telNumber);
            }
            Log.d(Config.TAG_APP, TAG + "updateCallLog : add "
                    + Config.NUMBER_OF_CALL_LOG_TO_ADD + " call log");
        }
        Log.d(Config.TAG_APP, TAG + "--- updateCallLog : ended ---");
    }

    /**
     * This function is use to delete a Call Log by id
     *
     * @param id - the call log id
     */
    private void deleteOne(Long id) {

        String where = String.format("%s = ?", "_id");
        String[] whereParams = new String[]{id.toString()};

        mContext.getContentResolver().delete(CallLog.Calls.CONTENT_URI, where,
                whereParams);
    }

    /**
     * This function goes through the Address Book and save the contactId of each entry.
     */
    public void getCallLogIds() {

        mCallLogIds = new ArrayList<Long>();

        ContentResolver cr = mContext.getContentResolver();
        Cursor cur = cr.query(CallLog.Calls.CONTENT_URI, null,
                null, null, null);
        if (cur != null) {
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    Long id = cur.getLong(cur.getColumnIndex(CallLog.Calls._ID));
                    mCallLogIds.add(id);
                }
            }
            cur.close();
        }
    }

    @Override
    public void createDataSet() {

        Log.d(Config.TAG_APP, TAG + "createDataSet started");

        int nbCallToAdd;
        int randomUserId;
        String number;

        // Create a call logs with 40% of all the call for one user
        nbCallToAdd = (Config.NUMBER_OF_CALL_LOG * 40) / 100;
        randomUserId = mGenerator.nextInt(Config.MAX_PHONE_NUMBER_IN_AB);
        number = Test.mABNumbers.get(randomUserId);
        createCallLogThread(nbCallToAdd, number, true);

        // Create 4 call logs with 10% of all the call for each user
        nbCallToAdd = (Config.NUMBER_OF_CALL_LOG * 10) / 100;
        for (int j = 0; j < 4; j++) {
            randomUserId = mGenerator.nextInt(Config.MAX_PHONE_NUMBER_IN_AB);
            number = Test.mABNumbers.get(randomUserId);
            createCallLogThread(nbCallToAdd, number, true);
        }

        // Create a call log with 10% of all the call for N contact in the AB
        nbCallToAdd = (Config.NUMBER_OF_CALL_LOG * 10) / 100;
        createCallLogThread(nbCallToAdd, null, true);

        // Create a call log with 10% of all the call for N contact not in the
        // AB
        nbCallToAdd = (Config.NUMBER_OF_CALL_LOG * 10) / 100;
        createCallLogThread(nbCallToAdd, null, false);

        Log.d(Config.TAG_APP, TAG + "create " + Config.NUMBER_OF_CALL_LOG
                + " CallLogs");

    }

    @Override
    public void deleteAll() {
        int d = mContext.getContentResolver().delete(CallLog.Calls.CONTENT_URI,
                null, null);
        Log.d(Config.TAG_APP, TAG + " delete " + d + " CallLogs");
    }

    @Override
    public int getCount() {
        int nb = -1;
        Cursor phones = mContext.getContentResolver().query(
                CallLog.Calls.CONTENT_URI, null, null, null, null);
        if (phones != null) {
            nb = phones.getCount();
            phones.close();
        }
        return nb;
    }
}
