package com.onmobile.mom.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.onmobile.mom.test.Test;

/**
 * This class contains useful tools for test purposes.
 *
 * @author adetalouet
 */
public abstract class Utils {

    /**
     * Tag to debug
     */
    private static final String TAG = "Utils - ";
    /**
     * Defines if the scan is ended
     */
    private static Boolean isScanEnded = false;
    /**
     * Scan listener. We get a callback when the scan is done.
     */
    private static OnScanCompletedListener scanCallback = new OnScanCompletedListener() {
        @Override
        public void onScanCompleted(String path, Uri uri) {
            isScanEnded = true;
        }
    };


    /**
     * This function defines a random number of 10 digits. It defines a phone
     * numbers for user
     */
    public static String randomNumber() {
        long number = (long) Math.floor(Math.random() * 9000000000L) + 1000000000L;
        String num = String.valueOf(number);

        // Check whether this number already exist or not
        while (Test.mABNumbers.contains(num)) {
            randomNumber();
            Log.d(Config.TAG_APP, TAG + "randomNumber -- recursive enter");
        }

        Test.mABNumbers.add(num);

        return num;
    }

    /**
     * This function defines a random name between 4 and 8 characters. It
     * defines the name of a user
     */
    public static String randomName() {
        Random generator = new Random();
        StringBuilder sb = new StringBuilder();

        // Contain all the chars that can be used to create the name
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        // Define the length of the name
        int randomLength = 4 + generator.nextInt(4);

        for (int i = 0; i < randomLength; i++) {
            char c = chars[generator.nextInt(chars.length)];
            sb.append(c);
        }

        // Format the String
        String name = sb.toString();
        name = Character.toUpperCase(name.charAt(0)) + name.substring(1);

        Test.mABNames.add(name);

        return name;
    }

    /**
     * Get the JSON Object from the InputStream returned by the query
     */
    public static JSONObject getJSONObject(InputStream json) {

        JSONObject jObj = null;
        String jsonLine = "";

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    json, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            json.close();
            jsonLine = sb.toString();
        } catch (Exception e) {
            Log.e(Config.TAG_APP, TAG + "getJSONObject : " + e);
            e.printStackTrace();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(jsonLine);
        } catch (JSONException e) {
            Log.e(Config.TAG_APP, TAG + "getJSONObject - parse failed : " + e);
            e.printStackTrace();
        }
        
        Log.d(Config.TAG_APP, TAG + "getJSONObject : " + jsonLine);
        
        return jObj;
    }

    /**
     * Function that scan the content of the SdCard
     *
     * @param context - the test case context
     */
    public static void scanSdCard(Context context) {
        Log.d(Config.TAG_APP, TAG + " -- scanSdCard -- ");

        File directory;
        List<String> pathsL = new ArrayList<String>();

        // get images path
        String filePathImg = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures";
        directory = new File(filePathImg);
        File[] images = directory.listFiles();
        if (images != null) {
            for (File image : images)
                pathsL.add(image.getAbsolutePath());

            Log.d(Config.TAG_APP, TAG + " -- scanSdCard : find " + images.length + " images");
        }


        // get videos path
        String filePathVids = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Videos";
        directory = new File(filePathVids);
        File[] videos = directory.listFiles();
        if (videos != null) {
            for (File video : videos)
                pathsL.add(video.getAbsolutePath());

            Log.d(Config.TAG_APP, TAG + " -- scanSdCard : find " + videos.length + " videos");
        }


        // if files are present, let's scan them
        if (pathsL.size() != 0) {
            String[] allPaths = new String[pathsL.size()];
            allPaths = pathsL.toArray(allPaths);

            MediaScannerConnection.scanFile(context, allPaths, null, scanCallback);
            waitForScanEnded();
        } else
            Log.d(Config.TAG_APP, TAG + "nothing to scan");
    }

    /**
     * This function lock the thread waiting for the scan to end
     *
     * @throws Exception
     */
    private static void waitForScanEnded() {
        Log.d(Config.TAG_APP, TAG + "waitForScanEnded");
        while (!isScanEnded) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e(Config.TAG_APP, TAG + "waitForScanEnded exception :  " + e);
            }
        }
        Log.d(Config.TAG_APP, TAG + " -- scan ended -- ");
    }

    /**
     * This method set a fake proxy in the emulator settings.
     * Thus it creates a network loss.
     */
    public static void disableNetwork() {
        Log.d(Config.TAG_APP, TAG + " -- disableNetwork -- ");
        System.getProperties().put("proxySet", "true");
        System.getProperties().put("proxyPort", "8080");
        System.getProperties().put("proxyHost", "http://localhost");
    }

    /**
     * This method removed the fake proxy setted.
     */
    public static void enableNetwork() {
        Log.d(Config.TAG_APP, TAG + " -- enableNetwork -- ");
        System.getProperties().remove("proxySet");
        System.getProperties().remove("proxyHost");
        System.getProperties().remove("proxyPort");
    }
}
