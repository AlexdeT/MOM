package com.onmobile.mom.accountmanager.syncservices;

import android.accounts.Account;
import android.accounts.OperationCanceledException;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * This class is define in order to create the sync service that make a
 * rawcontact's creation possible. But the sync service is not used.
 *
 * @author adetalouet
 */
public class ContactsSyncAdapterService extends Service {
    private static final String TAG = "ContactsSyncAdapterService - ";
    private static SyncAdapterImpl sSyncAdapter = null;

    public ContactsSyncAdapterService() {
        super();
    }

    private static class SyncAdapterImpl extends AbstractThreadedSyncAdapter {
        private Context mContext;

        public SyncAdapterImpl(Context context) {
            super(context, true);
            mContext = context;
        }

        @Override
        public void onPerformSync(Account account, Bundle extras,
                                  String authority, ContentProviderClient provider,
                                  SyncResult syncResult) {
            try {
                ContactsSyncAdapterService.performSync(mContext, account,
                        extras, authority, provider, syncResult);
            } catch (OperationCanceledException e) {
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        IBinder ret = null;
        ret = getSyncAdapter().getSyncAdapterBinder();
        return ret;
    }

    private SyncAdapterImpl getSyncAdapter() {
        if (sSyncAdapter == null)
            sSyncAdapter = new SyncAdapterImpl(this);
        return sSyncAdapter;
    }

    private static void performSync(Context context, Account account,
                                    Bundle extras, String authority, ContentProviderClient provider,
                                    SyncResult syncResult) throws OperationCanceledException {
        Log.i(TAG, "performSync: " + account.toString());
    }
}
