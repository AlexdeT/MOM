package com.onmobile.mom.accountmanager;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

import com.onmobile.mom.app.Config;

/**
 * This class manage the creation of an {@link Account}. The will allow to
 * create a calendar and an other contact raw.
 *
 * @author adetalouet
 */
public abstract class MyAccount {

    /**
     * Tag to debug
     */
    private static final String TAG = "MyAccountManager - ";

    /**
     * The Android user account
     */
    private static Account mAccount;

    /**
     * This function creates an {@link Account} and add it to the
     * {@link AccountManager}. The ACCOUNT_NAME and ACCOUNT_TYPE can be set in
     * the {@link Config} class.
     *
     * @param context - the context of the test case
     */
    public static void createAccount(Context context) {

        mAccount = new Account(Config.ACCOUNT_NAME, Config.ACCOUNT_TYPE);

        AccountManager am = AccountManager.get(context);

        boolean accountCreated = am.addAccountExplicitly(mAccount,
                null, null);

        Log.d(Config.TAG_APP, TAG + "Account created: " + (accountCreated ? true : false + ". The account already exist"));
    }

    /**
     * This function deletes the created {@link Account}.
     * <p/>
     * TODO Doesn't work for now
     *
     * @param context - the context of the test case
     */
    public static void deleteAccount(Context context) {

        AccountManager am = AccountManager.get(context);
        am.removeAccount(mAccount, null, null);

        Account[] accounts = am.getAccounts();
        for (Account aAccount : accounts)
            if (aAccount.name == Config.ACCOUNT_NAME)
                Log.d(Config.TAG_APP, TAG + "Account not deleted");
            else
                Log.d(Config.TAG_APP, TAG + "Account deleted");
    }
}
