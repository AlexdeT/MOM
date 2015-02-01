package com.onmobile.mom.app;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.onmobile.api.contactsimport.ContactsImportException;
import com.onmobile.api.sync.launcher.ServiceObserver;
import com.onmobile.api.sync.launcher.SyncConfigKey;
import com.onmobile.api.sync.launcher.SyncDatabase;
import com.onmobile.api.sync.launcher.SyncDatabaseConfigKey;
import com.onmobile.api.sync.launcher.SyncException;
import com.onmobile.api.sync.launcher.SyncLauncher;
import com.onmobile.api.sync.launcher.SyncLauncherFactory;
import com.onmobile.api.sync.launcher.SyncObserver;
import com.onmobile.api.sync.launcher.SyncProfile;
import com.onmobile.api.sync.launcher.SyncState;
import com.onmobile.api.sync.launcher.SyncStateKey;
import com.onmobile.mom.database.DatabaseManager;

/**
 * This class manages and configures the SDK, known as the CoreServiceLib
 * provide.
 *
 * @author adetalouet
 */
public class CoreServices implements ServiceObserver, SyncObserver {

    /**
     * Tag to debug
     */
    private static final String TAG = "CoreServices - ";

    /**
     * Test case context
     */
    private Context mContext;

    /**
     * Sync launcher from the CoreServiceLib
     */
    public SyncLauncher mSyncLauncher = null;
    /**
     * Defines if the service is started
     */
    public boolean iSServiceStarted;
    /**
     * Defines if the sync is started
     */
    public boolean iSSyncStarted;
    /**
     * Defines if the sync ended
     */
    public boolean iSSyncEnded;
    /**
     * Defines if the sync is suspended
     */
    public boolean iSSyncSuspended;

    /**
     * Defines the history size
     */
    public final int SYNC_HISTORY_SIZE = 1;
    /**
     * Map to store all the sync parameters
     */
    public Map<SyncConfigKey, Object> iSyncParameters;

    /**
     * Constructor. Init all the SDK parameters
     *
     * @param context - the test case context
     */
    public CoreServices(Context context) {
        mContext = context;

        iSServiceStarted = false;
        iSSyncStarted = false;
        iSSyncEnded = false;
        iSSyncSuspended = false;

        iSyncParameters = new HashMap<SyncConfigKey, Object>();

        createInstance();

        enableLogs(Config.ENABLE_SDK_LOG);

        registerObservers();

        setSyncparameters();

        setAuthentication();

        if (Config.SYNC_CONTACT)
            registerDatabase(SyncDatabase.CONTACT, null);
        if (Config.SYNC_CALL_LOG)
            registerDatabase(SyncDatabase.CALL_LOG, null);
        if (Config.SYNC_SMS)
            registerDatabase(SyncDatabase.SMS, null);
        if (Config.SYNC_EVENT) {
            Map<SyncDatabaseConfigKey, Object> eventConfiguration = new HashMap<SyncDatabaseConfigKey, Object>();
            eventConfiguration.put(SyncDatabaseConfigKey.CALENDAR_ID, DatabaseManager.getEventDataSet().getCalId());
            registerDatabase(SyncDatabase.EVENT, eventConfiguration);
        }
        if (Config.SYNC_VIDEO)
            registerDatabase(SyncDatabase.VIDEO, null);
        if (Config.SYNC_PHOTO)
            registerDatabase(SyncDatabase.PHOTO, null);
    }

    /**
     * Disable SDK functionalities
     */
    public void tearDown() {
        unregisterSyncObserver();

        unregisterServiceObserver();

        if (Config.SYNC_CONTACT)
            unregisterDatabase(SyncDatabase.CONTACT);
        if (Config.SYNC_CALL_LOG)
            unregisterDatabase(SyncDatabase.CALL_LOG);
        if (Config.SYNC_EVENT)
            unregisterDatabase(SyncDatabase.EVENT);
        if (Config.SYNC_VIDEO)
            unregisterDatabase(SyncDatabase.VIDEO);
        if (Config.SYNC_PHOTO)
            unregisterDatabase(SyncDatabase.PHOTO);

        closeInstance();

    }

    /**
     * Create a {@link SyncLauncher}
     *
     * @return the created {@link SyncLauncher}
     */
    public SyncLauncher createInstance() {
        Log.d(Config.TAG_APP, TAG + "CreateInstance");

        try {
            iSServiceStarted = false;
            mSyncLauncher = SyncLauncherFactory.createInstance(mContext);
        } catch (SyncException e) {
            Log.e(Config.TAG_APP, TAG + "CreateInstance " + e);
        } catch (ContactsImportException e) {
            Log.e(Config.TAG_APP, TAG + "CreateInstance " + e);
        }

        return mSyncLauncher;
    }

    /**
     * Close the instance
     */
    public void closeInstance() {
        Log.d(Config.TAG_APP, TAG + "closeInstance");

        try {
            SyncLauncherFactory.closeInstance(mContext);
        } catch (SyncException e) {
            Log.e(Config.TAG_APP, TAG + "testCloseInstance " + e);
        }
    }

    /**
     * Start the sync with the selected profile
     *
     * @param syncProfile - the sync profile (BACKUP/RESTORE)
     */
    public void startSync(SyncProfile syncProfile) {
        try {
            Log.d(Config.TAG_APP, TAG + " -- startSync -- resume : " + mSyncLauncher.resumeLastSync());
        } catch (SyncException e1) {
            Log.e(Config.TAG_APP, TAG + "resumeLastSync " + e1);
        }
        iSSyncEnded = false;
        iSSyncSuspended = false;
        try {
            mSyncLauncher.startSync(syncProfile, null);
        } catch (SyncException e) {
            Log.e(Config.TAG_APP, TAG + "startSync " + e);
        }
    }

    /**
     * Stop the sync.
     */
    public void stopSync() {
        Log.d(Config.TAG_APP, TAG + "stopSync ");

        try {
            mSyncLauncher.stopSync(true);
        } catch (SyncException e) {
            Log.e(Config.TAG_APP, TAG + "stopSync " + e);
        }
    }

    /**
     * This function lock the thread waiting for the service started
     *
     * @throws Exception
     */
    public void waitForServiceStarted() throws Exception {
        Log.d(Config.TAG_APP, TAG + "waitForServiceStarted");

        int i = 1;
        while (!iSServiceStarted) {
            // 1 min
            if (i == 60) {
                throw new Exception("initialization failed.");
            }
            try {
                Thread.sleep(1000);
                i++;
            } catch (InterruptedException e) {
                Log.e(Config.TAG_APP, TAG + "waitForServiceStarted exception :  " + e);

            }
        }
    }

    /**
     * This function lock the thread waiting for the sync ended
     *
     * @throws Exception
     */
    public void waitForSyncStarted() throws Exception {
        Log.d(Config.TAG_APP, TAG + "waitForSyncStarted");

        while (!iSSyncStarted) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e(Config.TAG_APP, TAG + "waitForSyncStarted exception :  " + e);

            }
        }
        // 7s while the sync really starts
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            Log.e(Config.TAG_APP, TAG + "waitForSyncStarted exception :  " + e);

        }
    }

    /**
     * This function lock the thread waiting for the sync to be suspended
     *
     * @throws Exception
     */
    public void waitForSyncSuspended() throws Exception {
        Log.d(Config.TAG_APP, TAG + "waitForSyncSuspended");

        int i = 1;
        while (!iSSyncSuspended) {
            // 20 sec
            if (i == 40) {
                Log.e(Config.TAG_APP, TAG + "Suspend sync failed - waited " + i + " sec");
                throw new Exception("Suspend sync failed");
            }
            try {
                Thread.sleep(1000);
                i++;
            } catch (InterruptedException e) {
                Log.e(Config.TAG_APP, TAG + "waitForSyncSuspended exception :  " + e);

            }
        }
    }

    /**
     * This function lock the thread waiting for the sync ended
     *
     * @throws Exception
     */
    public void waitForSyncEnded() throws Exception {
        Log.d(Config.TAG_APP, TAG + "waitForSyncEnded");

        while (!iSSyncEnded) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e(Config.TAG_APP, TAG + "waitForSyncEnded exception :  " + e);
            }
        }
    }

    /**
     * Register observers
     */
    public void registerObservers() {
        registerServiceObserver();
    }

    /**
     * Register service observer
     */
    public void registerServiceObserver() {
        Log.d(Config.TAG_APP, TAG + "registerServiceObserver");

        try {
            mSyncLauncher.registerServiceObserver(this);
            waitForServiceStarted();
        } catch (SyncException e) {
            Log.e(Config.TAG_APP, TAG + "registerServiceObserver : " + e);
        } catch (Exception e) {
            Log.e(Config.TAG_APP, TAG + "registerServiceObserver : " + e);
        }
    }

    /**
     * Register sync observer
     */
    public void registerSyncObserver() {
        Log.d(Config.TAG_APP, TAG + "registerSyncObserver");

        try {
            mSyncLauncher.registerSyncObserver(this);
        } catch (SyncException e) {
            Log.e(Config.TAG_APP, TAG + "registerSyncObserver : " + e);
        }
    }

    /**
     * Unregister sync observer
     */
    public void unregisterSyncObserver() {
        Log.d(Config.TAG_APP, TAG + "unregisterSyncObserver");

        try {
            mSyncLauncher.unregisterSyncObserver(this);
        } catch (SyncException e) {
            Log.e(Config.TAG_APP, TAG
                    + "unregisterSyncObserver sync observer: " + e);
        }
    }

    /**
     * Unregister service observer
     */
    public void unregisterServiceObserver() {
        Log.d(Config.TAG_APP, TAG + "unregisterServiceObserver");

        try {
            mSyncLauncher.unregisterServiceObserver(this);
        } catch (SyncException e) {
            Log.e(Config.TAG_APP, TAG + "unregisterServiceObserver : " + e);
        }
    }

    /**
     * Set the sync parameters
     */
    public void setSyncparameters() {
        Log.d(Config.TAG_APP, TAG + "setSyncparameters ");

        iSyncParameters.put(SyncConfigKey.ENQUEUE_SYNC, false);
        iSyncParameters.put(SyncConfigKey.ENABLE_AUTHENTICATION_MD5, false);
        iSyncParameters.put(SyncConfigKey.SERVER_URL, Config.SYNCSERVER_URL);
        iSyncParameters.put(SyncConfigKey.SERVER_WIFI_URL,
                Config.SYNCSERVER_URL);
        iSyncParameters.put(SyncConfigKey.STOP_IF_NO_CHANGE, false);
        iSyncParameters.put(SyncConfigKey.SYNC_HISTORY_SIZE, SYNC_HISTORY_SIZE);
        iSyncParameters.put(SyncConfigKey.SYNC_OVER_WIFI, false);
        iSyncParameters
                .put(SyncConfigKey.SYNC_RESET_DATA_AFTER_SIM_SWAP, false);
        iSyncParameters.put(SyncConfigKey.SYNC_STOP_LOW_BATTERY, false);
        iSyncParameters.put(SyncConfigKey.SYNC_WHILE_ROAMING, false);
        try {
            mSyncLauncher.setSyncParameters(iSyncParameters);
        } catch (SyncException e) {
            Log.e(Config.TAG_APP, TAG + "setSyncparameters " + e);
        }
    }

    /**
     * Set the authentication
     */
    public void setAuthentication() {
        Log.d(Config.TAG_APP, TAG + "setAuthentication ");
        try {
            mSyncLauncher.setAuthentication(Config.LOGIN, Config.PASSWORD);
        } catch (SyncException e) {
            Log.e(Config.TAG_APP, TAG + "setAuthentication " + e);
        }
    }

    /**
     * Register a database
     *
     * @param a_Database - the database to register
     * @param a_Config   - the database configuration
     */
    public void registerDatabase(SyncDatabase a_Database,
                                 Map<SyncDatabaseConfigKey, Object> a_Config) {
        Log.d(Config.TAG_APP, TAG + "registerDatabase " + a_Database);

        try {
            mSyncLauncher.registerDatabase(a_Database, a_Config);
        } catch (SyncException e) {
            Log.e(Config.TAG_APP, TAG + "registerDatabase " + e);
        }
    }

    /**
     * Unregister a database
     *
     * @param a_Database - the database to unregister
     */
    public void unregisterDatabase(SyncDatabase a_Database) {
        Log.d(Config.TAG_APP, TAG + "unregisterDatabase " + a_Database);

        try {
            mSyncLauncher.unregisterDatabase(a_Database);
        } catch (SyncException e) {
            Log.e(Config.TAG_APP, TAG + "unregisterDatabase " + e);
        }
    }

    /**
     * Enable log
     *
     * @param isEnable - if the logs are enable
     */
    private void enableLogs(boolean isEnable) {
        Log.d(Config.TAG_APP, TAG + "enableLogs ");

        mSyncLauncher.enableLogs(isEnable);
    }

    static int i = 0;
    static int j = 0;

    /**
     * Callback giving the sync state
     *
     * @see com.onmobile.api.sync.launcher.SyncObserver#onSyncState(com.onmobile.api.sync.launcher.SyncState,
     * java.util.Map)
     */
    @Override
    public void onSyncState(SyncState arg0, Map<SyncStateKey, Object> arg1) {
        if (arg0 == SyncState.END_SYNC) {
            iSSyncEnded = true;
            Log.d(Config.TAG_APP, TAG + arg0);
        }
        if (arg0 == SyncState.SUSPENDED) {
            iSSyncSuspended = true;
            Log.d(Config.TAG_APP, TAG + arg0);
        }
        if (arg0 == SyncState.START_SYNC) {
            iSSyncStarted = true;
            Log.d(Config.TAG_APP, TAG + arg0);
        }
        if (arg0 == SyncState.SENDING_ITEMS) {
//			i++;
//			if(i%20==0)
//				Log.d(Config.TAG_APP, TAG + " - "+ i + " items sent");
        }
        if (arg0 == SyncState.RECEIVING_ITEMS) {
//			j++;
//			if(j%20==0)
//				Log.d(Config.TAG_APP, TAG + " - " + j + " items received");
        }
    }

    /**
     * Callback on service started
     *
     * @see com.onmobile.api.sync.launcher.ServiceObserver#onServiceStarted()
     */
    @Override
    public void onServiceStarted() {
        Log.d(Config.TAG_APP, TAG + "onServiceStarted");
        iSServiceStarted = true;
        registerSyncObserver();
    }
}
