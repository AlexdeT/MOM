package com.onmobile.mom.database;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.onmobile.mom.app.Config;


/**
 * This class manage the Android video database.
 *
 * @author adetalouet
 */
public class MyVideo implements IDatabase {

    /**
     * Tag to debug
     */
    private static final String TAG = "MyVideo - ";

    /**
     * Test case context
     */
    private Context mContext;

    /**
     * Constructor
     *
     * @param context - the context of the test case
     */
    public MyVideo(Context context) {
        mContext = context;
    }

    @Override
    public void createDataSet() {
        // Nothing
    }

    @Override
    public int getCount() {
        int nb = -1;
        Cursor phones = mContext.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null,
                null);
        if (phones != null) {
            phones.close();
            nb = phones.getCount();
        }
        return nb;
    }

    @Override
    public void deleteAll() {
        int d = mContext.getContentResolver().delete(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null);
        Log.d(Config.TAG_APP, TAG + " delete " + d + " Videos");
    }

}
