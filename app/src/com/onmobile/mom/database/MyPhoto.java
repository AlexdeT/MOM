package com.onmobile.mom.database;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.onmobile.mom.app.Config;

/**
 * This class manage the Android image database.
 *
 * @author adetalouet
 */
public class MyPhoto implements IDatabase {

    /**
     * Tag to debug
     */
    private static final String TAG = "MyPhoto -";

    /**
     * Test case context
     */
    private Context mContext;

    /**
     * Constructor
     *
     * @param context - the context of the test case
     */
    public MyPhoto(Context context) {
        mContext = context;
    }

    @Override
    public void createDataSet() {
        // Nothing
    }

    public int getCount() {
        int nb = -1;
        Cursor phones = mContext.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                null);
        if (phones != null) {
            nb = phones.getCount();
            phones.close();
        }
        return nb;
    }

    public void deleteAll() {
        int d = mContext.getContentResolver().delete(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null);
        Log.d(Config.TAG_APP, TAG + " delete " + d + " Photos");
    }

}
