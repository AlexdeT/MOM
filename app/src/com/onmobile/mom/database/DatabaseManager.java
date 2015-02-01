package com.onmobile.mom.database;

import android.content.Context;
import android.util.Log;

import com.onmobile.mom.app.Config;

/**
 * This class manages the device databases.
 *
 * @author adetalouet
 */
public abstract class DatabaseManager {

    /**
     * Tag to debug
     */
    private static final String TAG = "Device - ";

    /**
     * Contain the call log
     */
    private static MyCallLog mCallLogDataSet;
    /**
     * Contain the contact
     */
    private static MyContact mContactDataSet;
    /**
     * Contain the event
     */
    private static MyCalendar mEventDataSet;
    /**
     * Contain the photos
     */
    private static MyPhoto mPhotoDataSet;
    /**
     * Contain the videos
     */
    private static MyVideo mVideoDataSet;
    /**
     * Contain the sms
     */
    private static MySMS mSmsDataSet;

    /**
     * Initialize all the data sets
     */
    public static void initDatabases(Context mContext) {
        if (Config.SYNC_CALL_LOG)
            mCallLogDataSet = new MyCallLog(mContext);
        if (Config.SYNC_CONTACT)
            mContactDataSet = new MyContact(mContext);
        if (Config.SYNC_EVENT)
            mEventDataSet = new MyCalendar(mContext);
        if (Config.SYNC_PHOTO)
            mPhotoDataSet = new MyPhoto(mContext);
        if (Config.SYNC_VIDEO)
            mVideoDataSet = new MyVideo(mContext);
        if (Config.SYNC_SMS)
            mSmsDataSet = new MySMS(mContext);

        deleteDatabases(true);
        createDatabases();
    }

    /**
     * Create and fill the data sets. The images and videos are added with a
     * script that has to be run before the test
     */
    public static void createDatabases() {
        Log.d(Config.TAG_APP, TAG + "-- createDeviceDatabases --");

        if (Config.SYNC_CONTACT)
            mContactDataSet.createDataSet();
        if (Config.SYNC_CALL_LOG)
            mCallLogDataSet.createDataSet();
        if (Config.SYNC_EVENT)
            mEventDataSet.createDataSet();
        if (Config.SYNC_SMS)
            mSmsDataSet.createDataSet();

    }

    /**
     * Delete all data sets.
     *
     * @param isSetUp - if true, means the function is called at the beginning of the
     *                test, in the <code> Test.setUp </code> function. As we upload photos and videos through adb push, we
     *                don't want to erase those databases at setUp.
     */
    public static void deleteDatabases(boolean isSetUp) {
        Log.d(Config.TAG_APP, TAG + "-- deleteDeviceDatabases --");

        if (Config.SYNC_CALL_LOG)
            mCallLogDataSet.deleteAll();
        if (Config.SYNC_CONTACT)
            mContactDataSet.deleteAll();
        if (Config.SYNC_EVENT)
            mEventDataSet.deleteAll();
        if (Config.SYNC_SMS)
            mSmsDataSet.deleteAll();
        if (!isSetUp) {
            if (Config.SYNC_PHOTO)
                mPhotoDataSet.deleteAll();
            if (Config.SYNC_VIDEO)
                mVideoDataSet.deleteAll();
        }
    }

    /**
     * Update the contact list
     *
     * @param isRestore - true if it is before a restore
     */
    public static void updateContacts(boolean isRestore) {
        mContactDataSet.updateDataSet(isRestore);
    }

    /**
     * Update the SMS list
     *
     * @param isRestore - true if it is before a restore
     */
    public static void updateSMS(boolean isRestore) {
        mSmsDataSet.updateDataSet(isRestore);
    }

    /**
     * Update the CallLog list
     *
     * @param isRestore - true if it is before a restore
     */
    public static void updateCallLog(boolean isRestore) {
        mCallLogDataSet.updateDataSet(isRestore);
    }


    /**
     * @return the CallLogDataSet
     */
    public static MyCallLog getCallLogDataSet() {
        return mCallLogDataSet;
    }

    /**
     * @return the ContactDataSet
     */
    public static MyContact getContactDataSet() {
        return mContactDataSet;
    }

    /**
     * @return the EventDataSet
     */
    public static MyCalendar getEventDataSet() {
        return mEventDataSet;
    }

    /**
     * @return the PhotoDataSet
     */
    public static MyPhoto getPhotoDataSet() {
        return mPhotoDataSet;
    }

    /**
     * @return the VideoDataSet
     */
    public static MyVideo getVideoDataSet() {
        return mVideoDataSet;
    }

    /**
     * @return the SmsDataSet
     */
    public static MySMS getSmsDataSet() {
        return mSmsDataSet;
    }
}
