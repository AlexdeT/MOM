package com.onmobile.mom.test;

import java.util.ArrayList;

import android.test.AndroidTestCase;
import android.text.format.DateFormat;
import android.util.Log;

import com.onmobile.api.sync.launcher.SyncDatabase;
import com.onmobile.api.sync.launcher.SyncProfile;
import com.onmobile.mom.accountmanager.MyAccount;
import com.onmobile.mom.app.Config;
import com.onmobile.mom.app.CoreServices;
import com.onmobile.mom.app.Utils;
import com.onmobile.mom.database.DatabaseManager;
import com.onmobile.mom.httprequest.PIMRequest;

/**
 * This class extends {@link AndroidTestCase}. It is the master controller of
 * the application. It manages all the test that has to be done.
 *
 * @author adetalouet
 */
public class Test extends AndroidTestCase {

    /**
     * Tag to debug
     */
    private static final String TAG = "Test - ";

    /**
     * List of all the numbers created
     */
    public static ArrayList<String> mABNumbers;
    /**
     * List of all the names creates
     */
    public static ArrayList<String> mABNames;

    /**
     * Use to configures the CoreServiceLib, and to use its features
     */
    private static CoreServices libCoreServices;


    @Override
    public void setUp() throws Exception {

        Log.d(Config.TAG_APP, TAG + DateFormat.format("hh:mm:ss", System.currentTimeMillis()));
        Log.d(Config.TAG_APP, TAG + "-------------START OF TEST----------");

        // Scan the SdCard
        Utils.scanSdCard(mContext);

        // Create all the databases needed for the tests
        setUpTestCase();

        // Configuration of the SDK
        setUpCoreServices();
    }

    /**
     * Set up the TestCase. Create {@link MyAccount}, and the device databases.
     */
    private void setUpTestCase() {
        Log.d(Config.TAG_APP, TAG + "------- setUp TestCase ------- " + DateFormat.format("hh:mm:ss", System.currentTimeMillis()));

        mABNames = new ArrayList<String>();
        mABNumbers = new ArrayList<String>();

        // Create a User account
        MyAccount.createAccount(mContext);

        // Delete the server database
        PIMRequest.deleteDatabases();

        // Initialize, Delete and Create the device database
        DatabaseManager.initDatabases(mContext);

        Log.d(Config.TAG_APP, TAG + "------- setUp TestCase ended ------- " + DateFormat.format("hh:mm:ss", System.currentTimeMillis()));
    }

    /**
     * Set up the CoreServices. Register the observers, the databases, set the
     * sync parameters and the authentication.
     */
    private void setUpCoreServices() {
        Log.d(Config.TAG_APP, TAG + "------- setUp CoreServices ------- " + DateFormat.format("hh:mm:ss", System.currentTimeMillis()));

        // Configures the SDK
        libCoreServices = new CoreServices(mContext);

        Log.d(Config.TAG_APP, TAG + "------- setUp CoreServices ended ------- " + DateFormat.format("hh:mm:ss", System.currentTimeMillis()));
    }

    @Override
    public void tearDown() {

        // Disable SDK functionalities
        libCoreServices.tearDown();

        // Delete server databases
		PIMRequest.deleteDatabases();
        // Delete device databases
        DatabaseManager.deleteDatabases(true);
        // If a calendar was created, delete it
        if (Config.SYNC_EVENT)
            DatabaseManager.getEventDataSet().deleteCalendar();
        // Delete the created Android account
        MyAccount.deleteAccount(mContext);

        Log.d(Config.TAG_APP, TAG + "-------------END OF TEST----------");
        Log.d(Config.TAG_APP, TAG + DateFormat.format("hh:mm:ss", System.currentTimeMillis()));
    }

    @Override
    public void runTest() throws Throwable {

        runTestBackup();

        // Delete the device databases to test the restore
        DatabaseManager.deleteDatabases(false);

        // TODO remove once SMS restore works - We unregister the SMS database here because it is not supported yet.
        if (Config.SYNC_SMS)
            libCoreServices.unregisterDatabase(SyncDatabase.SMS);

        runTestRestore();
    }

    /**
     * Lunch backup sync. During this test, we are updating the contact database
     * to maximize the test scope.
     *
     * @throws Exception
     */
    private void runTestBackup() throws Exception {
        Log.d(Config.TAG_APP, TAG + "------- runTestBackup ------- " + DateFormat.format("hh:mm:ss", System.currentTimeMillis()));

        // Launch a sync
        libCoreServices.startSync(SyncProfile.BACKUP);
//		libCoreServices.waitForSyncStarted();
//		if(!libCoreServices.iSSyncEnded){
//			Utils.disableNetwork();
//			libCoreServices.waitForSyncSuspended();
//			Utils.enableNetwork();
//			libCoreServices.startSync(SyncProfile.BACKUP);
        libCoreServices.waitForSyncEnded();
//		}
        // Check the results
        CheckResult.checkBackupResult(false, mContext);

        Log.d(Config.TAG_APP, TAG + DateFormat.format("hh:mm:ss", System.currentTimeMillis()));

        // Make some update in the databases
        if (Config.SYNC_CONTACT)
            DatabaseManager.updateContacts(false);
        if (Config.SYNC_SMS)
            DatabaseManager.updateSMS(false);
        if (Config.SYNC_CALL_LOG)
            DatabaseManager.updateCallLog(false);

        //Lunch a sync
        libCoreServices.startSync(SyncProfile.BACKUP);
        libCoreServices.waitForSyncEnded();
        // Check the results
        CheckResult.checkBackupResult(true, mContext);

        Log.d(Config.TAG_APP, TAG + "------- runTestBackup ended ------- " + DateFormat.format("hh:mm:ss", System.currentTimeMillis()));
    }

    /**
     * Lunch the restore sync.
     *
     * @throws Exception
     */
    private void runTestRestore() throws Exception {
        Log.d(Config.TAG_APP, TAG + "------- runTestRestore ------- " + DateFormat.format("hh:mm:ss", System.currentTimeMillis()));

        // Launch a sync
        libCoreServices.startSync(SyncProfile.RESTORE);
//		libCoreServices.waitForSyncStarted();
//		if(!libCoreServices.iSSyncEnded){
//			Utils.disableNetwork();
//			libCoreServices.waitForSyncSuspended();
//			Utils.enableNetwork();
//			libCoreServices.startSync(SyncProfile.RESTORE);
        libCoreServices.waitForSyncEnded();
//		}
        // Check the results
        CheckResult.checkRestoreResult(mContext);

        // Make some update in the databases
        if (Config.SYNC_CONTACT)
            DatabaseManager.updateContacts(true);
        /*
		TODO Uncomment once the SMS and Call Log restore works fine
		if(Config.SYNC_SMS)
		DatabaseManager.updateSMS(true);
		*/
        if (Config.SYNC_CALL_LOG)
            DatabaseManager.updateCallLog(true);

        // Launch a sync
        libCoreServices.startSync(SyncProfile.RESTORE);
        libCoreServices.waitForSyncEnded();
        // Check the results
        CheckResult.checkRestoreResult(mContext);

        Log.d(Config.TAG_APP, TAG + "------- runTestRestore ended ------- " + DateFormat.format("hh:mm:ss", System.currentTimeMillis()));
    }

    /**
     * Function to make the test. If the test failed, Log an error with the
     * scope and values attached
     *
     * @param scope    - the scope of the test
     * @param expected - the expected value
     * @param actual   - the actual value
     */
    public static void myAssert(String scope, Object expected, Object actual) {
        if (!expected.equals(actual))
            Log.e(Config.TAG_APP, TAG + scope + " - Expected value/key : " + expected + " - Actual value/key : " + actual);

        assertEquals(expected, actual);
    }
}
